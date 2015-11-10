package group9.android_project;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

/**
 * Created by Fetes on 2015-10-20.
 */
public class LoginActivity extends AppCompatActivity {

    Context c;

    EditText username;
    EditText password;
    Button btnLoginLogin;

    Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        username = (EditText) findViewById(R.id.etUsername);
        password = (EditText) findViewById(R.id.etPassword);
        btnLoginLogin = (Button)findViewById(R.id.btnLogin);



        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


               /* if(username.length() == 0 ||password.length() == 0)
                {
                    Toast.makeText(LoginActivity.this, "Please fill in username and password!", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "GetToken";
                info.token = GetLoginInfo();
                info.context = context;
                AsyncCall asc = new AsyncCall();
                asc.execute(info);
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });
        TextView signUp = (TextView)findViewById(R.id.tvSignUp);
        signUp.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccActivity.class));
            }
        });

    }
    public AccessToken GetLoginInfo(){
        AccessToken token = new AccessToken();
        token.username = username.getText().toString();
        token.password = password.getText().toString();


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
        return token;
    }




}
