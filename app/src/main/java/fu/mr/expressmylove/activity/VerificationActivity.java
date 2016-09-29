package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;

public class VerificationActivity extends AppCompatActivity implements View.OnClickListener{

    private final int VERIFICATION_CODE = 1;
    private final int TIMER_CODE = 2;

    private int SURPLUS_SECOND = 30;    //剩余时间 秒

    private EditText et_verificationCode;
    private Button btn_submit;
    private Button btn_sendVoiceVerifyCode;
    private ImageView iv_delete;
    private ImageView iv_back;

    private EventHandler eventHandler;

    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private Handler handler = new Handler(){
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
                            Toast.makeText(VerificationActivity.this, "验证码正确", Toast.LENGTH_SHORT).show();
                            //进入填写昵称,密码和头像的界面
                            Intent intent = new Intent(VerificationActivity.this, InputInfoActivity.class);
                            intent.putExtra("phone", phone);
                            startActivity(intent);
                            finish();
                        }
                    } else {
                        String message = ((Throwable) data).getMessage();
                        try {
                            JSONObject jsonObject = new JSONObject(message);
                            String status = jsonObject.getString("status");
                            if (status.equals("468")){
                                Toast.makeText(VerificationActivity.this, "验证码错误", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        ((Throwable) data).printStackTrace();
                    }
                    break;
                case TIMER_CODE:
                    SURPLUS_SECOND--;
                    if (SURPLUS_SECOND == 0){
                        btn_sendVoiceVerifyCode.setText("发送语音验证码");
                        btn_sendVoiceVerifyCode.setEnabled(true);
                        btn_sendVoiceVerifyCode.setClickable(true);
                    }else{
                        btn_sendVoiceVerifyCode.setText("已发送" + SURPLUS_SECOND + "秒");
                        handler.sendEmptyMessageDelayed(TIMER_CODE,1000);
                    }
                    break;
            }
        }
    };


    private void initUI() {
        setContentView(R.layout.activity_verification);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_verificationCode = (EditText) findViewById(R.id.et_verificationCode);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.INVISIBLE);
        btn_submit = (Button) findViewById(R.id.btn_submit);
        btn_sendVoiceVerifyCode = (Button) findViewById(R.id.btn_sendVoiceVerifyCode);

    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        btn_submit.setOnClickListener(this);
        btn_submit.setClickable(false);
        btn_sendVoiceVerifyCode.setOnClickListener(this);
        btn_sendVoiceVerifyCode.setClickable(false);
        et_verificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.INVISIBLE);
                }
                btn_submit.setEnabled(s.length() == 4);
                btn_submit.setClickable(s.length() == 4);
            }
        });
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");

        //初始化短信验证SDK
        SMSSDK.initSDK(this, "17341c343c280", "c672c7cb35bc4c67da7fa65a50fa8cb7");

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

        //发送验证码
//        SMSSDK.getVerificationCode("86", phone);   //方便测试，暂时先不发,到时后要解开

        //计时器
        handler.sendEmptyMessageDelayed(TIMER_CODE,1000);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_delete:
                et_verificationCode.setText("");
                break;
            case R.id.btn_submit:
//                SMSSDK.submitVerificationCode("86", phone,et_verificationCode.getText().toString().trim());
                //这4行方便测试，以后要删，还要解开上面这一行
                Intent intent = new Intent(this, InputInfoActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_sendVoiceVerifyCode:
//                SMSSDK.getVoiceVerifyCode("86",phone);
                Toast.makeText(this, "发送了语音验证码,请注意接听", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}
