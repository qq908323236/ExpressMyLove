package fu.mr.expressmylove.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.wevey.selector.dialog.DialogOnClickListener;
import com.wevey.selector.dialog.NormalAlertDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.view.CustomProgressDialog;

public class LoginActivity extends Activity implements View.OnClickListener, TextWatcher {

    private final int PROGRESSDIALOG_DISMISS_CODE = 2;

    private TextView tv_register;
    private ImageView iv_back;
    private EditText et_phone;
    private ImageView iv_delete1;
    private EditText et_password;
    private ImageView iv_lookpassword;
    private ImageView iv_delete2;
    private TextView tv_forgetPassword;
    private ImageView iv_loginLogo;
    private Button btn_logindo;
    private ImageView iv_qq;
    private RelativeLayout rl_layout;

    private CustomProgressDialog progressDialog;

    private NormalAlertDialog dialog;

    private boolean pwdIsShow = false;    //密码是否显示 false不显示，true显示

    private String phone;

    private Callback.Cancelable cancelable;

    private MyApplication application;

    /**
     * 动画要用的参数
     */
    private boolean isShow;
    private boolean isEdShow;
    private int height;
    private int count = 0;

    private Tencent mTencent;
    private QQLogInListener mQQListener;
    private String openid;
    private String nickname;
    private String avatarUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
        initAnimation();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PROGRESSDIALOG_DISMISS_CODE:
                    //取消请求
                    if (cancelable != null) {
                        cancelable.cancel();
                    }
                    break;
            }
        }
    };

    private void initUI() {
        setContentView(R.layout.activity_login);
        rl_layout = (RelativeLayout) findViewById(R.id.rl_layout);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_register = (TextView) findViewById(R.id.tv_register);
        iv_loginLogo = (ImageView) findViewById(R.id.iv_loginLogo);
        et_phone = (EditText) findViewById(R.id.et_phone);
        iv_delete1 = (ImageView) findViewById(R.id.iv_delete1);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        iv_lookpassword = (ImageView) findViewById(R.id.iv_lookpassword);
        iv_delete2 = (ImageView) findViewById(R.id.iv_delete2);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
        btn_logindo = (Button) findViewById(R.id.btn_logindo);
        iv_qq = (ImageView) findViewById(R.id.iv_qq);
    }

    private void initListener() {
        iv_back.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        iv_delete1.setOnClickListener(this);
        iv_delete1.setVisibility(View.INVISIBLE);
        iv_lookpassword.setOnClickListener(this);
        iv_lookpassword.setVisibility(View.INVISIBLE);
        iv_delete2.setOnClickListener(this);
        iv_delete2.setVisibility(View.INVISIBLE);
        tv_forgetPassword.setOnClickListener(this);
        btn_logindo.setOnClickListener(this);
        btn_logindo.setClickable(false);
        et_phone.addTextChangedListener(this);
        et_password.addTextChangedListener(this);
        iv_qq.setOnClickListener(this);
    }

    private void initData() {
        application = (MyApplication) getApplication();
        application.addActivity(this);
        mTencent = Tencent.createInstance(Constans.APP_ID_QQ, getApplicationContext());
        mQQListener = new QQLogInListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                onBackPressed();
                break;
            case R.id.tv_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.iv_delete1:
                et_phone.setText("");
                break;
            case R.id.iv_lookpassword:
                if (pwdIsShow) {
                    //隐藏密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    et_password.setSelection(et_password.getText().length());  //设置editText的光标位置为最后
                    iv_lookpassword.setImageResource(R.mipmap.eye2);
                } else {
                    //显示密码
                    et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    et_password.setSelection(et_password.getText().length());//设置editText的光标位置为最后
                    iv_lookpassword.setImageResource(R.mipmap.eye1);
                }
                pwdIsShow = !pwdIsShow;
                break;
            case R.id.iv_delete2:
                et_password.setText("");
                break;
            case R.id.tv_forgetPassword:
                startActivity(new Intent(this, ForgetPasswordActivity.class));
                break;
            case R.id.btn_logindo:
                phone = et_phone.getText().toString().trim();
                if (phone.matches("^1[3-8]\\d{9}$")) {   //匹配是否是手机号
                    login();
                } else {
                    Toast.makeText(LoginActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.iv_qq:
                if (!mTencent.isSessionValid()) {
                    showProgressBar();
                    mTencent.login(LoginActivity.this, "get_simple_userinfo", mQQListener);
                }
                break;
        }
    }

    /**
     * 登陆，服务器请求
     */
    private void login() {
        showProgressBar();
        RequestParams params = new RequestParams(Constans.URL_LOGIN);
        params.addBodyParameter("phone", phone);
        params.addBodyParameter("upassword", et_password.getText().toString().trim());
        //把前面的activity全finsh掉
        cancelable = x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                progressDialog.dismiss();
                if (result.equals("0")) {
                    Toast.makeText(LoginActivity.this, "失败", Toast.LENGTH_SHORT).show();
                } else if (result.equals("-1")) { //还没注册呢
                    showNoRegisterDialog();
                } else if (result.equals("password error")) {
                    Toast.makeText(LoginActivity.this, "密码错误，请重新输入", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.putExtra("uid", result);
                    startActivity(intent);
                    application.deleteActivityList();   //把前面的activity全finsh掉
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
     * 显示该手机号未注册的dialog
     */
    private void showNoRegisterDialog() {
        dialog = new NormalAlertDialog.Builder(LoginActivity.this)
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
                        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
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

    /**
     * 初始化输入法弹出隐藏logo的动画
     */
    private void initAnimation() {
        et_phone.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("HHHHHHH", "click isShow " + isShow + "   isEdShow " + isEdShow);
                if (!isEdShow) {
                    if (!isShow) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_loginLogo, "alpha", 1.0f, 0f);
                        anim.setDuration(500);// 动画持续时间

                        ObjectAnimator animator = ObjectAnimator.ofFloat(rl_layout, "translationY", 0, -height);
                        animator.setDuration(500);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isEdShow = true;

                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                        anim.start();
                        animator.start();
                    }
                }
                return false;
            }
        });

        et_password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.e("HHHHHHH", "click isShow " + isShow + "   isEdShow " + isEdShow);
                if (!isEdShow) {
                    if (!isShow) {
                        ObjectAnimator anim = ObjectAnimator.ofFloat(iv_loginLogo, "alpha", 1.0f, 0f);
                        anim.setDuration(500);// 动画持续时间

                        ObjectAnimator animator = ObjectAnimator.ofFloat(rl_layout, "translationY", 0, -height);
                        animator.setDuration(500);
                        animator.addListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                isEdShow = true;
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        });

                        anim.start();
                        animator.start();
                    }
                }
                return false;
            }
        });

        rl_layout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                count++;
                if (isShow && count > 2) {
                    ObjectAnimator anim = ObjectAnimator.ofFloat(iv_loginLogo, "alpha", 0f, 1f);
                    anim.setDuration(500);// 动画持续时间

                    ObjectAnimator animator = ObjectAnimator.ofFloat(rl_layout, "translationY", -height, 0);
                    animator.setDuration(500);
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            isEdShow = false;
                            isShow = !isShow;
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });

                    anim.start();
                    animator.start();
                    Log.e("HHHHHHH", "---->isShow " + isShow + "   isEdShow " + isEdShow);
                } else {
                    isShow = !isShow;
                }
                Log.e("HHHHHHH", "isShow " + isShow + "   isEdShow " + isEdShow);
            }
        });

        calcuHeight();
    }

    /**
     * 计算高度
     */
    private void calcuHeight() {
        ViewTreeObserver vto = iv_loginLogo.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                iv_loginLogo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                height = iv_loginLogo.getMeasuredHeight();
                int width = iv_loginLogo.getMeasuredWidth();
                Log.e("HHHHHHHH", "h:" + height + "  w:" + width);
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
        if (!TextUtils.isEmpty(et_phone.getText().toString())) {
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

        btn_logindo.setEnabled(et_phone.getText().length() == 11
                && et_password.getText().length() >= 6);
        btn_logindo.setClickable(et_phone.getText().length() == 11
                && et_password.getText().length() >= 6);
    }

    /**
     * QQ授权回调的实现类
     */
    private class QQLogInListener implements IUiListener {

        @Override
        public void onComplete(Object o) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            final JSONObject jsonObject = (JSONObject) o;
            try {
                openid = jsonObject.getString("openid");  //唯一识别码 注册需要
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //这里判断如何数据库中有这个openid就不需要注册了，就直接登录
            RequestParams params = new RequestParams(Constans.URL_CHECK_OPEN_ID);
            params.addBodyParameter("qq_open_id", openid);
            x.http().post(params, new Callback.CommonCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    if (result.equals("0")) {
                        //该QQ没有注册过,就去注册
                        initOpenidAndToken(jsonObject);
                        getUserInfo();
                    } else {
                        //注册过，就获取到uid,然后直接进入主页
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("uid", result);
                        startActivity(intent);
                        application.deleteActivityList();   //把前面的activity全finsh掉
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
        public void onError(UiError uiError) {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "授权出错", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {
            progressDialog.dismiss();
            Toast.makeText(LoginActivity.this, "授权取消", Toast.LENGTH_SHORT).show();
        }
    }

    //设置Openid和Token
    private void initOpenidAndToken(JSONObject jsonObject) {
        try {
            String token = jsonObject.getString("access_token");
            String expires = jsonObject.getString("expires_in");

            mTencent.setAccessToken(token, expires);
            mTencent.setOpenId(openid);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void getUserInfo() {
        //sdk给我们提供了一个类UserInfo，这个类中封装了QQ用户的一些信息，我么可以通过这个类拿到这些信息
        QQToken mQQToken = mTencent.getQQToken();
        UserInfo userInfo = new UserInfo(LoginActivity.this, mQQToken);
        userInfo.getUserInfo(new IUiListener() {
            @Override
            public void onComplete(Object o) {
                JSONObject userInfoJson = (JSONObject) o;
                System.out.println(o.toString());
                try {
                    //获得QQ昵称
                    nickname = userInfoJson.getString("nickname");
                    //获得QQ头像URL
                    avatarUrl = userInfoJson.getString("figureurl_qq_2");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(LoginActivity.this, OtherLoginInputInfoActivity.class);
                intent.putExtra("openid", openid);
                intent.putExtra("nickname", nickname);
                intent.putExtra("avatarUrl", avatarUrl);
                startActivity(intent);
            }

            @Override
            public void onError(UiError uiError) {
                Log.e("GET_QQ_INFO_ERROR", "获取qq用户信息错误");
                Toast.makeText(LoginActivity.this, "获取qq用户信息错误", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Log.e("GET_QQ_INFO_CANCEL", "获取qq用户信息取消");
                Toast.makeText(LoginActivity.this, "获取qq用户信息取消", Toast.LENGTH_SHORT).show();
            }
        });

    }


    //确保能接收到回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tencent.onActivityResultData(requestCode, resultCode, data, mQQListener);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
