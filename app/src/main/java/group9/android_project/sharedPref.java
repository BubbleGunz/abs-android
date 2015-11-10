package group9.android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
        editor.putString("username_key", username);
        editor.putString("password_key", pwd);
        editor.putString("token_key", token);
        Calendar cal = Calendar.getInstance();
        Log.d("Datum set", cal.getTime().toString());
        Calendar calExpires = cal;
        calExpires.add(calExpires.DATE, 1);
        Log.d("mamms",calExpires.getTime().toString());
        if(cal.compareTo(calExpires) < 0) // FIXA så att det går att jämföra
        {
            Log.d("Prefsinfo:", "token acceptable");
        }
        else{
            Log.d("Prefsinfo:", "token denied");
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:HH:mm");
        System.out.println(sdf.format(cal.getTime()));
        editor.putString("token_expire", sdf.format(cal.getTime()));
        editor.apply();
    }
}
