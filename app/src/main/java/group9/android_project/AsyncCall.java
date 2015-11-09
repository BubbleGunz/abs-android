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


        switch(sw){
            case "CreateUser" :
            {
                JSONObject json = params[0].userInfo;
                ApiRequest.CreateUser(json);
                break;
            }
            case "GetToken":
            {
                AccessToken token = params[0].token;
                ApiRequest.GetToken(token);
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
