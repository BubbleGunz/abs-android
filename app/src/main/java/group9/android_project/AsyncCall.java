package group9.android_project;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by Benjamin on 2015-11-06.
 */
public class AsyncCall extends AsyncTask<String,Void,Void> {
    @Override
    protected Void doInBackground(String... params) {
        String sw = params.toString();
        User user = new User();
        ApiRequest.CreateUser(user);
        return null;
    }
}
