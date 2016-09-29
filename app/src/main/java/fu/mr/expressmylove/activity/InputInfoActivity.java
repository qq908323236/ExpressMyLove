package fu.mr.expressmylove.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wq.photo.widget.PickConfig;
import com.yalantis.ucrop.UCrop;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.ArrayList;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.utils.CircleImageView;
import fu.mr.expressmylove.utils.Constans;


public class InputInfoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private boolean pwdIsShow = false;    //密码是否显示 false不显示，true显示

    private CircleImageView civ_avatar;
    private ImageView iv_back;
    private EditText et_nickname;
    private ImageView iv_delete1;
    private EditText et_password;
    private ImageView iv_delete2;
    private Button btn_regeisterSuccess;
    private ImageView iv_lookpassword;

    private String phone;
    private String avatarPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_input_info);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        civ_avatar = (CircleImageView) findViewById(R.id.civ_avatar);
        et_nickname = (EditText) findViewById(R.id.et_nickname);
        iv_delete1 = (ImageView) findViewById(R.id.iv_delete1);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        iv_lookpassword = (ImageView) findViewById(R.id.iv_lookpassword);
        iv_delete2 = (ImageView) findViewById(R.id.iv_delete2);
        btn_regeisterSuccess = (Button) findViewById(R.id.btn_regeisterSuccess);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        civ_avatar.setOnClickListener(this);
        iv_delete1.setOnClickListener(this);
        iv_delete1.setVisibility(View.INVISIBLE);
        iv_lookpassword.setOnClickListener(this);
        iv_lookpassword.setVisibility(View.INVISIBLE);
        iv_delete2.setOnClickListener(this);
        iv_delete2.setVisibility(View.INVISIBLE);
        btn_regeisterSuccess.setOnClickListener(this);
        btn_regeisterSuccess.setClickable(false);
        et_nickname.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
    }

    private void initData() {
        phone = getIntent().getStringExtra("phone");
        MyApplication._instance.addActivity(this);
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
            case R.id.iv_delete1:
                et_nickname.setText("");
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
            case R.id.iv_delete2:
                et_password.setText("");
                break;
            case R.id.btn_regeisterSuccess:
                //这里请求服务器，上传数据
                uploadUserInfo();
                break;
        }
    }

    /**
     * 选择头像
     */
    private void choseAvatar() {
        int chose_mode = PickConfig.MODE_SINGLE_PICK;   //设置为单选
        UCrop.Options options = new UCrop.Options();
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCompressionQuality(80);  //设置压缩比例
        new PickConfig.Builder(InputInfoActivity.this)
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
     * 用户注册，上传用户信息
     */
    private void uploadUserInfo() {
        RequestParams params = new RequestParams(Constans.URL_REGISTER);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("nickname", et_nickname.getText().toString().trim());
        params.addBodyParameter("upassword", et_password.getText().toString().trim());
        if (avatarPath != null){
            params.addBodyParameter("avatar", new File(avatarPath));
        }
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result.equals("0")) {
                    Toast.makeText(InputInfoActivity.this, "失败", Toast.LENGTH_SHORT).show();
                } else if (result.equals("-1")){
                    Toast.makeText(InputInfoActivity.this, "此昵称已经有人用了", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(InputInfoActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    System.out.println("uid:" + result);
                    startActivity(new Intent(InputInfoActivity.this, HomeActivity.class));
                    MyApplication._instance.deleteActivityList();   //把前面的activity全finsh掉
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(InputInfoActivity.this, "连接失败，请检查网络连接", Toast.LENGTH_SHORT).show();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            //在data中返回 选择的图片列表
            ArrayList<String> paths = data.getStringArrayListExtra("data");
            //paths.get(0) 就是单选的的图片的路径
            avatarPath = paths.get(0);
//            System.out.println("paths:" + paths.get(0));
//            x.image().bind(civ_avatar, paths.get(0));

        }
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
            iv_delete1.setVisibility(View.VISIBLE);
        } else {
            iv_delete1.setVisibility(View.INVISIBLE);
        }

        if (!TextUtils.isEmpty(et_password.getText().toString())) {
            iv_lookpassword.setVisibility(View.VISIBLE);
            iv_delete2.setVisibility(View.VISIBLE);
        } else {
            iv_lookpassword.setVisibility(View.INVISIBLE);
            iv_delete2.setVisibility(View.INVISIBLE);
        }

        btn_regeisterSuccess.setEnabled(et_nickname.getText().length() >= 1
                && et_password.getText().length() >= 6);

        btn_regeisterSuccess.setClickable(et_nickname.getText().length() >= 1
                && et_password.getText().length() >= 6);
    }
}
