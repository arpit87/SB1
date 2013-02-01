package my.b1701.SB.Adapter;

import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import my.b1701.SB.provider.CustomSuggestionProvider;
import my.b1701.SB.provider.GeoAddress;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class AddressAdapter extends BaseAdapter implements Filterable {
    public static final String TAG = "my.b1701.SB.Adapter.AddressAdapter";

    Uri uri = Uri.parse("content://" + CustomSuggestionProvider.AUTHORITY + "/" + SearchManager.SUGGEST_URI_PATH_QUERY);

    private List<Address> mObjects;
    private int mResource;
    private int mDropDownResource;
    private int mFieldId = 0;
    private Context mContext;
    private CustomFilter mFilter;
    private LayoutInflater mInflater;

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param textViewResourceId The resource ID for a layout file containing a TextView to use when
     *                           instantiating views.
     */
    public AddressAdapter(Context context, int textViewResourceId) {
        init(context, textViewResourceId, 0);
    }

    /**
     * Constructor
     *
     * @param context            The current context.
     * @param resource           The resource ID for a layout file containing a layout to use when
     *                           instantiating views.
     * @param textViewResourceId The id of the TextView within the layout resource to be populated
     */
    public AddressAdapter(Context context, int resource, int textViewResourceId) {
        init(context, resource, textViewResourceId);
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    private void init(Context context, int resource, int textViewResourceId) {
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mResource = mDropDownResource = resource;
        try {
            mObjects = getResults(new String[]{null});
        } catch (JSONException e) {
            e.printStackTrace();
            mObjects = new ArrayList<Address>();
        }
        mFieldId = textViewResourceId;
    }

    public ArrayList<Address> getResults(String[] selectionsArgs) throws JSONException {
        Cursor cursor = getContext().getContentResolver().query(uri, null, null, selectionsArgs, "");
        ArrayList<Address> list = new ArrayList<Address>();
        if (cursor.moveToFirst()) {
            do {
                String addressStr = cursor.getString(6);
                boolean isSaved = "true".equals(cursor.getString(7));
                Address address = new Address(addressStr, isSaved);
                list.add(address);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

    public Context getContext() {
        return mContext;
    }

    public int getCount() {
        return mObjects == null ? 0 : mObjects.size();
    }

    public String getItem(int position) {
        return mObjects.get(position).getGeoAddress().toString();
    }

    public Address getAddress(int position) {
        return mObjects.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mResource);
    }

    private View createViewFromResource(int position, View convertView, ViewGroup parent,
                                        int resource) {
        View view;
        TextView text;

        if (convertView == null) {
            view = mInflater.inflate(resource, parent, false);
        } else {
            view = convertView;
        }

        try {
            if (mFieldId == 0) {
                //  If no custom field is assigned, assume the whole resource is a TextView
                text = (TextView) view;
            } else {
                //  Otherwise, find the TextView field within the layout
                text = (TextView) view.findViewById(mFieldId);
            }
        } catch (ClassCastException e) {
            Log.e("ArrayAdapter", "You must supply a resource ID for a TextView");
            throw new IllegalStateException(
                    "ArrayAdapter requires the resource ID to be a TextView", e);
        }

        String item = getItem(position);
        text.setText(item);
        return view;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return createViewFromResource(position, convertView, parent, mDropDownResource);
    }

    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new CustomFilter();
        }
        return mFilter;
    }

    private class CustomFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            FilterResults results = new FilterResults();

            String[] selectionsArgs = prefix == null ? new String[] {null} : new String[]{prefix.toString()};
            ArrayList<Address> list;
            try {
                list = getResults(selectionsArgs);
            } catch (JSONException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                list = new ArrayList<Address>();
            }
            results.values = list;
            results.count = list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            //noinspection unchecked
            mObjects = (List<Address>) results.values;
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }

    public static class Address {
        private GeoAddress geoAddress;
        private boolean isSaved;

        public Address(String geoAddressStr, boolean isSaved) throws JSONException {
            Log.i(TAG, geoAddressStr);
            geoAddress = new GeoAddress(geoAddressStr);
            this.isSaved = isSaved;
        }

        public GeoAddress getGeoAddress(){
            return geoAddress;
        }

        public boolean isSaved(){
            return isSaved;
        }
    }
}
