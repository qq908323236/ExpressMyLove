package fu.mr.expressmylove.application;

import android.app.Activity;
import android.app.Application;

import org.xutils.x;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Fu on 2016/9/24 23:12.
 */
public class MyApplication extends Application {

    public static MyApplication _instance;   //单例

    private List<Activity> activityList = new LinkedList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        if (_instance == null){
            _instance = new MyApplication();
        }
        x.Ext.init(this);
    }

    public void addActivity(Activity activity){
        activityList.add(activity);
    }

    public void deleteActivityList(){
        for (Activity activity : activityList) {
            activity.finish();
        }
    }

}
