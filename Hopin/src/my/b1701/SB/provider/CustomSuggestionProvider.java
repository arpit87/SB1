package my.b1701.SB.provider;

import android.R;
import android.app.SearchManager;
import android.content.*;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

public class CustomSuggestionProvider extends ContentProvider {

    // debugging support
    private static final String TAG = "my.b1701.SB.provider.SuggestionsProvider";

    // client-provided configuration values
    private String mAuthority;
    private int mMode;
    //private boolean mTwoLineDisplay;

    // general database configuration and tables
    private SQLiteOpenHelper mOpenHelper;
    private static final String sDatabaseName = "suggestions.db";
    private static final String sSuggestions = "suggestions";
    private static final String ORDER_BY = "date DESC";
    private static final String NULL_COLUMN = "query";
    private static final String searchIcon = "android.resource://system/" + R.drawable.ic_menu_search;
    // Table of database versions.  Don't forget to update!
    // NOTE:  These version values are shifted left 8 bits (x 256) in order to create space for
    // a small set of mode bitflags in the version int.
    //
    // 1      original implementation with queries, and 1 or 2 display columns
    // 1->2   added UNIQUE constraint to display1 column
    private static final int DATABASE_VERSION = 2 * 256;

    /**
     * This mode bit configures the database to record recent queries.  <i>required</i>
     *
     * @see #setupSuggestions(String, int)
     */
    public static final int DATABASE_MODE_QUERIES = 1;

    // Uri and query support
    private static final int URI_MATCH_SUGGEST = 1;
    private static final int URI_MATCH_DB_FETCH_ONLY = 2;

    public static final String DB_FETCH_ONLY ="db_fetch_only";

    private Uri mSuggestionsUri;
    private UriMatcher mUriMatcher;

    private String mSuggestSuggestionClause;
    private String[] mSuggestionProjection;


