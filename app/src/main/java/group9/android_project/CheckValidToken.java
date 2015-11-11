package group9.android_project;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Fetes on 2015-11-11.
 */
public class CheckValidToken {

    public static Boolean checkToken(Context context){
        User user = SharedPref.GetTokenInfo(context);
        Date currentDate = new Date();

        Log.d("CurrentDate: ",currentDate.toString());
        Log.d("TokenExpires: ", user.date.toString());
        int i = user.date.compareTo(currentDate);
        if(i < 0){ // Se till att savepref görs innan tokenjämförelsen
            return false;
        }
        else {
            return true;
        }
    }
    public static Boolean RefreshToken(Context context){
        User user = new User();
        user = SharedPref.GetUserPw(context);
        AsyncCallInfo info = new AsyncCallInfo();
        info.command = "GetToken";
        info.user = user;
        info.context = context;
        AsyncCall asc = new AsyncCall();
        asc.execute(info);
        return true;
    }

}
