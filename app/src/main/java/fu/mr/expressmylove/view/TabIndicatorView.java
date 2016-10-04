package fu.mr.expressmylove.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import fu.mr.expressmylove.R;

/**
 * Created by Fu on 2016/10/4 21:25.
 */

public class TabIndicatorView extends RelativeLayout {

    private ImageView tab_indicator_icon;
    private TextView tab_indicator_hint;
    private TextView tab_indicator_unread;

    private int normalIconId;
    private int focusIconId;

    public TabIndicatorView(Context context) {
        this(context, null);
    }

    public TabIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        //讲布局文件和代码进行绑定
        View.inflate(context, R.layout.tab_indicator, this);
        tab_indicator_icon = (ImageView) findViewById(R.id.tab_indicator_icon);
        tab_indicator_hint = (TextView) findViewById(R.id.tab_indicator_hint);
        tab_indicator_unread = (TextView) findViewById(R.id.tab_indicator_unread);

        setTabUnreadCount(0);
    }

    /**
     * 设置标题
     */
    public void setTabTitle(String title) {
        tab_indicator_hint.setText(title);
    }

    public void setTabTitle(int title) {
        tab_indicator_hint.setText(title);
    }

    /**
     * 设置图标
     * @param normalIconId
     * @param focusIconId
     */
    public void setTabIcon(int normalIconId, int focusIconId){
        this.normalIconId = normalIconId;
        this.focusIconId = focusIconId;

        tab_indicator_icon.setImageResource(normalIconId);
    }

    /**
     * 设置未读数的显示
     * @param unreadCount
     */
    public void setTabUnreadCount(int unreadCount){
        if (unreadCount <= 0){
            tab_indicator_unread.setVisibility(View.INVISIBLE);
        }else{
            if (unreadCount <= 99){
                tab_indicator_unread.setText(unreadCount + "");
            } else {
                tab_indicator_unread.setText("99+");
            }
            tab_indicator_unread.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置选中和未选中显示的图片和文字颜色
     * @param selected
     */
    public void setTableSelected(boolean selected){
        if (selected){
            tab_indicator_icon.setImageResource(focusIconId);
            tab_indicator_hint.setTextColor(getResources().getColor(R.color.colorTabFocus));
        } else {
            tab_indicator_icon.setImageResource(normalIconId);
            tab_indicator_hint.setTextColor(getResources().getColor(R.color.colorTabNomar));
        }
    }
}
