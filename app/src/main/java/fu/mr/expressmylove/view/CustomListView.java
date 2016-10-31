package fu.mr.expressmylove.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import fu.mr.expressmylove.R;

import static fu.mr.expressmylove.R.id.listview;

/**
 * Created by Fu on 2016/10/7 21:53.
 */

public class CustomListView extends ListView {

    private int headerViewHeight;

    public CustomListView(Context context) {
        super(context);
    }

    public CustomListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
//                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    //
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
//        switch (ev.getAction()) {
//            // 当手指触摸listview时，让父控件交出ontouch权限,不能滚动
//            case MotionEvent.ACTION_DOWN:
//                setParentScrollAble(false);
//            case MotionEvent.ACTION_MOVE:
//                break;
//            case MotionEvent.ACTION_UP:
//            case MotionEvent.ACTION_CANCEL:
//                // 当手指松开时，让父控件重新获取onTouch权限
//                setParentScrollAble(true);
//                break;
//
//        }
        return super.onInterceptTouchEvent(ev);
    }

    // 设置父控件是否可以获取到触摸处理权限
    private void setParentScrollAble(boolean flag) {
        getParent().requestDisallowInterceptTouchEvent(!flag);
    }



}
