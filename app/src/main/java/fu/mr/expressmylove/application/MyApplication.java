package fu.mr.expressmylove.application;

import android.app.Activity;
import android.app.Application;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import fu.mr.expressmylove.activity.HomeActivity;
import fu.mr.expressmylove.domain.User;
import fu.mr.expressmylove.utils.Constans;
import fu.mr.expressmylove.utils.SharedPreferencesUtils;

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

    /**
     * 初始化User,从缓存中获取User的信息,然后赋值给User
     * 当缓存中的信息修改后也要调用这个方法来刷新全局的User
     */
    public void initUser(){
        user.setUid(SharedPreferencesUtils.getString(this, "uid", ""));
        user.setAvatar(SharedPreferencesUtils.getString(this, "avatar", ""));
        user.setNickname(SharedPreferencesUtils.getString(this, "nickname", ""));
        user.setPersonalize(SharedPreferencesUtils.getString(this, "personalize", ""));
        user.setSex(SharedPreferencesUtils.getString(this, "sex", ""));
        user.setAuthentication(SharedPreferencesUtils.getString(this, "authentication", ""));
    }

    /**
     * 从服务器获取用户信息到缓存,并更新user
     * @param uid
     */
    public void getUserInfoFromService(final String uid){
        RequestParams params = new RequestParams(Constans.URL_GET_USER_INFO);
        params.addBodyParameter("uid", uid);
        x.http().post(params, new Callback.CommonCallback<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                try {
                    String avatar = result.getString("avatar");
                    String nickname = result.getString("nickname");
                    String sex = result.getString("sex");
                    String personalize = result.getString("personalize");
                    String authentication = result.getString("authentication");
                    user.setUid(uid);
                    user.setAvatar(avatar);
                    user.setNickname(nickname);
                    user.setPersonalize(personalize);
                    user.setSex(sex);
                    user.setAuthentication(authentication);

                    //登陆过，就设为自动登陆
                    SharedPreferencesUtils.saveBoolean(MyApplication.this, "isAutoLogin", true);
                    //缓存用户信息
                    SharedPreferencesUtils.saveString(MyApplication.this, "uid", uid);
                    SharedPreferencesUtils.saveString(MyApplication.this, "avatar", avatar);
                    SharedPreferencesUtils.saveString(MyApplication.this, "nickname", nickname);
                    SharedPreferencesUtils.saveString(MyApplication.this, "personalize", personalize);
                    SharedPreferencesUtils.saveString(MyApplication.this, "sex", sex);
                    SharedPreferencesUtils.saveString(MyApplication.this, "authentication", authentication);
                } catch (JSONException e) {
                    e.printStackTrace();
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
}
