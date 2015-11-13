package group9.android_project;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;

/**
 * Created by Fetes on 2015-11-13.
 */
public class AsyncCallArray extends AsyncTask<AsyncCallInfo,Void,JSONArray> {

    @Override
    protected JSONArray doInBackground(AsyncCallInfo... params) {
        String sw = params[0].command;
        Log.d("In background ", sw);

        switch (sw) {
            case "GetFriends": {
                User user = params[0].user;
                JSONArray response = ApiRequest.GetFriends(user, params[0].context);
                return response;
            }
            default:{
                break;
            }

        }
        return null;
    }
}
