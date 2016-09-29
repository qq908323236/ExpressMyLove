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
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.view.CustomProgressDialog;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private final int PROGRESSDIALOG_DISMISS_CODE = 2;

    private ImageView iv_back;
    private TextView tv_login;
    private EditText et_phone;
    private ImageView iv_delete;
    private TextView tv_agreement;
    private Button btn_sendVerificationCode;
    private NormalAlertDialog dialog;
    private CustomProgressDialog progressDialog;

    private Callback.Cancelable cancelable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESSDIALOG_DISMISS_CODE:
                    //取消请求
                    cancelable.cancel();
                    break;
            }
        }
    };


    private void initUI() {
        setContentView(R.layout.activity_register);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_login = (TextView) findViewById(R.id.tv_login);
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.INVISIBLE);    //一进来是看不到的
        btn_sendVerificationCode = (Button) findViewById(R.id.btn_sendVerificationCode);
        tv_agreement = (TextView) findViewById(R.id.tv_agreement);

    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        btn_sendVerificationCode.setOnClickListener(this);
        btn_sendVerificationCode.setClickable(false);     //这个要放在设置监听接口后面，因为设置了监听接口会自动把Clickable置成true
        tv_agreement.setOnClickListener(this);
        et_phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //文字变化时的回调
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            //文字变化结束后回调
            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)) {
                    iv_delete.setVisibility(View.VISIBLE);
                } else {
                    iv_delete.setVisibility(View.INVISIBLE);
                }
                //改变背景颜色
                btn_sendVerificationCode.setEnabled(s.length() == 11);
                //按钮是否能被点击
                btn_sendVerificationCode.setClickable(s.length() == 11);
            }
        });
    }

    private void initData() {
        MyApplication._instance.addActivity(this);
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
            case R.id.tv_agreement:
                Toast.makeText(this, "跳到web界面", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this,WebActivity.class));
                break;
            case R.id.iv_delete:
                et_phone.setText("");   //清除edittext中的内容
                break;
            case R.id.btn_sendVerificationCode:
                String phone = et_phone.getText().toString().trim();
                if (phone.matches("^1[3-8]\\d{9}$")) {   //匹配是否是手机号
                    verificationIsRegister(phone);  //验证手机号注册过没有
                } else {
                    Toast.makeText(RegisterActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 验证这个手机号是否已经注册
     *
     * @param phone 手机号
     * @return true:已经注册过了,  false:没有
     */
    private void verificationIsRegister(final String phone) {
        //这里要从服务器请求数据
        showProgressBar();
        RequestParams params = new RequestParams(Constans.URL_CHECK_PHONE);
        params.addBodyParameter("phone", phone);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                if (result.equals("0")) {
                    // 0就是该手机没有注册过

                    Intent intent = new Intent(RegisterActivity.this,VerificationActivity.class);
                    intent.putExtra("phone", phone);
                    startActivity(intent);
                } else {
                    showHadRegisterDialog();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(RegisterActivity.this, "连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
     * 显示已经注册了的dialog
     */
    private void showHadRegisterDialog() {
        dialog = new NormalAlertDialog.Builder(RegisterActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(false)
                .setContentText("该手机号已经注册过了")
                .setContentTextSize(16)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("登陆")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                        dialog.dismiss();
                    }
                })
                .build();
        dialog.show();
    }

    /**
     * 圆形进度条
     */
    private void showProgressBar() {
        progressDialog = CustomProgressDialog.createDialog(this);
        Message msg = handler.obtainMessage();
        msg.what = PROGRESSDIALOG_DISMISS_CODE;
        progressDialog.setDismissMessage(msg);
        progressDialog.show();
    }

}
