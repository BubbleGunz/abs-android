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
 * Created by Halling- on 2015-11-24.
 */
public class MyUserProfileActivity extends AppCompatActivity {

    TextView tvUsername;
    TextView tvVacation;
    TextView tvMemory;
    TextView tvSlash;
    Button btnDelete;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_layout);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvVacation = (TextView) findViewById(R.id.tvVacation);
        tvSlash = (TextView) findViewById(R.id.tvSlash);
        tvMemory = (TextView) findViewById(R.id.tvMemory);
        tvVacation.setVisibility(View.INVISIBLE);
        tvSlash.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);

        boolean isTokenValid = TokenHandler.checkToken(this);
        //Checking token: If token not valid - try to refresh token with savedprefs, else send to loginscreen
        //region CHECKING TOKEN
        if (!isTokenValid) {
            User user = SharedPref.GetUserPw(this);
            AsyncCallInfo info = new AsyncCallInfo();
            info.command = "GetToken";
            info.user = user;
            info.context = context;
            AsyncCall asc = new AsyncCall() {
                @Override
                protected void onPostExecute(JSONObject jsonObject) {

                    try {
                        int code = (int) jsonObject.get("code");
                        String ResponseMsg = (String) jsonObject.get("message");
                        if (code == 200) {
                            Toast.makeText(MyUserProfileActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(MyUserProfileActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                            SharedPref.clearPrefs(context);
                            startActivity(new Intent(MyUserProfileActivity.this, LoginActivity.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asc.execute(info);
        }
        //endregion

        tvUsername.setText(SharedPref.GetUsername(context));


    }


}
