package fu.mr.expressmylove.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import fu.mr.expressmylove.R;
import fu.mr.expressmylove.application.MyApplication;

public class SelectActivity extends Activity {

    private ImageView iv_guide1;
    private ImageView iv_guide2;
    private ImageView iv_guide3;
    private ImageView iv_guide4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initListener();
        initData();
        startAnimation();

    }

    private void initUI() {
        setContentView(R.layout.activity_select);
        iv_guide1 = (ImageView) findViewById(R.id.iv_guide1);
        iv_guide2 = (ImageView) findViewById(R.id.iv_guide2);
        iv_guide3 = (ImageView) findViewById(R.id.iv_guide3);
        iv_guide4 = (ImageView) findViewById(R.id.iv_guide4);
    }

    private void initListener() {

    }

    private void initData() {
        ((MyApplication) getApplication()).addActivity(this);
    }

    private void startAnimation() {
        ObjectAnimator anim1 = new ObjectAnimator().ofFloat(iv_guide1, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim2 = new ObjectAnimator().ofFloat(iv_guide2, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set1 = new AnimatorSet();
        set1.playTogether(anim1, anim2);

        ObjectAnimator anim3 = new ObjectAnimator().ofFloat(iv_guide2, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim4 = new ObjectAnimator().ofFloat(iv_guide3, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set2 = new AnimatorSet();
        set2.playTogether(anim3, anim4);

        ObjectAnimator anim5 = new ObjectAnimator().ofFloat(iv_guide3, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim6 = new ObjectAnimator().ofFloat(iv_guide4, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set3 = new AnimatorSet();
        set3.playTogether(anim5, anim6);

        ObjectAnimator anim7 = new ObjectAnimator().ofFloat(iv_guide4, "alpha", 1f, 0f).setDuration(5000);
        ObjectAnimator anim8 = new ObjectAnimator().ofFloat(iv_guide1, "alpha", 0f, 1f).setDuration(5000);
        AnimatorSet set4 = new AnimatorSet();
        set4.playTogether(anim7, anim8);

        AnimatorSet set_all = new AnimatorSet();
        set_all.playSequentially(set1, set2, set3, set4);
        set_all.addListener(new AnimatorListenerAdapter() {

            private boolean mCanceled;

            @Override
            public void onAnimationStart(Animator animation) {
                mCanceled = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCanceled = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!mCanceled) {
                    animation.start();
                }
            }
        });
        set_all.start();

    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public void register(View view) {
        startActivity(new Intent(this, RegisterActivity.class));
    }
}
