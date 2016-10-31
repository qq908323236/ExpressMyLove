package fu.mr.expressmylove.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fu on 2016/10/6 14:29.
 *
 * 封装的SharedPreferences的工具类
 */

public class SharedPreferencesUtils {

    public static final String SP_NAME = "config";

    /**
     * String类型的变量的存取
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context, String key, String value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        sp.edit().putString(key,value).commit();
    }

    public static String getString(Context context, String key, String defvalue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        return sp.getString(key,defvalue);
    }


    /**
     * boolean类型的变量的存取
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        sp.edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defvalue) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, context.MODE_PRIVATE);
        return sp.getBoolean(key, defvalue);
    }
}
