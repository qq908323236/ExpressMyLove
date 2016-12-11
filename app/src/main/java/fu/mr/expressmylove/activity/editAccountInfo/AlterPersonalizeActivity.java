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

public class AlterPersonalizeActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{

    private ImageView iv_back;
    private TextView tv_save;
    private EditText et_personalize;
    private ImageView iv_delete;
    private TextView tv_count;
    private MyApplication application;
    private User user;
    private String old_personalize;

    private static final int RESULT_CODE_PERSONALIZE = 3;
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
        setContentView(R.layout.activity_alter_personalize);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_save = (TextView) findViewById(R.id.tv_save);
        et_personalize = (EditText) findViewById(R.id.et_personalize);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        tv_count = (TextView) findViewById(R.id.tv_count);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        et_personalize.addTextChangedListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        user = application.getUser();
        old_personalize = user.getPersonalize();
        et_personalize.setText(old_personalize);
        et_personalize.setSelection(et_personalize.getText().length());
        tv_count.setText(et_personalize.getText().length() + "/32");
        if (TextUtils.isEmpty(et_personalize.getText())) {
            iv_delete.setVisibility(View.INVISIBLE);
        } else {
            iv_delete.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                alterPersonalize();
                break;
            case R.id.iv_delete:
                et_personalize.setText("");
                break;
        }
    }

    private void alterPersonalize(){
        final CustomProgressDialog progressDialog = Utils.showCircleProgressDialog(this, handler);
        final String personalize = et_personalize.getText().toString();
        RequestParams params = new RequestParams(Constans.URL_UPDATE_USER_INFO);
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("what", Constans.UPDATE_USER_INFO_WHAT_PERSONALIZE);
        params.addBodyParameter("personalize", personalize);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                if (result.equals("0")) {
                    Toast.makeText(application, "失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(application, "成功", Toast.LENGTH_SHORT).show();
                    //保存缓存
                    SharedPreferencesUtils.saveString(AlterPersonalizeActivity.this, "personalize", personalize);
                    //刷新User
                    application.initUser();
                    Intent data = new Intent();
                    data.putExtra("personalize", personalize);
                    setResult(RESULT_CODE_PERSONALIZE, data);
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
        setBtnSaveEnable(!et_personalize.getText().toString().equals(old_personalize));
        if (TextUtils.isEmpty(et_personalize.getText())) {
            iv_delete.setVisibility(View.INVISIBLE);
        } else {
            iv_delete.setVisibility(View.VISIBLE);
        }
        tv_count.setText(s.length() + "/32");
    }

    /**
     * 设置保存按钮可用不,true可用，false不可用
     *
     * @param b
     */
    private void setBtnSaveEnable(boolean b) {
        if (b) {
            tv_save.setTextColor(Color.parseColor("#33475f"));
            tv_save.setClickable(true);
        } else {
            tv_save.setTextColor(Color.parseColor("#55000000"));
            tv_save.setClickable(false);
        }
    }

}
