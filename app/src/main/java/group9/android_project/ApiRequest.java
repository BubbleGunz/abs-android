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

    //Return JsonObj with message and code
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
            if(code == 200) {
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

    //Returns a JsonObj with token,code,message && Saves userinfo in sharedprefs
    public static JSONObject GetToken(User user,Context context){
        JSONObject json = new JSONObject();
        try
        {
            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/token");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Accept", "application/json");
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write("grant_type=password&username=" + user.username + "&password=" + user.password);
            osw.close();
            os.close();


            int code = urlConnection.getResponseCode();
            StringBuilder sb = new StringBuilder();
            if(code == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                sb = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                try {
                    json = new JSONObject(sb.toString());
                    String accessToken = json.getString("access_token");

                    Calendar tokenDate = Calendar.getInstance();
                    Date date = new Date();
                    tokenDate.setTime(date);
                    tokenDate.add(Calendar.DATE, 1);
                    date = tokenDate.getTime();
                    SharedPref.setUser(context, user.username, user.password, date, accessToken);

                    json = new JSONObject();
                    json.put("message", "Token Created!");
                    json.put("code", code);
                    json.put("token", accessToken);

                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                    return json;

                }
            }
            else{
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                try {
                    json = new JSONObject(sb.toString());
                    String error = json.getString("error_description");
                    json = new JSONObject();
                    json.put("message", error);
                    json.put("code", code);

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    urlConnection.disconnect();
                    return json;

                }
            }
            //endregion

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return json;
    }

    //Returns the users friendlist in a JsonArary
    public static JSONArray GetFriends(User user,Context context){
        JSONArray jsonArray = new JSONArray();
        try
        {

            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/users/"+user.username+"/friends");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Authorization", "bearer " + user.token);

            int code = urlConnection.getResponseCode();
            StringBuilder sb = new StringBuilder();
            if(code == 200) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                sb = new StringBuilder();

                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                    Log.d("Läser line", "körs");

                }
                br.close();
                try {
                    JSONArray json = new JSONArray(sb.toString());
                    JSONObject code2 = new JSONObject();
                    code2.put("code",code);
                    jsonArray.put(code2);
                    for (int i = 0 ; i<json.length();i++) {
                        JSONObject friendUserJson =  json.getJSONObject(i);
                        User friendUser = new User();
                        friendUser.username = friendUserJson.getString("username");
                        friendUser.firstname = friendUserJson.getString("firstname");
                        friendUser.lastname = friendUserJson.getString("lastname");
                        friendUser.email = friendUserJson.getString("email");

                        jsonArray.put(friendUser);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else{
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

            }

                try {



                }finally {
                    urlConnection.disconnect();
                    return jsonArray;

                }

            //endregion

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    //Add friend , Returns code and message in JsonObject
    public static JSONObject SetFriend(User searcheduser,Context context){
        JSONObject json = new JSONObject();
        try
        {
            //region CONNECTION
            User myUser = SharedPref.GetTokenInfo(context);
            myUser.username = SharedPref.GetUsername(context);
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/users/"+myUser.username+"/friends");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("Authorization","bearer " +myUser.token);

            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write("username="+searcheduser.username);
            osw.close();
            os.close();

            int code = urlConnection.getResponseCode();
            StringBuilder sb = new StringBuilder();
            if(code == 204) {
                try {
                    json.put("message", searcheduser.username+" added to friendlist!");
                    json.put("code", code);
                } catch (JSONException e) {
                    e.printStackTrace();
                } finally {
                    urlConnection.disconnect();
                    return json;

                }
            }
            else{
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                JSONObject jsonResponse = new JSONObject();
                try {
                    json = new JSONObject(sb.toString());
                    String error = json.getString("message");
                    jsonResponse.put("message", error);
                    jsonResponse.put("code", code);

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    urlConnection.disconnect();
                    return jsonResponse;

                }
            }
            //endregion

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return json;
    }



}
