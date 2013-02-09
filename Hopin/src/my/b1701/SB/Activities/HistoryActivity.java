package my.b1701.SB.Activities;

import android.app.ListActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import my.b1701.SB.Adapter.HistoryAdapter;
import my.b1701.SB.provider.HistoryContentProvider;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HistoryActivity extends ListActivity {
    private static final String TAG = "my.b1701.SB.Activites.HistoryActivity";

    private static Uri mHistoryUri = Uri.parse("content://" + HistoryContentProvider.AUTHORITY + "/db_fetch_only");
    private static String[] columns = new String[]{ "sourceLocation",
                                                    "destinationLocation",
                                                    "timeOfRequest",
                                                    "dailyInstantType",
                                                    "takeOffer",
                                                    "date"};

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        HistoryAdapter adapter = new HistoryAdapter(this, fetchHistory());
        setListAdapter(adapter);
    }

    private List<HistoryAdapter.HistoryItem> fetchHistory() {
        Log.e(TAG, "Fetching searches");
        ContentResolver cr = getContentResolver();
        Cursor cursor = cr.query(mHistoryUri, columns, null, null, null);

        if (cursor == null || cursor.getCount() == 0) {
            Log.e(TAG, "Empty result");
            return Collections.EMPTY_LIST;
        } else {
            List<HistoryAdapter.HistoryItem> historyItems = new ArrayList<HistoryAdapter.HistoryItem>();
            if (cursor.moveToFirst()) {
                do {
                    HistoryAdapter.HistoryItem historyItem = new HistoryAdapter.HistoryItem(cursor.getString(0),
                            cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4),
                            new Date(cursor.getLong(5)));
                    Log.e(TAG, historyItem.getSourceLocation() + " : " + historyItem.getDestinationLocation());
                    historyItems.add(historyItem);
                } while (cursor.moveToNext());

                cursor.close();
            }

            return historyItems;
        }
    }

}
