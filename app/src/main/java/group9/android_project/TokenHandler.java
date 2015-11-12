package group9.android_project;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by Fetes on 2015-11-11.
 */
public class TokenHandler {

    //Return true if token is still valid
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
}
