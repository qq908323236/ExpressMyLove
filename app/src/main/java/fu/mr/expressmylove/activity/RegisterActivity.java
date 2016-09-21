package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import fu.mr.expressmylove.R;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final int VERIFICATION_CODE = 1;

    private TextView tv_2;
    private ImageView iv_back;
    private TextView tv_login;
    private EditText et_phone;
    private ImageView iv_delete;
    private Button btn_sendVerificationCode;
    private EventHandler eventHandler;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case VERIFICATION_CODE:
                    int event = msg.arg1;
                    int result = msg.arg2;
                    Object data = msg.obj;
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        //回调完成
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交验证码成功
                            Toast.makeText(RegisterActivity.this, "提交验证码成功", Toast.LENGTH_SHORT).show();
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            //获取验证码成功
                            Toast.makeText(RegisterActivity.this, "获取验证码成功", Toast.LENGTH_SHORT).show();
                        } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {

                        }
                    } else {
                        ((Throwable) data).printStackTrace();
                    }
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        initListener();
        initData();

    }

    private void initUI() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_login = (TextView) findViewById(R.id.tv_login);
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        btn_sendVerificationCode = (Button) findViewById(R.id.btn_sendVerificationCode);
        tv_2 = (TextView) findViewById(R.id.tv_2);
        tv_2.append(Html.fromHtml("<a href=\"" + "http://www.baidu.com" + "\">" + "《服务协议》" + "</a> "));

    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        btn_sendVerificationCode.setOnClickListener(this);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文字变化时的回调
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initData() {
        //初始化短信验证SDK
        SMSSDK.initSDK(this, "1" +
                "7341c343c280", "c672c7cb35bc4c67da7fa65a50fa8cb7");

        //回调监听
        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                Message msg = handler.obtainMessage();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;
                msg.what = VERIFICATION_CODE;
                handler.sendMessage(msg);
            }
        };

        //注册监听器
        SMSSDK.registerEventHandler(eventHandler);
        //获取支持国家列表
        SMSSDK.getSupportedCountries();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_login:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.iv_delete:
                et_phone.setText("");
                break;
            case R.id.btn_sendVerificationCode:
                System.out.println("发送验证码");
                String phone = et_phone.getText().toString();
                if (phone.matches("^1[3-8]\\d{9}$")){
                    System.out.println("手机号正确");
                } else {
                    System.out.println("手机号错误");
                }
                break;
        }
    }

}
