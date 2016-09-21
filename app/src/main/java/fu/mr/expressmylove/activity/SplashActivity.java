package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

import fu.mr.expressmylove.R;

public class SplashActivity extends AppCompatActivity {


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            enterSelect();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Thread(){
            @Override
            public void run() {
                SystemClock.sleep(2000);
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    /***
     * 进入登陆或注册界面
     */
    private void enterSelect() {
        Intent intent = new Intent(this, SelectActivity.class);
        startActivity(intent);
        finish();
    }

}
