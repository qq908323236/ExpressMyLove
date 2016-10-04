package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    private final int PROGRESSDIALOG_DISMISS_CODE = 2;

    private ImageView iv_back;
    private EditText et_phone;
    private ImageView iv_delete;
    private Button btn_sendVerificationCode;

    private NormalAlertDialog dialog;

    private CustomProgressDialog progressDialog;

    private Callback.Cancelable cancelable;
    private String phone;

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
        setContentView(R.layout.activity_forget_password);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        iv_delete.setVisibility(View.INVISIBLE);
        btn_sendVerificationCode = (Button) findViewById(R.id.btn_sendVerificationCode);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        btn_sendVerificationCode.setOnClickListener(this);
        btn_sendVerificationCode.setClickable(false);
        et_phone.addTextChangedListener(new TextWatcher() {
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
                //改变背景颜色
                btn_sendVerificationCode.setEnabled(s.length() == 11);
                //按钮是否能被点击
                btn_sendVerificationCode.setClickable(s.length() == 11);
            }
        });
    }

    private void initData() {
        ((MyApplication) getApplication()).addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_delete:
                et_phone.setText("");
                break;
            case R.id.btn_sendVerificationCode:
                phone = et_phone.getText().toString().trim();
                if (phone.matches("^1[3-8]\\d{9}$")){
                    verificationIsRegister(phone);
                } else {
                    Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
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
                    // 0就是该手机没有注册过,要先注册才能忘记密码
                    showNoRegisterDialog();
                } else {
                    Intent intent = new Intent(ForgetPasswordActivity.this,VerificationActivity.class);
                    intent.putExtra("phone", phone);
                    intent.putExtra("what", Constans.WHAT_FORGETPASSWORD);  //通过what判断为什么需要验证
                    startActivity(intent);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(ForgetPasswordActivity.this, "连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
     * 显示该手机号未注册的dialog
     */
    private void showNoRegisterDialog(){
        dialog = new NormalAlertDialog.Builder(ForgetPasswordActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(false)
                .setContentText("该手机号还未注册")
                .setContentTextSize(16)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("去注册")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        startActivity(new Intent(ForgetPasswordActivity.this, RegisterActivity.class));
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
