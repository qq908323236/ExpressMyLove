package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.edmodo.cropper.cropwindow.handle.Handle;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;

public class SplashActivity extends AppCompatActivity {

    private static final int CODE_ENTER_HOME = 1;
    private static final int CODE_ENTER_SELECT = 2;

    private static final int ENTER_TIME = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initData();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case CODE_ENTER_HOME:
                    enterHome();
                    break;
                case CODE_ENTER_SELECT:
                    enterSelect();
                    break;
            }
        }
    };

    private void initData() {
        final long startTime = System.currentTimeMillis();

        new Thread(){
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                if (SharedPreferencesUtils.getBoolean(SplashActivity.this, "isAutoLogin", false)) {
                    //有缓存,自动登陆进来的,从缓存中初始化用户信息,然后直接进入主页
                    initUserInfo();
                    message.what = CODE_ENTER_HOME;
                } else {
                    //进入登陆注册界面
                    message.what = CODE_ENTER_SELECT;
                }



                long endTime = System.currentTimeMillis();

                long timeUsed = endTime - startTime;  //初始化花费的时间

                if (timeUsed < ENTER_TIME) {
                    //强制休眠一段时间，保证闪屏页展示2秒中
                    SystemClock.sleep(ENTER_TIME - timeUsed);
                }
                handler.sendMessage(message);
            }
        }.start();

    }

    /**
     * 从缓存中获取信息给赋值给User
     */
    private void initUserInfo() {
        MyApplication application = (MyApplication) getApplication();
        application.initUser();
    }


    /***
     * 进入Select界面
     */
    private void enterSelect() {
        startActivity(new Intent(this, SelectActivity.class));
        finish();
    }

    /**
     * 进入主页
     */
    private void enterHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

}