    public final static String AUTHORITY = "my.b1701.SB.provider.CustomSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public CustomSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    /**
     * Builds the database.  This version has extra support for using the version field
     * as a mode flags field, and configures the database columns depending on the mode bits
     * (features) requested by the extending class.
     *
     * @hide
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context, int newVersion) {
            super(context, sDatabaseName, null, newVersion);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE suggestions (" +
                    "_id INTEGER PRIMARY KEY" +
                    ",display1 TEXT UNIQUE ON CONFLICT REPLACE" +
                    ",query TEXT" +
                    ",data TEXT" +
                    ",date LONG" +
                    ");");
            db.execSQL(builder.toString());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS suggestions");
            onCreate(db);
        }
    }

    protected void setupSuggestions(String authority, int mode) {
        if (TextUtils.isEmpty(authority) ||
                ((mode & DATABASE_MODE_QUERIES) == 0)) {
            throw new IllegalArgumentException();
        }

        // saved values
        mAuthority = new String(authority);
        mMode = mode;

        // derived values
        mSuggestionsUri = Uri.parse("content://" + mAuthority + "/suggestions");
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(mAuthority, SearchManager.SUGGEST_URI_PATH_QUERY, URI_MATCH_SUGGEST);
        mUriMatcher.addURI(mAuthority, DB_FETCH_ONLY, URI_MATCH_DB_FETCH_ONLY);

        mSuggestSuggestionClause = "display1 LIKE ?";
        mSuggestionProjection = new String[]{
                "0 AS " + SearchManager.SUGGEST_COLUMN_FORMAT,
                "'android.resource://system/"
                        + R.drawable.ic_menu_recent_history + "' AS "
                        + SearchManager.SUGGEST_COLUMN_ICON_1,
                "display1 AS " + SearchManager.SUGGEST_COLUMN_TEXT_1,
                "query AS " + SearchManager.SUGGEST_COLUMN_QUERY,
                "_id",
                "'" + Intent.ACTION_VIEW + "' AS " + SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
                "data AS " + SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                "'true' AS " + SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
        };

    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        final int length = uri.getPathSegments().size();
        if (length != 1) {
            throw new IllegalArgumentException("Unknown Uri");
        }

        final String base = uri.getPathSegments().get(0);
        int count = 0;
        if (base.equals(sSuggestions)) {
            count = db.delete(sSuggestions, selection, selectionArgs);
        } else {
            throw new IllegalArgumentException("Unknown Uri");
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @Override
    public String getType(Uri uri) {
        if (mUriMatcher.match(uri) == URI_MATCH_SUGGEST) {
            return SearchManager.SUGGEST_MIME_TYPE;
        }
        int length = uri.getPathSegments().size();
        if (length >= 1) {
            String base = uri.getPathSegments().get(0);
            if (base.equals(sSuggestions)) {
                if (length == 1) {
                    return "vnd.android.cursor.dir/suggestion";
                } else if (length == 2) {
                    return "vnd.android.cursor.item/suggestion";
                }
            }
        }
        throw new IllegalArgumentException("Unknown Uri");
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();

        int length = uri.getPathSegments().size();
        if (length < 1) {
            throw new IllegalArgumentException("Unknown Uri");
        }
        // Note:  This table has on-conflict-replace semantics, so insert() may actually replace()
        long rowID = -1;
        String base = uri.getPathSegments().get(0);
        Uri newUri = null;
        if (base.equals(sSuggestions)) {
            if (length == 1) {
                rowID = db.insert(sSuggestions, NULL_COLUMN, values);
                if (rowID > 0) {
                    newUri = Uri.withAppendedPath(mSuggestionsUri, String.valueOf(rowID));
                }
            }
        }
        if (rowID < 0) {
            throw new IllegalArgumentException("Unknown Uri");
        }
        getContext().getContentResolver().notifyChange(newUri, null);
        return newUri;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @Override
    public boolean onCreate() {
        if (mAuthority == null || mMode == 0) {
            throw new IllegalArgumentException("Provider not configured");
        }
        int mWorkingDbVersion = DATABASE_VERSION + mMode;
        mOpenHelper = new DatabaseHelper(getContext(), mWorkingDbVersion);

        return true;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    // TODO: Confirm no injection attacks here, or rewrite.
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        SQLiteDatabase db = mOpenHelper.getReadableDatabase();

        // special case for actual suggestions (from search manager)
        int match = mUriMatcher.match(uri);
        if (match == URI_MATCH_DB_FETCH_ONLY){
            String suggestSelection;
            String[] myArgs = new String[]{selectionArgs[0]};
            suggestSelection = "display1 = ?";

            Cursor cSavedSug = db.query(sSuggestions, projection,
                    suggestSelection, myArgs, null, null, ORDER_BY, null);
            cSavedSug.setNotificationUri(getContext().getContentResolver(), uri);
            return  cSavedSug;
        }
        else if (match == URI_MATCH_SUGGEST) {
            String suggestSelection;
            String[] myArgs;
            if (TextUtils.isEmpty(selectionArgs[0])) {
                suggestSelection = null;
                myArgs = null;
            } else {
                String like = "%" + selectionArgs[0] + "%";
                myArgs = new String[]{like};
                suggestSelection = mSuggestSuggestionClause;
            }

            // Suggestions are always performed with the default sort order
            Cursor cSavedSug = db.query(sSuggestions, mSuggestionProjection,
                    suggestSelection, myArgs, null, null, ORDER_BY, null);
            cSavedSug.setNotificationUri(getContext().getContentResolver(), uri);

            if (TextUtils.isEmpty(selectionArgs[0])) {
                return cSavedSug;
            }

            //fetch keys stored in the db to be used to avoid duplication in results
            Set<String> keys = new HashSet<String>();
            if (cSavedSug.moveToFirst()) {
                do {
                    keys.add(cSavedSug.getString(2));
                } while (cSavedSug.moveToNext());
            }

            Uri urlForCustomSug = Uri.parse(GeoAddressProvider.CONTENT_URI + "/" + selectionArgs[0]);
            Cursor cCustSug = getContext().getContentResolver().query(urlForCustomSug, null, null, null, "");

            if (cCustSug == null || cCustSug.getCount() == 0)
                return cSavedSug;


            MatrixCursor cCombSug = new MatrixCursor(new String[]{SearchManager.SUGGEST_COLUMN_FORMAT,
                    SearchManager.SUGGEST_COLUMN_ICON_1,
                    SearchManager.SUGGEST_COLUMN_TEXT_1,
                    SearchManager.QUERY, "_id",
                    SearchManager.SUGGEST_COLUMN_INTENT_ACTION,
                    SearchManager.SUGGEST_COLUMN_INTENT_DATA,
                    SearchManager.SUGGEST_COLUMN_INTENT_EXTRA_DATA
            });


            int offset = 1000;
            if (cCustSug.moveToFirst()) {
                do {
                    try {
                        String name = GeoAddress.getName(cCustSug.getString(0));
                        if (keys.contains(name)) {
                            continue;
                        }
                        cCombSug.addRow(new Object[]{0, searchIcon, name, selectionArgs[0], offset++, Intent.ACTION_VIEW, cCustSug.getString(0), "false"});
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } while (cCustSug.moveToNext());
            }

            MergeCursor c = new MergeCursor(new Cursor[]{cSavedSug, cCombSug});
            return c;
        }

        // otherwise process arguments and perform a standard query
        int length = uri.getPathSegments().size();
        if (length != 1 && length != 2) {
            throw new IllegalArgumentException("Unknown Uri");
        }

        String base = uri.getPathSegments().get(0);
        if (!base.equals(sSuggestions)) {
            throw new IllegalArgumentException("Unknown Uri");
        }

        String[] useProjection = null;
        if (projection != null && projection.length > 0) {
            useProjection = new String[projection.length + 1];
            System.arraycopy(projection, 0, useProjection, 0, projection.length);
            useProjection[projection.length] = "_id AS _id";
        }

        StringBuilder whereClause = new StringBuilder(256);
        if (length == 2) {
            whereClause.append("(_id = ").append(uri.getPathSegments().get(1)).append(")");
        }

        // Tack on the user's selection, if present
        if (selection != null && selection.length() > 0) {
            if (whereClause.length() > 0) {
                whereClause.append(" AND ");
            }

            whereClause.append('(');
            whereClause.append(selection);
            whereClause.append(')');
        }

        // And perform the generic query as requested
        Cursor c = db.query(base, useProjection, whereClause.toString(),
                selectionArgs, null, null, sortOrder,
                null);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    /**
     * This method is provided for use by the ContentResolver.  Do not override, or directly
     * call from your own code.
     */
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

}
