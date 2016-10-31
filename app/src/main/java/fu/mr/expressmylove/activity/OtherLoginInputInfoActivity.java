package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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

import com.tencent.tauth.Tencent;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;
import com.wq.photo.widget.PickConfig;
import com.yalantis.ucrop.UCrop;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.view.CustomProgressDialog;
import fu.mr.expressmylove.view.ShapeImageView;

import static com.wq.photo.adapter.PhotoAdapter.params;


public class OtherLoginInputInfoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private final int PROGRESSDIALOG_DISMISS_CODE = 2;

    private MyApplication application;

    private ImageView iv_back;
    private ShapeImageView civ_avatar;
    private EditText et_nickname;
    private ImageView iv_delete;
    private Button btn_ok;

    private Tencent mTencent;

    private String openid;
    private String nickname;
    private String avatarUrl;

    private NormalAlertDialog dialog;
    private boolean isGiveUp = false;

    private CustomProgressDialog progressDialog;
    private String avatarPath;
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
                    if (cancelable != null){
                        cancelable.cancel();
                    }
                    break;
            }
        }
    };

    private void initUI() {
        setContentView(R.layout.activity_qqlogin_input_info);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        civ_avatar = (ShapeImageView) findViewById(R.id.civ_avatar);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        iv_delete = (ImageView) findViewById(R.id.iv_delete);
        btn_ok = (Button) findViewById(R.id.btn_ok);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        civ_avatar.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        iv_delete.setVisibility(View.INVISIBLE);
        btn_ok.setOnClickListener(this);
        btn_ok.setClickable(false);
        et_nickname.addTextChangedListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        application.addActivity(this);
        Intent intent = getIntent();
        openid = intent.getStringExtra("openid");
        nickname = intent.getStringExtra("nickname");
        avatarUrl = intent.getStringExtra("avatarUrl");

        et_nickname.setText(nickname);
        downLoadAvatar(avatarUrl);

        mTencent = Tencent.createInstance(Constans.APP_ID_QQ, getApplicationContext());

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.civ_avatar:
                choseAvatar();
                break;
            case R.id.iv_delete:
                et_nickname.setText("");
                break;
            case R.id.btn_ok:
                //这里请求服务器，上传数据
                showProgressBar();
                uploadUserInfo();
                break;
        }
    }

    /**
     * 用户注册，上传用户信息
     */
    private void uploadUserInfo() {
        RequestParams params = new RequestParams(Constans.URL_REGISTER);
        params.addBodyParameter("qq_open_id", openid);
        params.addBodyParameter("nickname", et_nickname.getText().toString().trim());
        if (avatarPath != null) {
            params.addBodyParameter("avatar", new File(avatarPath));
        }
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {  //这个result就是uid
                progressDialog.dismiss();
                if (result.equals("0")) {
                    Toast.makeText(OtherLoginInputInfoActivity.this, "失败", Toast.LENGTH_SHORT).show();
                } else if (result.equals("-1")) {
                    Toast.makeText(OtherLoginInputInfoActivity.this, "此昵称已经有人用了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(OtherLoginInputInfoActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(OtherLoginInputInfoActivity.this, HomeActivity.class);
                    intent.putExtra("uid", result);
                    startActivity(intent);
                    application.deleteActivityList();   //把前面的activity全finsh掉
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(OtherLoginInputInfoActivity.this, "连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
     * 显示是否放弃注册的dialog
     */
    private void showIsGiveUpDialog() {
        dialog = new NormalAlertDialog.Builder(OtherLoginInputInfoActivity.this)
                .setHeight(0.23f)  //屏幕高度*0.23
                .setWidth(0.65f)  //屏幕宽度*0.65
                .setTitleVisible(false)
                .setContentText("你确定要放弃注册吗？")
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
                        mTencent.logout(OtherLoginInputInfoActivity.this);
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
        Message msg = handler.obtainMessage();
        msg.what = PROGRESSDIALOG_DISMISS_CODE;
        progressDialog.setDismissMessage(msg);
        progressDialog.show();
    }

    /**
     * 选择头像
     */
    private void choseAvatar() {
        int chose_mode = PickConfig.MODE_SINGLE_PICK;   //设置为单选
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(80);  //设置压缩比例
        new PickConfig.Builder(OtherLoginInputInfoActivity.this)
                .isneedcrop(true)   //是否要剪裁
                .actionBarcolor(Color.parseColor("#FF4081"))
                .statusBarcolor(Color.parseColor("#D81B60"))
                .isneedcamera(true)    //是否需要相机
                .isSqureCrop(true)      //是否为正方形剪裁
                .setUropOptions(options)
                .maxPickSize(1)      //最多选择几张照片
                .spanCount(3)       //一行显示几张照片
                .pickMode(chose_mode).build();
    }

    /**
     * 下载QQ头像
     * @param avatarUrl
     */
    private void downLoadAvatar(String avatarUrl){
        RequestParams params = new RequestParams(avatarUrl);
        String cachePath = getCacheDir().getAbsolutePath();
        params.setSaveFilePath(cachePath);
        params.setAutoRename(true);
        x.http().get(params, new Callback.CommonCallback<File>() {
            @Override
            public void onSuccess(File result) {
                avatarPath = result.getAbsolutePath();
                x.image().bind(civ_avatar, avatarPath);

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
        if (!TextUtils.isEmpty(et_nickname.getText().toString())) {
            iv_delete.setVisibility(View.VISIBLE);
        } else {
            iv_delete.setVisibility(View.INVISIBLE);
        }

        btn_ok.setEnabled(et_nickname.getText().length() >= 1);

        btn_ok.setClickable(et_nickname.getText().length() >= 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            //在data中返回 选择的图片列表
            ArrayList<String> paths = data.getStringArrayListExtra("data");
            //paths.get(0) 就是单选的的图片的路径
            avatarPath = paths.get(0);
            x.image().bind(civ_avatar, paths.get(0));

        }
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
