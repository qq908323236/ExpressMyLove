package fu.mr.expressmylove.adapter;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.x;

import java.util.List;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.Utils;

import static android.R.attr.data;

/**
 * Created by Fu on 2016/11/10 10:57.
 */

public class BbqListViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> data;
    private final MyApplication application;
    private final User user;

    public BbqListViewAdapter(Activity context, List<String> data) {
        this.context = context;
        this.data = data;

        application = (MyApplication) context.getApplication();
        user = application.getUser();
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
        ImageView iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        x.image().bind(iv_avatar, Constans.URL_BASE + user.getAvatar(), Utils.CircleImageOptions);
        return view;
    }
}
