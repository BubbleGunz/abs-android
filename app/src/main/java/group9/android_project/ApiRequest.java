package group9.android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * Created by Benjamin on 2015-11-05.
 */
public class ApiRequest {
    public Context context;
    public static final String PREFS_NAME="USER_PREFS";
    public static JSONObject CreateUser(JSONObject user){
        try
        {
            //region CONNECTION
            URL url = new URL("http://www.abs-cloud.elasticbeanstalk.com/api/v1/accounts/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);

            osw.write(user.toString());
            osw.close();
            os.close();

            int code = urlConnection.getResponseCode();
            Log.d("Code", ""+code);

            urlConnection.disconnect();
            //endregion
            JSONObject jsonResponse = new JSONObject();
            if(code >199 || code < 300) {
                try {
                    jsonResponse.put("message","Account created!");
                    jsonResponse.put("code",code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonResponse;
            }
            else{
                try {
                    jsonResponse.put("message","Couldnt create account!");
                    jsonResponse.put("code",code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonResponse;
            }
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

       
    }
    public static JSONObject GetToken(User user,Context context){

        try
        {
            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/token");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Accept", "application/json");
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write("grant_type=password&username=" + user.username + "&password=" + user.password);
            osw.close();
            os.close();


            int code = urlConnection.getResponseCode();
            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            InputStream is = urlConnection.getInputStream();
            StringBuilder sb = new StringBuilder();

            String line;
            br = new BufferedReader(new InputStreamReader(is));
            JSONObject json = new JSONObject();
            String accessToken = new String();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            try {
                 json = new JSONObject(sb.toString());
            }catch (JSONException e)
            {
                e.printStackTrace();
            }
            try {
                accessToken = json.getString("access_token");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.d("Code", "" + code);
            Log.d("Accestoken", accessToken);

            urlConnection.disconnect();
            //endregion

            JSONObject jsonResponse = new JSONObject();
            if(code >199 || code < 300) {
                try {
                    jsonResponse.put("message","Token Created!");
                    jsonResponse.put("context",context);
                    jsonResponse.put("code",code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Calendar tokenDate = Calendar.getInstance();
                Date date = new Date();
                tokenDate.setTime(date);
                tokenDate.add(Calendar.DATE,1);
                date = tokenDate.getTime();
                SharedPref.setUser(context,user.username,user.password,date,accessToken);
                return jsonResponse;
            }
            else{
                try {
                    jsonResponse.put("message","Couldnt get token!");
                    jsonResponse.put("code",code);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return json;
            }

        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
