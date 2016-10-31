package fu.mr.expressmylove.activity.editAccountInfo;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class AlterSexActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView iv_back;
    private LinearLayout ll_female;
    private LinearLayout ll_male;
    private MyApplication application;
    private User user;
    private ImageView iv_checked1;
    private ImageView iv_checked2;

    private static final int RESULT_CODE_SEX = 2;

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
        setContentView(R.layout.activity_alter_sex);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_female = (LinearLayout) findViewById(R.id.ll_female);
        iv_checked1 = (ImageView) findViewById(R.id.iv_checked1);
        ll_male = (LinearLayout) findViewById(R.id.ll_male);
        iv_checked2 = (ImageView) findViewById(R.id.iv_checked2);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        ll_female.setOnClickListener(this);
        ll_male.setOnClickListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        user = application.getUser();
        String sex = user.getSex();
        switch (sex){
            case "女":
                iv_checked2.setVisibility(View.INVISIBLE);
                break;
            case "男":
                iv_checked1.setVisibility(View.INVISIBLE);
                break;
            default:
                iv_checked1.setVisibility(View.INVISIBLE);
                iv_checked2.setVisibility(View.INVISIBLE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_female:
                if (iv_checked1.getVisibility() == View.INVISIBLE) {
                    alterSex("女");
                }else{
                    onBackPressed();
                }
                break;
            case R.id.ll_male:
                if (iv_checked2.getVisibility() == View.INVISIBLE) {
                    alterSex("男");
                }else{
                    onBackPressed();
                }
                break;
        }
    }

    private void alterSex(final String sex){
        final CustomProgressDialog progressDialog = Utils.showCircleProgressDialog(this, handler);
        RequestParams params = new RequestParams(Constans.URL_UPDATE_USER_INFO);
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("what", Constans.UPDATE_USER_INFO_WHAT_SEX);
        params.addBodyParameter("sex", sex);
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                System.out.println(result);
                if (result.equals("0")) {
                    Toast.makeText(application, "失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(application, "修改成功", Toast.LENGTH_SHORT).show();
                    switch (sex) {
                        case "女":
                            iv_checked1.setVisibility(View.VISIBLE);
                            iv_checked2.setVisibility(View.INVISIBLE);
                            break;
                        case "男":
                            iv_checked1.setVisibility(View.INVISIBLE);
                            iv_checked2.setVisibility(View.VISIBLE);
                            break;
                    }
                    //保存缓存
                    SharedPreferencesUtils.saveString(AlterSexActivity.this, "sex", sex);
                    //刷新User
                    application.initUser();
                    Intent data = new Intent();
                    data.putExtra("sex", sex);
                    setResult(RESULT_CODE_SEX, data);
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
}
