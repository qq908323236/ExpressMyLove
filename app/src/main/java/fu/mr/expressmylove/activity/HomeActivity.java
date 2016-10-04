package fu.mr.expressmylove.activity;

import android.app.Application;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.widget.TabHost;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.fragment.BbqFragment;
import fu.mr.expressmylove.fragment.MeFragment;
import fu.mr.expressmylove.fragment.MsgFragment;
import fu.mr.expressmylove.fragment.SsnFragment;
import fu.mr.expressmylove.view.TabIndicatorView;

public class HomeActivity extends FragmentActivity implements TabHost.OnTabChangeListener{
    private static final String TAG_ME = "me";
    private static final String TAG_MSG = "msg";
    private static final String TAG_SSN = "ssn";
    private static final String TAG_BBQ = "bbq";

    private FragmentTabHost tabhost;
    private TabIndicatorView bbqIndicatorView;
    private TabIndicatorView ssnIndicatorView;
    private TabIndicatorView msgIndicatorView;
    private TabIndicatorView meIndicatorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_home);
        //1.初始化tabhost
        tabhost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);

        //新建Tabsepc
        TabHost.TabSpec spec = tabhost.newTabSpec(TAG_BBQ);
        bbqIndicatorView = new TabIndicatorView(this);
        bbqIndicatorView.setTabTitle("表白墙");
        bbqIndicatorView.setTabIcon(R.mipmap.tab_bbq_normal,R.mipmap.tab_bbq_focus);
        spec.setIndicator(bbqIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, BbqFragment.class, null);

        //新建Tabsepc
        spec = tabhost.newTabSpec(TAG_SSN);
        ssnIndicatorView = new TabIndicatorView(this);
        ssnIndicatorView.setTabTitle("碎碎念");
        ssnIndicatorView.setTabIcon(R.mipmap.tab_ssn_normal,R.mipmap.tab_ssn_focus);
        spec.setIndicator(ssnIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, SsnFragment.class, null);

        //新建Tabsepc
        spec = tabhost.newTabSpec(TAG_MSG);
        msgIndicatorView = new TabIndicatorView(this);
        msgIndicatorView.setTabTitle("消息");
        msgIndicatorView.setTabIcon(R.mipmap.tab_msg_normal,R.mipmap.tab_msg_focus);
        spec.setIndicator(msgIndicatorView);
        //添加TabSpec
        tabhost.addTab(spec, MsgFragment.class, null);

        //新建Tabsepc
        spec = tabhost.newTabSpec(TAG_ME);
        meIndicatorView = new TabIndicatorView(this);
        meIndicatorView.setTabTitle("我");
        meIndicatorView.setTabIcon(R.mipmap.tab_me_normal,R.mipmap.tab_me_focus);
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
        String uid = getIntent().getStringExtra("uid");
        User user = application.getUser();
        user.setUid(uid);
    }

    @Override
    public void onTabChanged(String tabId) {
        bbqIndicatorView.setTableSelected(tabId.equals(TAG_BBQ));
        ssnIndicatorView.setTableSelected(tabId.equals(TAG_SSN));
        msgIndicatorView.setTableSelected(tabId.equals(TAG_MSG));
        meIndicatorView.setTableSelected(tabId.equals(TAG_ME));
    }


}
