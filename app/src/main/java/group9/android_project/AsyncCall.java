package group9.android_project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Benjamin on 2015-11-06.
 */
public class AsyncCall extends AsyncTask<AsyncCallInfo,Void,JSONObject> {
    @Override
    protected JSONObject doInBackground(AsyncCallInfo... params) {
        String sw = params[0].command;
        Log.d("In background ",sw);


        switch(sw){
            case "CreateUser" :
            {
                JSONObject json = params[0].userInfo;
                JSONObject response = ApiRequest.CreateUser(json);
                return response;
            }
            case "GetToken":
            {
                User user = params[0].user;
                JSONObject response = ApiRequest.GetToken(user,params[0].context);
                return response;
            }
            default:{
                break;
            }

        }

        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        try {
            int code = (int)jsonObject.get("code");
            Context context = (Context)jsonObject.get("context");
            if(code >199 || code <300)
            {
                Toast.makeText(context, jsonObject.get("message").toString(), Toast.LENGTH_SHORT).show();
                return;
            }
            else{

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}
