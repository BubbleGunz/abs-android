package group9.android_project;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.Buffer;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Fetes on 2015-11-02.
 */
public class CreateAccActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText confirmedPassword;
    EditText firstname;
    EditText lastname;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_acc_layout);

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        confirmedPassword = (EditText) findViewById(R.id.etConfirm);
        firstname = (EditText) findViewById(R.id.etLastname);
        lastname = (EditText) findViewById(R.id.etUsername);
        email = (EditText) findViewById(R.id.etEmail);


        Button btnCreate = (Button)findViewById(R.id.btnCreateAccCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


               /* if(username.getText() == null || password.getText() == null || confirmedPassword.getText() == null || firstname.getText() == null || lastname.getText()== null || email.getText() == null)
                {
                    Toast.makeText(CreateAccActivity.this, "Please fill all input fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText() != confirmedPassword.getText())
                {
                    Toast.makeText(CreateAccActivity.this, "Password doesnt match!", Toast.LENGTH_SHORT).show();
                    return;
                }
*/
                JSONObject userObj = new JSONObject();
                try {
                    userObj.put("username",username.getText().toString().trim());
                    userObj.put("password", password.getText().toString().trim());
                    userObj.put("confirmpassword", password.getText().toString().trim());;
                    userObj.put("firstname", firstname.getText().toString().trim());
                    userObj.put("lastname", lastname.getText().toString().trim());
                    userObj.put("email", email.getText().toString().trim());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("DEBUG",userObj.toString());

            }
        });
        Button btnBack = (Button)findViewById(R.id.btnCreateAccBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(CreateAccActivity.this, LoginActivity.class));
            }
        });

    }

    private void createAccount()
    {
        try{
            URL url = new URL("http://abs-cloud.elasticbeanstalk.com/api/v1/accounts");
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);
            connection.setChunkedStreamingMode(0);

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());


            int responseCode = connection.getResponseCode();
            String output = "Request URL " + url;
            output += System.getProperty("line.seperator") + "Request Parameters ";
            output += System.getProperty("line.seperator") + "Response Code " + responseCode;

            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line = "";
            StringBuilder responseOutput = new StringBuilder();

            while((line = br.readLine()) != null){
                responseOutput.append(line);
            }
            br.close();

            output += System.getProperty("line.seperator") + responseOutput.toString();


        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


}
