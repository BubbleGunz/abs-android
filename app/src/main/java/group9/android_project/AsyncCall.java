package group9.android_project;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

import org.json.JSONObject;

/**
 * Created by Benjamin on 2015-11-06.
 */
public class AsyncCall extends AsyncTask<AsyncCallInfo,Void,Void> {
    @Override
    protected Void doInBackground(AsyncCallInfo... params) {
        String sw = params[0].command;
        Log.d("In background ",sw);
        JSONObject user = params[0].userInfo;
        switch(sw){
            case "CreateUser" :
            {
                //CreateAccActivity caa = new CreateAccActivity();
                //user = caa.GetUserInfo();
                ApiRequest.CreateUser(user);
                break;
            }
            case "GetToken":
            {
                ApiRequest.CreateUser(user);
                break;
            }
            default:{
                break;
            }

        }

        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
