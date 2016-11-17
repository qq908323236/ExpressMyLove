package fu.mr.expressmylove.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;

import java.net.URL;

import fu.mr.expressmylove.activity.bigImage.BigImageActivity;
import fu.mr.expressmylove.view.CustomProgressDialog;

/**
 * Created by xiayong on 2015/6/27.
 */
public class Utils {

    /**
     * Map a value within a given range to another range.
     * @param value the value to map
     * @param fromLow the low end of the range the value is within
     * @param fromHigh the high end of the range the value is within
     * @param toLow the low end of the range to map to
     * @param toHigh the high end of the range to map to
     * @return the mapped value
     */
    public static double mapValueFromRangeToRange(
            double value,
            double fromLow,
            double fromHigh,
            double toLow,
            double toHigh) {
        double fromRangeSize = fromHigh - fromLow;
        double toRangeSize = toHigh - toLow;
        double valueScale = (value - fromLow) / fromRangeSize;
        return toLow + (valueScale * toRangeSize);
    }

    /**
     * set margins of the specific view
     * @param target
     * @param l
     * @param t
     * @param r
     * @param b
     */
    public static void setMargin(View target, int l, int t, int r, int b){
        if (target.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) target.getLayoutParams();
            p.setMargins(l, t, r, b);
            target.requestLayout();
        }
    }

    /**
     * convert drawable to bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    /**
     * 显示圆形进度条，不带handler的
     * @param context
     * @return
     */
    public static CustomProgressDialog showCircleProgressDialog(Context context) {
        CustomProgressDialog progressDialog = CustomProgressDialog.createDialog(context);
        progressDialog.show();
        return progressDialog;
    }



    /**
     * 显示圆形进度条,带handler
     * @param context
     * @param handler
     * @return
     */
    public static final int PROGRESSDIALOG_DISMISS_CODE = 2;
    public static CustomProgressDialog showCircleProgressDialog(Context context, Handler handler) {
        CustomProgressDialog progressDialog = CustomProgressDialog.createDialog(context);
        Message msg = handler.obtainMessage();
        msg.what = PROGRESSDIALOG_DISMISS_CODE;
        progressDialog.setDismissMessage(msg);
        progressDialog.show();
        return progressDialog;
    }

    /**
     * xUtils3.0的圆形图片的配置
     */
    public static ImageOptions CircleImageOptions = new ImageOptions.Builder()
            .setSize(DensityUtil.dip2px(180), DensityUtil.dip2px(180))
            .setRadius(DensityUtil.dip2px(5))
            // 如果ImageView的大小不是定义为wrap_content, 不要crop.
            .setCrop(true)
            // 加载中或错误图片的ScaleType
            //.setPlaceholderScaleType(ImageView.ScaleType.MATRIX)
            .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
            //设置使用缓存
            .setUseMemCache(true)
            //设置不支持gif
            .setIgnoreGif(true)
            //设置显示圆形图片
            .setCircular(true)
            .build();

    /**
     * 查看大图
     */
    public static void SeeBigImage(View view, Activity activity, String url){
        ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view,
                view.getWidth() / 2, view.getHeight() / 2, 0, 0);
        Intent intent = new Intent(activity, BigImageActivity.class);
        intent.putExtra("url", url);
        ActivityCompat.startActivity(activity, intent,
                compat.toBundle());
    }
}
