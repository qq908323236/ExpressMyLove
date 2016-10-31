package fu.mr.expressmylove.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.activity.editAccountInfo.EditAccountInfoActivity;
import fu.mr.expressmylove.activity.setting.SettingActivity;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;
import fu.mr.expressmylove.utils.Utils;
import fu.mr.expressmylove.view.WaterDropListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment implements WaterDropListView.IWaterDropListViewListener {

    private View rootView;    //缓存Fragment View
    private WaterDropListView listview;
    private MyApplication application;
    private User user;

    private View headerView;
    private ImageView iv_avatar;
    private TextView tv_nickname;
    private ImageView iv_sex;
    private TextView tv_personalize;

    private TextView tv_setting;
    private Button btn_editInfo;

    private List<String> list;
    private MyListviewAdapter adapter;

    private int firstVisiblePosition; // listView第一个可见的item的位置，即在数据集合中的位置position
    private int firstVisiblePositionTop; // listView第一可见的item距离父布局的top
    private int dividerHeight;


    private static final int REQUEST_CODE_EDIT_INFO = 10;


    public MeFragment() {
        // Required empty public constructor
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    listview.stopRefresh();
                    break;
                case 2:
                    listview.stopLoadMore();
                    break;
                case 3:
                    listview.setPullLoadEnable(true);
//                    adapter.notifyDataSetChanged();
                    listview.setDividerHeight(dividerHeight);
                    listview.setAdapter(adapter);
                    break;
            }

        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        application = (MyApplication) getActivity().getApplication();
        user = application.getUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_me, container, false);
            initUI(rootView);
            initListener();
            initData();
        }

        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (list.isEmpty()) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                        list = getData();
                        handler.sendEmptyMessage(3);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onStart() {
        super.onStart();
        // 恢复现在listView的位置，(上一次保存的位置)
        listview.setSelectionFromTop(firstVisiblePosition, firstVisiblePositionTop);
    }

    @Override
    public void onStop() {
        super.onStop();
        saveListViewPositionAndTop();
    }

    private void initUI(View view) {
        tv_setting = (TextView) view.findViewById(R.id.tv_setting);
        initHeaderView(view);
        listview = (WaterDropListView) view.findViewById(R.id.listview);
        listview.addHeaderView(headerView);
        listview.setHeaderDividersEnabled(false);  //去掉header的分割线
        listview.setFooterDividersEnabled(false);   //去掉footer的分割线
        listview.setPullLoadEnable(false);
        list = new ArrayList<String>();
        adapter = new MyListviewAdapter();
        dividerHeight = listview.getDividerHeight();
        listview.setDividerHeight(0);
        listview.setAdapter(new ProgressBarAdapter());
    }

    private void initHeaderView(View view) {
        headerView = View.inflate(getActivity(), R.layout.header_me, null);
        iv_avatar = (ImageView) headerView.findViewById(R.id.iv_avatar);
        tv_nickname = (TextView) headerView.findViewById(R.id.tv_nickname);
        iv_sex = (ImageView) headerView.findViewById(R.id.iv_sex);
        //ID显示
        TextView tv_id = (TextView) headerView.findViewById(R.id.tv_id);
        tv_id.setText("ID:" + user.getUid());
        tv_personalize = (TextView) headerView.findViewById(R.id.tv_personalize);
        btn_editInfo = (Button) headerView.findViewById(R.id.btn_editInfo);
        showUserInfo();
    }

    private void showUserInfo() {
        //头像绑定
        x.image().bind(iv_avatar, Constans.URL_BASE + user.getAvatar(), Utils.CircleImageOptions);

        //昵称显示
        tv_nickname.setText(user.getNickname());

        //性别图片显示
        showSexImage(user.getSex());

        //个性签名显示
        tv_personalize.setText(user.getPersonalize());
//        tv_personalize.setText("个性签名");
    }

    private void initListener() {
        listview.setWaterDropListViewListener(this);
        //设置
        tv_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
            }
        });
        //编辑资料
        btn_editInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), EditAccountInfoActivity.class), REQUEST_CODE_EDIT_INFO);
            }
        });

    }

    private void initData() {


    }

    /**
     * 根据性别显示性别的图标
     *
     * @param sex
     */
    private void showSexImage(String sex) {
        switch (sex) {
            case "男":
                iv_sex.setImageResource(R.mipmap.icon_nan);
                break;
            case "女":
                iv_sex.setImageResource(R.mipmap.icon_nv);
                break;
            case "未知":
                iv_sex.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == REQUEST_CODE_EDIT_INFO && requestCode == resultCode) {
            Bundle bundle = data.getBundleExtra("bundle");
            boolean avatar = bundle.getBoolean("avatar", false);
            boolean nickname = bundle.getBoolean("nickname", false);
            boolean sex = bundle.getBoolean("sex", false);
            boolean personalize = bundle.getBoolean("personalize", false);
            if (avatar) {
                x.image().bind(iv_avatar, Constans.URL_BASE + user.getAvatar(), Utils.CircleImageOptions);
            }
            if (nickname) {
                tv_nickname.setText(user.getNickname());
            }
            if (sex) {
                showSexImage(user.getSex());
            }
            if (personalize){
                tv_personalize.setText(user.getPersonalize());
            }
        }
    }

    /**
     * 保存当前页签listView的第一个可见的位置和top
     */
    private void saveListViewPositionAndTop() {
        firstVisiblePosition = listview.getFirstVisiblePosition();
        View item = listview.getChildAt(0);
        firstVisiblePositionTop = (item == null) ? 0 : item.getTop();
    }


    public List<String> getData() {

        List<String> data = new ArrayList<String>();
        data.add("To see a world in a grain of sand,");
        data.add("And a heaven in a wild flower,");
        data.add("Hold infinity in the palm of your hand,");
        data.add("And eternity in an hour.");
        data.add("To see a world in a grain of sand,");
        data.add("And a heaven in a wild flower,");
        data.add("Hold infinity in the palm of your hand,");
        data.add("我喜欢你.");
        return data;
    }

    //当下拉刷新的时候
    @Override
    public void onRefresh() {
        RequestParams params = new RequestParams(Constans.URL_GET_USER_INFO);
        params.addBodyParameter("uid", user.getUid());
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                listview.stopRefresh();
                try {
                    String avatar = result.getString("avatar");
                    String nickname = result.getString("nickname");
                    String sex = result.getString("sex");
                    String personalize = result.getString("personalize");
                    String authentication = result.getString("authentication");

                    user.setAvatar(avatar);
                    user.setNickname(nickname);
                    user.setPersonalize(personalize);
                    user.setSex(sex);
                    user.setAuthentication(authentication);

                    showUserInfo(); //刷新ui

                    //缓存用户信息
                    SharedPreferencesUtils.saveString(getActivity(), "avatar", avatar);
                    SharedPreferencesUtils.saveString(getActivity(), "nickname", nickname);
                    SharedPreferencesUtils.saveString(getActivity(), "personalize", personalize);
                    SharedPreferencesUtils.saveString(getActivity(), "sex", sex);
                    SharedPreferencesUtils.saveString(getActivity(), "authentication", authentication);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * List上拉加载更多
     */
    @Override
    public void onLoadMore() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    handler.sendEmptyMessage(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private class ProgressBarAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 1;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(getActivity(), R.layout.item_progress, null);
            return view;
        }
    }

    private class MyListviewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (list.isEmpty()) {
                return 1;
            } else {
                return list.size();
            }
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (list.isEmpty()) {
                System.out.println("list为空");
                View view = View.inflate(getActivity(), R.layout.item_listview_me, null);
                TextView tv_textview = (TextView) view.findViewById(R.id.tv_textview);
                tv_textview.setText("加载数据中");
                return view;
            } else {
                ViewHolder holder;
                View view;
                if (convertView != null) {
                    view = convertView;
                    holder = (ViewHolder) view.getTag();
                } else {
                    holder = new ViewHolder();
                    view = View.inflate(getActivity(), R.layout.item_listview_me, null);
                    holder.tv_textview = (TextView) view.findViewById(R.id.tv_textview);

                    view.setTag(holder);
                }

                holder.tv_textview.setText(list.get(position));

                return view;
            }

        }
    }

    static class ViewHolder {
        TextView tv_textview;
    }
}
