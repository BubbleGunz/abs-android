package group9.android_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fetes on 2015-11-19.
 */
public class UserProfileActivity extends AppCompatActivity{

    TextView tvVacationName;
    TextView tvUsername;
    TextView tvName;
    Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_layout);

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
                            Toast.makeText(UserProfileActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(UserProfileActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                            SharedPref.clearPrefs(context);
                            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asc.execute(info);
        }
        //endregion

        Intent i = getIntent();
        User profileUser = (User) i.getSerializableExtra("userObject");
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvUsername.setText(profileUser.username);
        tvUsername = (TextView)findViewById(R.id.tvName);
        tvUsername.setText(profileUser.firstname + " "+ profileUser.lastname);

        //Getting friends: Fetching the users friends
        //region GETTING FRIENDS
        AsyncCallInfo info = new AsyncCallInfo();
        info.command = "GetVacations";
        info.user = profileUser;
        info.context = context;

        AsyncCall asc = new AsyncCall(){
            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    int code = (int)jsonObject.get("code");
                    if(code == 200) {
                        ArrayList<Vacation> vacationList = (ArrayList<Vacation>)jsonObject.get("vacations");
                        populateVacationsList(vacationList);
                    }
                    else{
                        return;
                    }
                    /*String ResponseMsg = (String)jsonObject.get("message");
                    if(code == 200) {
                        Toast.makeText(FriendsActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else{
                        Toast.makeText(FriendsActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                        SharedPref.clearPrefs(context);
                        startActivity(new Intent(FriendsActivity.this, LoginActivity.class));
                    */

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        asc.execute(info);
        //endregion

    }



    //Fill the listview with vacations
    private void populateVacationsList(ArrayList<Vacation> vacationsArraylist) {
        // Construct the data source
        //ArrayList<User> arrayOfUsers = User.setFriend(friendsArraylist);
        // Create the adapter to convert the array to views
        CustomVacationsAdapter adapter = new CustomVacationsAdapter(this, vacationsArraylist);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvVacations);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Vacation vacation = (Vacation) adapter.getItemAtPosition(position);
                Intent i = new Intent(UserProfileActivity.this,UserProfileActivity.class);
                i.putExtra("vacationObject",vacation);
                startActivity(i);
            }
        });

        listView.setAdapter(adapter);
    }

}
