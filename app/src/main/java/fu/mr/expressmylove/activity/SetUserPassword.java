package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
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
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.view.CustomProgressDialog;

import static fu.mr.expressmylove.R.id.et_password;
import static fu.mr.expressmylove.R.id.iv_lookpassword;

public class SetUserPassword extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_back;
    private EditText et_password;
    private ImageView iv_lookpassword;
    private ImageView iv_delete;
    private Button btn_submit;

    private CustomProgressDialog progressDialog;

    private boolean pwdIsShow = false;    //密码是否显示 false不显示，true显示
    private String phone;

    private NormalAlertDialog dialog;
    private boolean isGiveUp = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_set_user_password);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        iv_lookpassword = (ImageView) findViewById(R.id.iv_lookpassword);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        btn_submit = (Button) findViewById(R.id.btn_submit);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        iv_lookpassword.setOnClickListener(this);
        iv_lookpassword.setVisibility(View.INVISIBLE);
        iv_delete.setOnClickListener(this);
        iv_delete.setVisibility(View.INVISIBLE);
        btn_submit.setOnClickListener(this);
        btn_submit.setClickable(false);
        et_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s)){
                    iv_lookpassword.setVisibility(View.VISIBLE);
                    iv_delete.setVisibility(View.VISIBLE);
                }else{
                    iv_lookpassword.setVisibility(View.INVISIBLE);
                    iv_delete.setVisibility(View.INVISIBLE);
                }

                btn_submit.setEnabled(s.length() >= 6);
                btn_submit.setClickable(s.length() >= 6);
            }
        });
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.iv_lookpassword:
                if (pwdIsShow) {
                    //隐藏密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password.setSelection(et_password.getText().length());
                    iv_lookpassword.setImageResource(R.mipmap.eye2);
                } else {
                    //显示密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password.setSelection(et_password.getText().length());
                    iv_lookpassword.setImageResource(R.mipmap.eye1);
                }
                pwdIsShow = !pwdIsShow;
                break;
            case R.id.iv_delete:
                et_password.setText("");
                break;
            case R.id.btn_submit:
                showProgressBar();
                setPassword();
                break;
        }
    }

    private void setPassword(){
        RequestParams params = new RequestParams(Constans.URL_SET_PASSWORD);
        params.addBodyParameter("phone",phone);
        params.addBodyParameter("upassword",et_password.getText().toString().trim());
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                if (result.equals("1")){
                    Toast.makeText(SetUserPassword.this, "设置成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SetUserPassword.this,LoginActivity.class));
                    finish();
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

    /**
     * 显示是否放弃修改密码的dialog
     */
    private void showIsGiveUpDialog() {
        dialog = new NormalAlertDialog.Builder(SetUserPassword.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(false)
                .setContentText("你确定要放弃修改密码吗？")
                .setContentTextSize(16)
                .setContentTextColor(R.color.black_light)
                .setLeftButtonText("取消")
                .setLeftButtonTextColor(R.color.black_light)
                .setRightButtonText("放弃")
                .setRightButtonTextColor(R.color.black_light)
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickLeftButton(View view) {
                        dialog.dismiss();
                    }

                    @Override
                    public void clickRightButton(View view) {
                        isGiveUp = true;
                        dialog.dismiss();
                        onBackPressed();
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
        progressDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isGiveUp == true) {
            super.onBackPressed();
        } else {
            showIsGiveUpDialog();
        }
    }
}
