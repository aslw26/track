package lta.amazoning.track;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static int ITEM_COUNT;
    private final LayoutInflater mInflater;
    private Context context;

    List<String> listDataGroup;
    List<String> indexGroup = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();

    ContentAdapter(Context context, List<String> listDataGroup) {
        this.context = context;
        this.listDataGroup = listDataGroup;
        try {
            this.ITEM_COUNT = listDataGroup.size();
            Log.i("ContentAdapter", listDataGroup.toString());
        }
        catch (NullPointerException ne) {
            this.ITEM_COUNT = 0;
        }
        Log.i("ContentAdapter", "Initialising Content Adapter");
        Log.i("ContentAdapter", String.valueOf(this.ITEM_COUNT));
        mInflater = LayoutInflater.from(context);
    }

    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        return new RecyclerView.ViewHolder(
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(
                mInflater.inflate(R.layout.view_holder_item, parent, false)) {};
    }

//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (ITEM_COUNT == 0) {
            listDataGroup.add("");
            indexGroup.add("empty");
            Log.i("ContentAdapter", "Empty List");
            // Adding child data
            holder.setContent(listDataGroup, indexGroup, listDataChild);
            return;
        }

        if (position == ITEM_COUNT - 1) {
            List<String> listDataGroup = new ArrayList<>();
            List<String> indexGroup = new ArrayList<>();
            HashMap<String, List<String>> listDataChild = new HashMap<>();
            listDataGroup.add("");
            indexGroup.add("e");

            // Adding child data
            holder.setContent(listDataGroup, indexGroup, listDataChild);
            return;
        }

        Log.i("ContentAdapter", "Binding ViewHolder");
        // array of strings
        String[] array;

        // list of alcohol
        List<String> alcoholList = new ArrayList<>();
        array = this.context.getResources().getStringArray(R.array.string_array_alcohol);
        for (String item : array) {
            alcoholList.add(item);
        }

        // Adding child data
        listDataChild.put("Alcohol", alcoholList);
        holder.setContent(listDataGroup, indexGroup, listDataChild);
    }

    @Override
    public int getItemCount() {
        try {
            return ITEM_COUNT;
        }
        catch (NullPointerException ne) {
            return 0;
        }
    }
}