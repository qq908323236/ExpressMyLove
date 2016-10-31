package fu.mr.expressmylove.activity.editAccountInfo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;
import fu.mr.expressmylove.utils.Utils;
import fu.mr.expressmylove.view.CustomProgressDialog;

public class EditAccountInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_back;
    private LinearLayout ll_avatar;
    private LinearLayout ll_nickName;
    private LinearLayout ll_sex;
    private LinearLayout ll_personalize;
    private ImageView iv_avatar;
    private TextView tv_nickName;
    private TextView tv_sex;
    private TextView tv_personalize;
    private MyApplication application;
    private User user;

    private static final int REQUEST_CODE_NICKNAME = 1;
    private static final int REQUEST_CODE_SEX = 2;
    private static final int REQUEST_CODE_PERSONALIZE = 3;
    private Bundle bundle;
    private static final int RESULT_CODE_EDIT_INFO = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
    }

    private void initUI() {
        setContentView(R.layout.activity_edit_account_info);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ll_avatar = (LinearLayout) findViewById(R.id.ll_avatar);
        iv_avatar = (ImageView) findViewById(R.id.iv_avatar);
        ll_nickName = (LinearLayout) findViewById(R.id.ll_nickName);
        tv_nickName = (TextView) findViewById(R.id.tv_nickName);
        ll_sex = (LinearLayout) findViewById(R.id.ll_sex);
        tv_sex = (TextView) findViewById(R.id.tv_sex);
        ll_personalize = (LinearLayout) findViewById(R.id.ll_personalize);
        tv_personalize = (TextView) findViewById(R.id.tv_personalize);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        ll_avatar.setOnClickListener(this);
        ll_nickName.setOnClickListener(this);
        ll_sex.setOnClickListener(this);
        ll_personalize.setOnClickListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        user = application.getUser();

        /**
         * 初始化用户信息并显示
         */
        //头像绑定
        x.image().bind(iv_avatar, Constans.URL_BASE + user.getAvatar(), Utils.CircleImageOptions);
        //昵称
        tv_nickName.setText(user.getNickname());
        //性别
        tv_sex.setText(user.getSex());
        //个性签名
        tv_personalize.setText(user.getPersonalize());

        //用来存储哪些信息改变了，方便上一个界面更新,改变了就设为true
        bundle = new Bundle();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.ll_avatar:
                choseAvatar();
                break;
            case R.id.ll_nickName:
                startActivityForResult(new Intent(this, AlterNicknameActivity.class), REQUEST_CODE_NICKNAME);
                break;
            case R.id.ll_sex:
                startActivityForResult(new Intent(this, AlterSexActivity.class), REQUEST_CODE_SEX);
                break;
            case R.id.ll_personalize:
                startActivityForResult(new Intent(this, AlterPersonalizeActivity.class), REQUEST_CODE_PERSONALIZE);
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
        new PickConfig.Builder(EditAccountInfoActivity.this)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //选择头像界面返回的
        if (resultCode == RESULT_OK && requestCode == PickConfig.PICK_REQUEST_CODE) {
            //在data中返回 选择的图片列表
            ArrayList<String> paths = data.getStringArrayListExtra("data");
            //paths.get(0) 就是单选的的图片的路径
            uploadAvatar(paths.get(0));
            //改变了就设为true
            bundle.putBoolean("avatar", true);
        }
        //修改昵称返回的
        if (resultCode == REQUEST_CODE_NICKNAME && requestCode == resultCode) {
            tv_nickName.setText(data.getStringExtra("nickname"));
            bundle.putBoolean("nickname", true);
        }
        //修改性别返回的
        if (resultCode == REQUEST_CODE_SEX && requestCode == resultCode) {
            tv_sex.setText(data.getStringExtra("sex"));
            bundle.putBoolean("sex", true);
        }
        //修改个性签名返回的
        if (resultCode == REQUEST_CODE_PERSONALIZE && requestCode == resultCode) {
            tv_personalize.setText(data.getStringExtra("personalize"));
            bundle.putBoolean("personalize", true);
        }
    }

    /**
     * 长传头像
     *
     * @param avatarPath 头像文件路径
     */
    private void uploadAvatar(final String avatarPath) {
        final CustomProgressDialog progressDialog = Utils.showCircleProgressDialog(this);
        RequestParams params = new RequestParams(Constans.URL_UPDATE_USER_INFO);
        params.addBodyParameter("uid", user.getUid());
        params.addBodyParameter("what", Constans.UPDATE_USER_INFO_WHAT_AVATAR);
        params.addBodyParameter("avatar", new File(avatarPath));
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                if (result.equals("0")) {
                    Toast.makeText(application, "头像上传失败", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(application, "头像上传成功", Toast.LENGTH_SHORT).show();
                    x.image().bind(iv_avatar, Constans.URL_BASE + result, Utils.CircleImageOptions);
                    //保存缓存
                    SharedPreferencesUtils.saveString(EditAccountInfoActivity.this, "avatar", result);
                    //刷新User
                    application.initUser();
                }
                System.out.println(result);
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
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("bundle",bundle);
        setResult(RESULT_CODE_EDIT_INFO,data);
//        finish();
        super.onBackPressed();
    }
}
