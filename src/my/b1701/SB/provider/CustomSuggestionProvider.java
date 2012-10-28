package my.b1701.SB.provider;

import android.R;
import android.app.SearchManager;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class CustomSuggestionProvider extends ContentProvider {

    // debugging support
    private static final String TAG = "SuggestionsProvider";

    // client-provided configuration values
    private String mAuthority;
    private int mMode;
    private boolean mTwoLineDisplay;

    // general database configuration and tables
    private SQLiteOpenHelper mOpenHelper;
    private static final String sDatabaseName = "suggestions.db";
    private static final String sSuggestions = "suggestions";
    private static final String ORDER_BY = "date DESC";
    private static final String NULL_COLUMN = "query";

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
    /**
     * This mode bit configures the database to include a 2nd annotation line with each entry.
     * <i>optional</i>
     *
     * @see #setupSuggestions(String, int)
     */
    public static final int DATABASE_MODE_2LINES = 2;

    // Uri and query support
    private static final int URI_MATCH_SUGGEST = 1;

    private Uri mSuggestionsUri;
    private UriMatcher mUriMatcher;

    private String mSuggestSuggestionClause;
    private String[] mSuggestionProjection;


    public final static String AUTHORITY = "my.b1701.SB.provider.CustomSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public CustomSuggestionProvider(){
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

        private int mNewVersion;

        public DatabaseHelper(Context context, int newVersion) {
            super(context, sDatabaseName, null, newVersion);
            mNewVersion = newVersion;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            StringBuilder builder = new StringBuilder();
            builder.append("CREATE TABLE suggestions (" +
                    "_id INTEGER PRIMARY KEY" +
                    ",display1 TEXT UNIQUE ON CONFLICT REPLACE");
            if (0 != (mNewVersion & DATABASE_MODE_2LINES)) {
                builder.append(",display2 TEXT");
            }
            builder.append(",query TEXT" +
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

    /**
     * In order to use this class, you must extend it, and call this setup function from your
     * constructor.  In your application or activities, you must provide the same values when
     * you create the {@link android.provider.SearchRecentSuggestions} helper.
     *
     * @param authority This must match the authority that you've declared in your manifest.
     * @param mode      You can use mode flags here to determine certain functional aspects of your
     *                  database.  Note, this value should not change from run to run, because when it does change,
     *                  your suggestions database may be wiped.
     * @see #DATABASE_MODE_QUERIES
     * @see #DATABASE_MODE_2LINES
     */
    protected void setupSuggestions(String authority, int mode) {
        if (TextUtils.isEmpty(authority) ||
                ((mode & DATABASE_MODE_QUERIES) == 0)) {
            throw new IllegalArgumentException();
        }
        // unpack mode flags
        mTwoLineDisplay = (0 != (mode & DATABASE_MODE_2LINES));

        // saved values
        mAuthority = new String(authority);
        mMode = mode;

        // derived values
        mSuggestionsUri = Uri.parse("content://" + mAuthority + "/suggestions");
        mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        mUriMatcher.addURI(mAuthority, SearchManager.SUGGEST_URI_PATH_QUERY, URI_MATCH_SUGGEST);

        if (mTwoLineDisplay) {
            mSuggestSuggestionClause = "display1 LIKE ? OR display2 LIKE ?";

            mSuggestionProjection = new String[]{
                    "0 AS " + SearchManager.SUGGEST_COLUMN_FORMAT,
                    "'android.resource://system/"
                            + R.drawable.ic_menu_recent_history + "' AS "
                            + SearchManager.SUGGEST_COLUMN_ICON_1,
                    "display1 AS " + SearchManager.SUGGEST_COLUMN_TEXT_1,
                    "display2 AS " + SearchManager.SUGGEST_COLUMN_TEXT_2,
                    "query AS " + SearchManager.SUGGEST_COLUMN_QUERY,
                    "_id"
            };
        } else {
            mSuggestSuggestionClause = "display1 LIKE ?";

            mSuggestionProjection = new String[]{
                    "0 AS " + SearchManager.SUGGEST_COLUMN_FORMAT,
                    "'android.resource://system/"
                            + R.drawable.ic_menu_recent_history + "' AS "
                            + SearchManager.SUGGEST_COLUMN_ICON_1,
                    "display1 AS " + SearchManager.SUGGEST_COLUMN_TEXT_1,
                    "query AS " + SearchManager.SUGGEST_COLUMN_QUERY,
                    "_id",
                    "'android.intent.action.SEARCH' AS " + SearchManager.SUGGEST_COLUMN_INTENT_ACTION
            };
        }


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
        if (mUriMatcher.match(uri) == URI_MATCH_SUGGEST) {
            String suggestSelection;
            String[] myArgs;
            if (TextUtils.isEmpty(selectionArgs[0])) {
                suggestSelection = null;
                myArgs = null;
            } else {
                String like = "%" + selectionArgs[0] + "%";
                if (mTwoLineDisplay) {
                    myArgs = new String[]{like, like};
                } else {
                    myArgs = new String[]{like};
                }
                suggestSelection = mSuggestSuggestionClause;
            }
            // Suggestions are always performed with the default sort order
            Cursor c1 = db.query(sSuggestions, mSuggestionProjection,
                    suggestSelection, myArgs, null, null, ORDER_BY, null);
            c1.setNotificationUri(getContext().getContentResolver(), uri);

            if (TextUtils.isEmpty(selectionArgs[0])){
                return c1;
            }

            Uri uri2  = Uri.parse(GeoAddressProvider.CONTENT_URI + "/" + selectionArgs[0]);
            Cursor c2 = getContext().getContentResolver().query(uri2, null, null, null, "");
            
            if (c2 == null || c2.getCount() == 0)
                return c1;


            MatrixCursor c3 = new MatrixCursor(new String[]{SearchManager.SUGGEST_COLUMN_FORMAT, SearchManager.SUGGEST_COLUMN_ICON_1, SearchManager.SUGGEST_COLUMN_TEXT_1, SearchManager.QUERY, "_id", SearchManager.SUGGEST_COLUMN_INTENT_ACTION});
            int offset = 1000;
            if (c2.moveToFirst()) {
                do {
                    c3.addRow(new Object[]{0, null, c2.getString(0), selectionArgs[0], offset++, "android.intent.action.VIEW"});
                } while (c2.moveToNext());
            }

            MergeCursor c = new MergeCursor(new Cursor[]{c1, c3});
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
