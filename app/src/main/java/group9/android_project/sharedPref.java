package group9.android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Fetes on 2015-11-10.
 */
 class SharedPref {

    private static String PREF_NAME ="prefs";

    private static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }
    public static String GetUsername (Context context) {
        return getPrefs(context).getString("username_key", "");
    }
    public static User GetUserPw (Context context){
        User user = new User();
        user.username = getPrefs(context).getString("username_key", "");
        user.password = getPrefs(context).getString("password_key", "");
        return user;
    }
    public static User GetTokenInfo (Context context){
        User user = new User();
        long time = getPrefs(context).getLong("tokendate_key",0L);
        user.token = getPrefs(context).getString("token_key", "default_token");
        user.date = new Date(time);
        return user;
    }
    public static void setUser(Context context,String username,String password,Date date, String token){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("username_key", username);
        editor.putString("password_key", password);
        editor.putLong("tokendate_key", date.getTime());
        editor.putString("token_key", token);
        editor.apply();
    }
    public static void clearPrefs(Context context) {
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        settings.edit().clear().apply();
    }

}
