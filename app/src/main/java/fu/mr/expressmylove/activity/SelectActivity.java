package fu.mr.expressmylove.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;

public class SelectActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();

    }

    private void initUI() {
        setContentView(R.layout.activity_select);
    }

    private void initListener() {

    }

    private void initData() {
        MyApplication._instance.addActivity(this);
    }

    public void login(View view){
        startActivity(new Intent(this,LoginActivity.class));
    }

    public void register(View view){
        startActivity(new Intent(this,RegisterActivity.class));
    }
}
