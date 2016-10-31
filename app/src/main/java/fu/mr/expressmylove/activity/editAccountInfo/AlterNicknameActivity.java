package fu.mr.expressmylove.activity.editAccountInfo;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;
import fu.mr.expressmylove.utils.Utils;
import fu.mr.expressmylove.view.CustomProgressDialog;

public class AlterNicknameActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private ImageView iv_back;
    private TextView tv_submit;
    private EditText et_nickName;
    private ImageView iv_delete;
    private String old_nickname;
    private User user;
    private MyApplication application;

    private static final int RESULT_CODE_NICKNAME = 1;
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
                case Utils.PROGRESSDIALOG_DISMISS_CODE:
                    //取消请求
                    if (cancelable != null) {
                        cancelable.cancel();
                    }
                    break;
            }
        }
    };

    private void initUI() {
        setContentView(R.layout.activity_alter_nickname);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_submit = (TextView) findViewById(R.id.tv_submit);
        et_nickName = (EditText) findViewById(R.id.et_nickName);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_submit.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        et_nickName.addTextChangedListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        user = application.getUser();
        old_nickname = user.getNickname();
        et_nickName.setText(old_nickname);
        et_nickName.setSelection(et_nickName.getText().length());//设置editText的光标位置为最后
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_submit:
                alterNickName();
                break;
            case R.id.iv_delete:
                et_nickName.setText("");
                break;
        }
    }

    private void alterNickName(){
        final CustomProgressDialog progressDialog = Utils.showCircleProgressDialog(this, handler);
        final String nickname = et_nickName.getText().toString();
        RequestParams params = new RequestParams(Constans.URL_UPDATE_USER_INFO);
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("what", Constans.UPDATE_USER_INFO_WHAT_NICKNAME);
        params.addBodyParameter("nickname", nickname);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                if (result.equals("0")) {
                    Toast.makeText(application, "失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(application, "成功", Toast.LENGTH_SHORT).show();
                    //保存缓存
                    SharedPreferencesUtils.saveString(AlterNicknameActivity.this, "nickname", nickname);
                    //刷新User
                    application.initUser();
                    Intent data = new Intent();
                    data.putExtra("nickname", nickname);
                    setResult(RESULT_CODE_NICKNAME, data);
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

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        setBtnSubmitEnable(!et_nickName.getText().toString().equals(old_nickname)
                && !TextUtils.isEmpty(et_nickName.getText()));
        if (TextUtils.isEmpty(et_nickName.getText())) {
            iv_delete.setVisibility(View.INVISIBLE);
        } else {
            iv_delete.setVisibility(View.VISIBLE);
        }

    }

    /**
     * 设置完成按钮可用不,true可用，false不可用
     *
     * @param b
     */
    private void setBtnSubmitEnable(boolean b) {
        if (b) {
            tv_submit.setTextColor(Color.parseColor("#33475f"));
            tv_submit.setClickable(true);
        } else {
            tv_submit.setTextColor(Color.parseColor("#55000000"));
            tv_submit.setClickable(false);
        }
    }
}
