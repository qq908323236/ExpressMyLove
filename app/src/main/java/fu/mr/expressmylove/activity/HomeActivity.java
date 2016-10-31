package fu.mr.expressmylove.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TabHost;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.fragment.BbqFragment;
import fu.mr.expressmylove.fragment.MeFragment;
import fu.mr.expressmylove.fragment.MsgFragment;
import fu.mr.expressmylove.fragment.SsnFragment;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;
import fu.mr.expressmylove.view.TabIndicatorView;

public class HomeActivity extends FragmentActivity implements TabHost.OnTabChangeListener {
    private static final String TAG_ME = "me";
    private static final String TAG_MSG = "msg";
    private static final String TAG_SSN = "ssn";
    private static final String TAG_BBQ = "bbq";

    private FragmentTabHost tabhost;
    private TabIndicatorView bbqIndicatorView;
    private TabIndicatorView ssnIndicatorView;
    private TabIndicatorView msgIndicatorView;
    private TabIndicatorView meIndicatorView;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        smoothSwitchScreen();
        setContentView(R.layout.activity_home);
        //1.初始化tabhost
        tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //新建Tabsepc
        TabHost.TabSpec spec = tabhost.newTabSpec(TAG_BBQ);
        bbqIndicatorView = new TabIndicatorView(this);
        bbqIndicatorView.setTabTitle("表白墙");
        bbqIndicatorView.setTabIcon(R.mipmap.tab_bbq_normal, R.mipmap.tab_bbq_focus);
        spec.setIndicator(bbqIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, BbqFragment.class, null);

        //新建Tabsepc
        spec = tabhost.newTabSpec(TAG_SSN);
        ssnIndicatorView = new TabIndicatorView(this);
        ssnIndicatorView.setTabTitle("碎碎念");
        ssnIndicatorView.setTabIcon(R.mipmap.tab_ssn_normal, R.mipmap.tab_ssn_focus);
        spec.setIndicator(ssnIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, SsnFragment.class, null);

        //新建Tabsepc
        spec = tabhost.newTabSpec(TAG_MSG);
        msgIndicatorView = new TabIndicatorView(this);
        msgIndicatorView.setTabTitle("消息");
        msgIndicatorView.setTabIcon(R.mipmap.tab_msg_normal, R.mipmap.tab_msg_focus);
        spec.setIndicator(msgIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, MsgFragment.class, null);

        //新建Tabsepc
        spec = tabhost.newTabSpec(TAG_ME);
        meIndicatorView = new TabIndicatorView(this);
        meIndicatorView.setTabTitle("我");
        meIndicatorView.setTabIcon(R.mipmap.tab_me_normal, R.mipmap.tab_me_focus);
        spec.setIndicator(meIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, MeFragment.class, null);

        //去掉分割线
        tabhost.getTabWidget().setDividerDrawable(null);

        //设置当前选择的tab
        tabhost.setCurrentTabByTag(TAG_BBQ);
        bbqIndicatorView.setTableSelected(true);

        //监听tabhost的选中事件
        tabhost.setOnTabChangedListener(this);

    }

    private void initListener() {

    }

    private void initData() {
        //初始化用户信息
        MyApplication application = (MyApplication) getApplication();
        application.addActivity(this);
        String uid = getIntent().getStringExtra("uid");  //这个UID只有注册或手动登陆进来才会用到
        user = application.getUser();
        if (!SharedPreferencesUtils.getBoolean(HomeActivity.this, "isAutoLogin", false)) {
            //如果没缓存,手动登陆进来的,就要初始化用户信息，并缓存,否则则不用
            initUserInfo(uid);
        }
        System.out.println(user.getUid());
        System.out.println(user.getAvatar());
        System.out.println(user.getNickname());
        System.out.println(user.getSex());
        System.out.println(user.getAuthentication());
    }

    /**
     * 从服务器获取用户信息并缓存起来
     *
     * @param uid
     */
    private void initUserInfo(final String uid) {
        RequestParams params = new RequestParams(Constans.URL_GET_USER_INFO);
        params.addBodyParameter("uid", uid);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                 try {
                    String avatar = result.getString("avatar");
                    String nickname = result.getString("nickname");
                    String sex = result.getString("sex");
                    String personalize = result.getString("personalize");
                    String authentication = result.getString("authentication");
                    user.setUid(uid);
                    user.setAvatar(avatar);
                    user.setNickname(nickname);
                    user.setPersonalize(personalize);
                    user.setSex(sex);
                    user.setAuthentication(authentication);

                    //登陆过，就设为自动登陆
                    SharedPreferencesUtils.saveBoolean(HomeActivity.this, "isAutoLogin", true);
                    //缓存用户信息
                    SharedPreferencesUtils.saveString(HomeActivity.this, "uid", uid);
                    SharedPreferencesUtils.saveString(HomeActivity.this, "avatar", avatar);
                    SharedPreferencesUtils.saveString(HomeActivity.this, "nickname", nickname);
                    SharedPreferencesUtils.saveString(HomeActivity.this, "personalize", personalize);
                    SharedPreferencesUtils.saveString(HomeActivity.this, "sex", sex);
                    SharedPreferencesUtils.saveString(HomeActivity.this, "authentication", authentication);
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

    @Override
    public void onTabChanged(String tabId) {
        bbqIndicatorView.setTableSelected(tabId.equals(TAG_BBQ));
        ssnIndicatorView.setTableSelected(tabId.equals(TAG_SSN));
        msgIndicatorView.setTableSelected(tabId.equals(TAG_MSG));
        meIndicatorView.setTableSelected(tabId.equals(TAG_ME));
    }


    /**
     * 解决全屏到非全屏的状态栏显示问题
     */
    private void smoothSwitchScreen() {
        // 5.0以上修复了此bug
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup rootView = ((ViewGroup) this.findViewById(android.R.id.content));
            int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            rootView.setPadding(0, statusBarHeight, 0, 0);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        }
    }
}
