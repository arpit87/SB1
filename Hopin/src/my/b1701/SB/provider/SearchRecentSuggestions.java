package my.b1701.SB.provider;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.Semaphore;

public class SearchRecentSuggestions {
    // debugging support
    private static final String LOG_TAG = "SearchSuggestions";

    // This is a superset of all possible column names (need not all be in table)
    private static class SuggestionColumns implements BaseColumns {
        public static final String DISPLAY1 = "display1";
        public static final String QUERY = "query";
        public static final String LOCATION = "data";
        public static final String DATE = "date";
    }

    /* if you change column order you must also change indices below */
    /**
     * This is the database projection that can be used to view saved queries, when
     * configured for one-line operation.
     */
    public static final String[] QUERIES_PROJECTION_1LINE = new String[] {
            SuggestionColumns._ID,
            SuggestionColumns.DATE,
            SuggestionColumns.QUERY,
            SuggestionColumns.DISPLAY1,
    };

    /*
     * Set a cap on the count of items in the suggestions table, to
     * prevent db and layout operations from dragging to a crawl. Revisit this
     * cap when/if db/layout performance improvements are made.
     */
    private static final int MAX_HISTORY_COUNT = 250;

    // client-provided configuration values
    private final Context mContext;
    private final String mAuthority;
    private final Uri mSuggestionsUri;
    private final Uri mDBFetchUri;


    /** Released once per completion of async write.  Used for tests. */
    private static final Semaphore sWritesInProgress = new Semaphore(0);

    /**
     * Although provider utility classes are typically static, this one must be constructed
     * because it needs to be initialized using the same values that you provided in your
     * {@link android.content.SearchRecentSuggestionsProvider}.
     *
     * @param authority This must match the authority that you've declared in your manifest.
     * @param mode You can use mode flags here to determine certain functional aspects of your
     * database.  Note, this value should not change from run to run, because when it does change,
     * your suggestions database may be wiped.
     *
     * @see android.content.SearchRecentSuggestionsProvider
     * @see android.content.SearchRecentSuggestionsProvider#setupSuggestions
     */
    public SearchRecentSuggestions(Context context, String authority, int mode) {
        if (TextUtils.isEmpty(authority) ||
                ((mode & SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES) == 0)) {
            throw new IllegalArgumentException();
        }

        // saved values
        mContext = context;
        mAuthority = new String(authority);

        // derived values
        mSuggestionsUri = Uri.parse("content://" + mAuthority + "/suggestions");
        mDBFetchUri = Uri.parse("content://" + mAuthority + "/" + CustomSuggestionProvider.DB_FETCH_ONLY);
    }

   public void saveRecentQuery(final String queryString, final String data) {
        if (TextUtils.isEmpty(queryString)) {
            return;
        }

        new Thread("saveRecentQuery") {
            @Override
            public void run() {
                saveRecentQueryBlocking(queryString, data);
                sWritesInProgress.release();
            }
        }.start();
    }

    // Visible for testing.
    void waitForSave() {
        // Acquire writes semaphore until there is nothing available.
        // This is to clean up after any previous callers to saveRecentQuery
        // who did not also call waitForSave().
        do {
            sWritesInProgress.acquireUninterruptibly();
        } while (sWritesInProgress.availablePermits() > 0);
    }

    private void saveRecentQueryBlocking(String queryString, String location) {
        ContentResolver cr = mContext.getContentResolver();
        Cursor cursor = cr.query(mDBFetchUri, new String[]{SuggestionColumns.DISPLAY1}, SuggestionColumns.DISPLAY1, new String[] {queryString}, null);
        if (cursor == null || (cursor != null && cursor.getCount() > 0)){
            return;
        }

        long now = System.currentTimeMillis();

        // Use content resolver (not cursor) to insert/update this query
        try {
            ContentValues values = new ContentValues();
            values.put(SuggestionColumns.DISPLAY1, queryString);
            values.put(SuggestionColumns.QUERY, queryString);
            values.put(SuggestionColumns.DATE, now);
            values.put(SuggestionColumns.LOCATION, location);
            cr.insert(mSuggestionsUri, values);
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "saveRecentQuery", e);
        }

        // Shorten the list (if it has become too long)
        truncateHistory(cr, MAX_HISTORY_COUNT);
    }

    /**
     * Completely delete the history.  Use this call to implement a "clear history" UI.
     *
     * Any application that implements search suggestions based on previous actions (such as
     * recent queries, page/items viewed, etc.) should provide a way for the user to clear the
     * history.  This gives the user a measure of privacy, if they do not wish for their recent
     * searches to be replayed by other users of the device (via suggestions).
     */
    public void clearHistory() {
        ContentResolver cr = mContext.getContentResolver();
        truncateHistory(cr, 0);
    }

    /**
     * Reduces the length of the history table, to prevent it from growing too large.
     *
     * @param cr Convenience copy of the content resolver.
     * @param maxEntries Max entries to leave in the table. 0 means remove all entries.
     */
    protected void truncateHistory(ContentResolver cr, int maxEntries) {
        if (maxEntries < 0) {
            throw new IllegalArgumentException();
        }

        try {
            // null means "delete all".  otherwise "delete but leave n newest"
            String selection = null;
            if (maxEntries > 0) {
                selection = "_id IN " +
                        "(SELECT _id FROM suggestions" +
                        " ORDER BY " + SuggestionColumns.DATE + " DESC" +
                        " LIMIT -1 OFFSET " + String.valueOf(maxEntries) + ")";
            }
            cr.delete(mSuggestionsUri, selection, null);
        } catch (RuntimeException e) {
            Log.e(LOG_TAG, "truncateHistory", e);
        }
    }
}
