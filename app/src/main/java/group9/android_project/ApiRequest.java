package group9.android_project;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.session.MediaSession;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.JsonReader;
import android.util.Log;
import android.widget.Toast;

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
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Benjamin on 2015-11-05.
 */
public class ApiRequest {
    public Context context;
    public static final String PREFS_NAME="USER_PREFS";

    //POST -------- Return JsonObj with message and code
    public static JSONObject CreateUser(JSONObject user){
        JSONObject jsonReturn = new JSONObject();
        HttpURLConnection urlConnection = null;
        //region CONNECTION
        try
        {
            URL url = new URL("http://www.abs-cloud.elasticbeanstalk.com/api/v1/accounts/");
            urlConnection = (HttpURLConnection) url.openConnection();
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
            urlConnection.disconnect();
            //endregion
            if(code == 200) {
                jsonReturn.put("code", code);
            }
            else{
                jsonReturn.put("code", code);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //POST --------Return a JsonObj with token,code,message && Saves userinfo in sharedprefs
    public static JSONObject GetToken(User user,Context context){
        JSONObject jsonReturn = new JSONObject();
        HttpURLConnection urlConnection = null;
        //region CONNECTION
        try
        {
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/token");
            urlConnection = (HttpURLConnection) url.openConnection();
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
                jsonReturn = new JSONObject(sb.toString());
                String accessToken = jsonReturn.getString("access_token");

                Calendar tokenDate = Calendar.getInstance();
                Date date = new Date();
                tokenDate.setTime(date);
                tokenDate.add(Calendar.DATE, 1);
                date = tokenDate.getTime();
                SharedPref.setUser(context, user.username, user.password, date, accessToken);

                jsonReturn = new JSONObject();
                jsonReturn.put("code", code);
                jsonReturn.put("token", accessToken);

            }
            else{
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

                JSONObject jsonResponse = new JSONObject(sb.toString());
                String error = jsonResponse.getString("error_description");
                jsonReturn = new JSONObject();
                jsonReturn.put("message", error);
                jsonReturn.put("code", code);

            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion

    }

    //GET --------Returns JsonObject the friendlist of the user
    public static JSONObject GetFriends(User user,Context context){
        JSONObject jsonReturn = new JSONObject();
        ArrayList<User> friendList = new ArrayList<User>();
        HttpURLConnection urlConnection = null;

        //region CONNECTION
        try
        {

            User myUser = SharedPref.GetTokenInfo(context);
            myUser.username = SharedPref.GetUsername(context);
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/users/"+myUser.username+"/friends");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Authorization", "bearer " + myUser.token);

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

                JSONArray jsonResponse = new JSONArray(sb.toString());
                for (int i = 0 ; i<jsonResponse.length();i++) {
                    JSONObject friendJson =  jsonResponse.getJSONObject(i);
                    User friend = new User();
                    friend.firstname = friendJson.getString("firstname");
                    friend.lastname = friendJson.getString("lastname");
                    friend.username = friendJson.getString("username");

                    friendList.add(friend);
                }
                jsonReturn.put("friends",friendList);
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
            jsonReturn.put("code",code);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //POST -------- Returns JsonObject with code and message
    public static JSONObject AddFriend(User searchedUser,Context context){
        JSONObject jsonReturn = new JSONObject();
        HttpURLConnection urlConnection = null;

        try
        {
            //region CONNECTION
            User myUser = SharedPref.GetTokenInfo(context);
            myUser.username = SharedPref.GetUsername(context);
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/users/"+myUser.username+"/friends");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Accept", "application/json");
            urlConnection.addRequestProperty("Authorization","bearer " +myUser.token);

            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write("username=" + searchedUser.username);
            osw.close();
            os.close();

            int code = urlConnection.getResponseCode();

            StringBuilder sb = new StringBuilder();
            if(code == 204) {
                jsonReturn.put("code", code);

            }
            else{
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();
                JSONObject jsonResponse = new JSONObject(sb.toString());
                jsonReturn.put("code", code);
            }
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //GET --------Returns JsonObject with the choosen user's vacations
    public static JSONObject GetVacations(User user,Context context){
        JSONObject jsonReturn = new JSONObject();
        ArrayList<Vacation> vacationList = new ArrayList<Vacation>();
        HttpURLConnection urlConnection = null;
        try
        {
            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/users/"+user.username+"/vacations");
            User myUser = SharedPref.GetTokenInfo(context);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Authorization", "bearer " + myUser.token);

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

                JSONArray jsonResponse = new JSONArray(sb.toString());
                for (int i = 0 ; i<jsonResponse.length();i++) {
                    JSONObject vacationJson =  jsonResponse.getJSONObject(i);
                    Vacation vacation = new Vacation();
                    vacation.id = vacationJson.getInt("id");
                    vacation.title = vacationJson.getString("title");
                    vacation.description = vacationJson.getString("description");
                    vacation.place = vacationJson.getString("place");
                    vacation.end = vacationJson.getInt("end");
                    vacation.start = vacationJson.getInt("start");

                    vacationList.add(vacation);
                }
                jsonReturn.put("vacations",vacationList);
                jsonReturn.put("code",code);
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
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //GET --------Returns JsonObject with the choosen user's memories
    public static JSONObject GetMemories(Vacation vacation,Context context){
        JSONObject jsonReturn = new JSONObject();
        ArrayList<Memory> memoryArrayList = new ArrayList<Memory>();
        HttpURLConnection urlConnection = null;
        try
        {
            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/vacations/"+vacation.id+"/memories");
            User myUser = SharedPref.GetTokenInfo(context);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Authorization", "bearer " + myUser.token);

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

                JSONArray jsonResponse = new JSONArray(sb.toString());
                for (int i = 0 ; i<jsonResponse.length();i++) {
                    JSONObject memoryJson =  jsonResponse.getJSONObject(i);
                    Memory memory = new Memory();
                    memory.id = memoryJson.getInt("id");
                    memory.title = memoryJson.getString("title");
                    memory.description = memoryJson.getString("description");
                    memory.place = memoryJson.getString("place");
                    memory.time = memoryJson.getInt("time");
                    JSONObject position = memoryJson.getJSONObject("position");
                    Position p = new Position();
                    p.latitude = (float)position.getDouble("latitude");
                    p.longitude = (float)position.getDouble("longitude");
                    memory.position = p;

                    memoryArrayList.add(memory);
                }
                jsonReturn.put("memories",memoryArrayList);
                jsonReturn.put("code",code);
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
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //GET --------Returns JsonObject with the choosen user's media
    public static JSONObject GetMedia(Memory memory,Context context){
        JSONObject jsonReturn = new JSONObject();
        ArrayList<Media> mediaArrayList = new ArrayList<Media>();
        HttpURLConnection urlConnection = null;

        try
        {
            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/memories/"+memory.id+"/media-objects");
            User myUser = SharedPref.GetTokenInfo(context);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Authorization", "bearer " + myUser.token);

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
                urlConnection.disconnect();

                JSONArray jsonResponse = new JSONArray(sb.toString());
                for (int i = 0 ; i<jsonResponse.length();i++) {
                    JSONObject mediaJson =  jsonResponse.getJSONObject(i);
                    final Media media = new Media();
                    final PictureMedia pMedia = new PictureMedia();
                    media.id = mediaJson.getInt("id");
                    media.fileURL = mediaJson.getString("fileurl");
                    media.container = mediaJson.getString("container");
                    if(media.container.contains("image"))
                    {
                        pMedia.width = mediaJson.getInt("width");
                        pMedia.height = mediaJson.getInt("height");

                        //Getting bitmap: fethcing the bitmaps from the media
                        //region GETTING BITMAPS
                        if(media.fileURL != null) {
                            try {
                                Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(media.fileURL).getContent());
                                media.bitmap = bitmap;
                            }
                            catch (IOException e)
                            {
                                e.printStackTrace();
                            }
                        }
                        //endregion

                    }

                    mediaArrayList.add(media);
                }
                jsonReturn.put("media",mediaArrayList);
                jsonReturn.put("code",code);
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
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //GET --------Fetching the images
    public static JSONObject GetBitmap(String fileUrl,Context context){
        JSONObject jsonReturn = new JSONObject();
        HttpURLConnection urlConnection = null;
            try {
                    //region CONNECTION
                    URL url = new URL(fileUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoInput(true);
                    urlConnection.setUseCaches(false);

                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        InputStream input = urlConnection.getInputStream();
                        Bitmap myBitmap = BitmapFactory.decodeStream(input);

                        jsonReturn.put("bitmap", myBitmap);
                    } else {
                        BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;

                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();
                    }
                    jsonReturn.put("code", code);

            }



        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //GET --------Remove friend to the user
    public static JSONObject RemoveFriend(User user,Context context){
        JSONObject jsonReturn = new JSONObject();
        HttpURLConnection urlConnection = null;
        String myUser = SharedPref.GetUsername(context);
        try {
            //region CONNECTION
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/users/" + myUser + "/friends/" + user.username);
            urlConnection = (HttpURLConnection) url.openConnection();
            User myToken = SharedPref.GetTokenInfo(context);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("DELETE");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            urlConnection.addRequestProperty("Authorization", "bearer " + myToken.token);

            int code = urlConnection.getResponseCode();
            StringBuilder sb = new StringBuilder();
            if (code == 204) {

                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                br.close();

            }
            jsonReturn.put("code", code);
        }



        catch (JSONException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //POST --------Add a vacation to the user
    public static JSONObject AddVacation(Vacation vacation, Context context){
        JSONObject jsonReturn = new JSONObject();
        User myUser = SharedPref.GetTokenInfo(context);
        HttpURLConnection urlConnection = null;
        //region CONNECTION
        try
        {
            URL url = new URL("http://www.abs-cloud.elasticbeanstalk.com/api/v1/vacations/");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Authorization","bearer " +myUser.token);
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write("title=" + vacation.title + "&description=" + vacation.description + "&place=" + vacation.place + "&start=" + vacation.start + "&end=" + vacation.end);
            osw.close();
            os.close();

            int code = urlConnection.getResponseCode();
            urlConnection.disconnect();
            //endregion
            jsonReturn.put("code", code);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }

    //POST --------Add a vacation to the user
    public static JSONObject AddMemory(Memory memory,Vacation vacation, Context context){
        JSONObject jsonReturn = new JSONObject();
        User myUser = SharedPref.GetTokenInfo(context);
        HttpURLConnection urlConnection = null;
        //region CONNECTION
        try
        {
            URL url = new URL("http://www.abs-cloud.elasticbeanstalk.com/api/v1/vacations/"+vacation.id+"/memories");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            urlConnection.addRequestProperty("Authorization","bearer " +myUser.token);
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);
            osw.write("title=" + memory.title+"&description="+memory.description+"&place="+memory.place+"&time="+memory.time+"&position="+memory.position);
            osw.close();
            os.close();

            int code = urlConnection.getResponseCode();
            urlConnection.disconnect();
            //endregion
            jsonReturn.put("code", code);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            return jsonReturn;

        }
        //endregion
    }



}
