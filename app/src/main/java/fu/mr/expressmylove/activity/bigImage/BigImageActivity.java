package fu.mr.expressmylove.activity.bigImage;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import org.xutils.common.Callback;
import org.xutils.x;

import fu.mr.expressmylove.R;
import uk.co.senab.photoview.PhotoViewAttacher;



public class BigImageActivity extends AppCompatActivity {

    private ImageView iv_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        iv_image = (ImageView) findViewById(R.id.iv_image);

        x.image().bind(iv_image, getIntent().getStringExtra("url"), new Callback.ProgressCallback<Drawable>() {
            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                System.out.println("onStarted");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                System.out.println("total:" + total);
                System.out.println(current);
            }

            @Override
            public void onSuccess(Drawable result) {
                new PhotoViewAttacher(iv_image);
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
        super.onBackPressed();
        overridePendingTransition(0, R.anim.zoomout);
    }
}
