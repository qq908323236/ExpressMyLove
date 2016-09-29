package fu.mr.expressmylove.utils;

/**
 * Created by Fu on 2016/9/24 22:59.
 * 存放一些常量
 */
public class Constans {

    //本地服务器接口的基础地址
    public static final String URL_BASE = "http://10.5.86.41/expressmylove/";

    //远程服务器接口基础地址
//    public static final String URL_BASE = "http://123.206.42.197/expressmylove/";

    //用户登陆接口
    public static final String URL_LOGIN = URL_BASE + "user/login.php";

    //用户注册账号的接口
    public static final String URL_REGISTER = URL_BASE + "user/register.php";

    //注册账号时检查手机号是否注册的接口
    public static final String URL_CHECK_PHONE = URL_BASE + "user/check_phone.php";



}
