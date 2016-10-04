package fu.mr.expressmylove.utils;

/**
 * Created by Fu on 2016/9/24 22:59.
 * 存放一些常量
 */
public class Constans {
    /**
     * mob短信验证的APPKEY 和APPSECRET
     */

    public static final String APP_KEY = "17341c343c280";

    public static final String APP_SERCRET = "c672c7cb35bc4c67da7fa65a50fa8cb7";


    /**
     * 服务器接口
     */
    //本地服务器接口的基础地址
//    public static final String URL_BASE = "http://10.5.86.41/expressmylove/";

    //远程服务器接口基础地址
    public static final String URL_BASE = "http://123.206.42.197/expressmylove/";

    //用户登陆接口
    public static final String URL_LOGIN = URL_BASE + "user/login.php";

    //用户注册账号的接口
    public static final String URL_REGISTER = URL_BASE + "user/register.php";

    //注册账号时检查手机号是否注册的接口
    public static final String URL_CHECK_PHONE = URL_BASE + "user/check_phone.php";

    //忘记密码后验证成功设置密码的接口
    public static final String URL_SET_PASSWORD = URL_BASE + "user/set_password.php";

    //服务协议的网址
    public static final String URL_SERVICE_AGREEMENT = URL_BASE + "fwxy.html";


    /**
     * 通过WHAT常量来判断为什么要手机发送验证码
     */
    public static final String WHAT_REGISTER = "REGISTER";

    public static final String WHAT_FORGETPASSWORD = "FORGETPASSWORD";

    /**
     *  通过WHAT_WEB常量来判断WEB界面打开什么网址
     */
    public static final String WHAT_WEB_SERVICE_AGREEMENT = "SERVICE_AGREEMENT";
}
