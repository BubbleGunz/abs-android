package group9.android_project;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Benjamin on 2015-11-05.
 */
public class ApiRequest {

    public static void CreateUser(JSONObject user){
        try
        {
            URL url = new URL("http://www.abs-cloud.elasticbeanstalk.com/api/v1/accounts/");
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.addRequestProperty("Content-Type", "application/json");
            OutputStream os = urlConnection.getOutputStream();
            OutputStreamWriter osw = new OutputStreamWriter(os);

            osw.write(user.toString());
            /*osw.write("{ " +
                        "\"username\": \"" + user.username + "\" , " +
                        "\"password\":\""+ user.password +"\" ," +
                        "\"confirmpassword\":\""+user.confirmpassword+"\" ," +
                        "\"firstname\":\""+user.firstname+"\"," +
                        "\"lastname\":\""+user.lastname+"\"," +
                        "\"email\":\""+user.email+"\"" +
                     "}");
                     */
            osw.close();
            os.close();

            int code = urlConnection.getResponseCode();

            //Scanner scan = new Scanner(urlConnection.getInputStream());
            Log.d("Code", ""+code);

            //Log.d("API", scan.nextLine());


            //scan.close();
            urlConnection.disconnect();
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }

       
    }
}
