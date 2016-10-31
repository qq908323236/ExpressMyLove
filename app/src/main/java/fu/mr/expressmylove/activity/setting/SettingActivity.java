package fu.mr.expressmylove.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.tencent.tauth.Tencent;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.activity.HomeActivity;
import fu.mr.expressmylove.activity.InputInfoActivity;
import fu.mr.expressmylove.activity.SelectActivity;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;

import static fu.mr.expressmylove.R.id.iv_back;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{

    private LinearLayout ll_logout;
    private ImageView iv_back;
    private NormalAlertDialog dialog;

    private Tencent mTencent;
    private MyApplication application;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_setting);
        ll_logout = (LinearLayout) findViewById(R.id.ll_logout);
        iv_back = (ImageView) findViewById(R.id.iv_back);
    }

    private void initListener() {
        ll_logout.setOnClickListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        mTencent = Tencent.createInstance(Constans.APP_ID_QQ, getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_logout:
                showIsLogOutDialog();
                break;
        }
    }

    /**
     * 显示是否退出登陆的对话框
     */
    private void showIsLogOutDialog(){
        dialog = new NormalAlertDialog.Builder(SettingActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(false)
                .setContentText("你确定要退出当前账号吗？")
                .setContentTextSize(16)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("确定")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        mTencent.logout(SettingActivity.this);
                        //退出登陆的话，就把自动登陆设为false
                        SharedPreferencesUtils.saveBoolean(SettingActivity.this, "isAutoLogin", false);
                        startActivity(new Intent(SettingActivity.this, SelectActivity.class));
                        application.deleteActivityList();
                        finish();
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();
    }
}
