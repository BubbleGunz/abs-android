package group9.android_project;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fetes on 2015-11-10.
 */
public class sharedPref {

    private static String PREF_NAME ="prefs";

    private static SharedPreferences getPrefs(Context context){
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
    }
    public  static String getUsername(Context context){
        return getPrefs(context).getString("username_key", "default_username");
    }
    public static void setUser(Context context,String username,String pwd,String token){
        SharedPreferences.Editor editor = getPrefs(context).edit();
        editor.putString("username_key",username);
        editor.putString("password_key",pwd);
        editor.putString("token_key",token);
        editor.apply();
    }
}
