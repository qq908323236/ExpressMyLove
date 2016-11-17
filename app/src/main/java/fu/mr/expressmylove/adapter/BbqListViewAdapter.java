package fu.mr.expressmylove.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import fu.mr.expressmylove.R;

/**
 * Created by Fu on 2016/11/10 10:57.
 */

public class BbqListViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;

    public BbqListViewAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_listview_bbq, null);
        return view;
    }
}
