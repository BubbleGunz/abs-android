package group9.android_project;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Switch;

/**
 * Created by Benjamin on 2015-11-06.
 */
public class AsyncCall extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... params) {
        String sw = params[0];
        Log.d("In background ",sw);
        User user = new User();
        switch(sw){
            case "CreateUser" :
            {
                CreateAccActivity caa = new CreateAccActivity();
                user = caa.GetUserInfo();
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
