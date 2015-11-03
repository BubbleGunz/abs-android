package group9.android_project;

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
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.Logger;

/**
 * Created by Fetes on 2015-10-20.
 */
public class LoginActivity extends AppCompatActivity {

    Context c;

    EditText usernameInput;
    EditText passwordInputInput;
    Button btnLoginLogin;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);

        usernameInput = (EditText) findViewById(R.id.usernameInput);
        passwordInputInput = (EditText) findViewById(R.id.passwordInput);
        btnLoginLogin = (Button)findViewById(R.id.btnLoginLogin);


        btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String username = usernameInput.getText()+"";
                String password = usernameInput.getText()+"";

                if(username.length() == 0 ||password.length() == 0)
                {
                    Toast.makeText(LoginActivity.this, "Please fill in username and password!", Toast.LENGTH_SHORT).show();
                    return;
                }
               // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            }
        });

        Button btnCreateAcc = (Button)findViewById(R.id.btnLoginCreateAcc);
        btnCreateAcc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccActivity.class));
            }
        });
    }




}
