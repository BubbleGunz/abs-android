package group9.android_project;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fetes on 2015-10-20.
 */
public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);
        final Context context = this;

        TextView labelUsername = (TextView)findViewById(R.id.labelHomeUsername);



        boolean isTokenValid = TokenHandler.checkToken(this);
        User user = SharedPref.GetUserPw(this);


        //Checking token: If token not valid - try to refresh token with savedprefs, else send to loginscreen
        //region CHECKING TOKEN
        if(!isTokenValid)
        {
            AsyncCallInfo info = new AsyncCallInfo();
            info.command = "GetToken";
            info.user = user;
            info.context = context;
            AsyncCall asc = new AsyncCall(){
                @Override
                protected void onPostExecute(JSONObject jsonObject) {

                    try {
                        int code = (int)jsonObject.get("code");
                        String ResponseMsg = (String)jsonObject.get("message");
                        if(code == 200) {
                            Toast.makeText(HomeActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(HomeActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                            SharedPref.clearPrefs(context);
                            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asc.execute(info);
        }
        //endregion

        labelUsername.setText(user.username);


        Button btnFriends = (Button)findViewById(R.id.btnHomeFriends);
        btnFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FriendsActivity.class));
            }
        });

    }

}
