package fu.mr.expressmylove.activity.post;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import fu.mr.expressmylove.R;

import static fu.mr.expressmylove.R.id.et_nickName;
import static fu.mr.expressmylove.R.id.tv_submit;

public class PostActivity extends AppCompatActivity implements View.OnClickListener,TextWatcher{

    private TextView tv_cancel;
    private TextView tv_post;
    private EditText et_content;
    private ImageView iv_addImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        initUI();
        initListener();
        intiData();
    }

    private void initUI() {
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        tv_post = (TextView) findViewById(R.id.tv_post);
        et_content = (EditText) findViewById(R.id.et_content);
        iv_addImage = (ImageView) findViewById(R.id.iv_addImage);
    }

    private void initListener() {
        tv_cancel.setOnClickListener(this);
        tv_post.setOnClickListener(this);
        setBtnPostEnable(false);    //刚进来发布按钮不可用
        iv_addImage.setOnClickListener(this);
        et_content.addTextChangedListener(this);
    }

    private void intiData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_cancel:
                onBackPressed();
                break;
            case R.id.tv_post:
                Toast.makeText(this, "发表帖子", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_addImage:
                Toast.makeText(this, "添加图片", Toast.LENGTH_SHORT).show();
                break;
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
        setBtnPostEnable(!TextUtils.isEmpty(et_content.getText()));
    }

    /**
     * 设置Post按钮可用不,true可用，false不可用
     *
     * @param b
     */
    private void setBtnPostEnable(boolean b) {
        if (b) {
            tv_post.setTextColor(Color.parseColor("#33475f"));
            tv_post.setClickable(true);
        } else {
            tv_post.setTextColor(Color.parseColor("#55000000"));
            tv_post.setClickable(false);
        }
    }
}
