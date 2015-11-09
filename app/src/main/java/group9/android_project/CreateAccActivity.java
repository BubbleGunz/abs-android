package group9.android_project;

import android.content.Intent;
import android.os.AsyncTask;
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
        btnCreate.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "CreateUser";
                info.userInfo = GetUserInfo();
                AsyncCall asc = new AsyncCall();
                asc.execute(info);

            }

        });
    }

    public JSONObject GetUserInfo(){
        JSONObject userObject = new JSONObject();
        try{
            userObject.put("username",username.getText().toString());
            userObject.put("password",password.getText().toString());
            userObject.put("confirmpassword",confirmedPassword.getText().toString());
            userObject.put("firstname",firstname.getText().toString());
            userObject.put("lastname",lastname.getText().toString());
            userObject.put("email",email.getText().toString());

        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        /*user.username = username.getText().toString();
        user.firstname = firstname.getText().toString();
        user.lastname = lastname.getText().toString();
        user.email = email.getText().toString();
        user.password = password.getText().toString();
        user.confirmpassword = confirmedPassword.getText().toString();

                       /* if(username.getText() == null || password.getText() == null || confirmedPassword.getText() == null || firstname.getText() == null || lastname.getText()== null || email.getText() == null)
                {
                    Toast.makeText(CreateAccActivity.this, "Please fill all input fields!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.getText() != confirmedPassword.getText())
                {
                    Toast.makeText(CreateAccActivity.this, "Password doesnt match!", Toast.LENGTH_SHORT).show();
                    return;
                }*/
        return userObject;
    }
}
