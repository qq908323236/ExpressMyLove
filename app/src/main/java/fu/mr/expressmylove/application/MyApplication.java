package fu.mr.expressmylove.application;

import android.app.Activity;
import android.app.Application;

import org.xutils.x;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fu.mr.expressmylove.domain.User;

/**
 * Created by Fu on 2016/9/24 23:12.
 */
public class MyApplication extends Application {

    private List<Activity> activityList = new LinkedList<Activity>();
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);

        user = new User();
    }

    /**
     * 存放一些前面要被销毁的ACTIVITY
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 销毁Activity集合里面的所有activity
     */
    public void deleteActivityList() {
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
