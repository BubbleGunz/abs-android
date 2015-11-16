package group9.android_project;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    Button btnAddFriend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        final Context context = this;

        boolean isTokenValid = TokenHandler.checkToken(this);
        User user = new User();
        user = SharedPref.GetTokenInfo(context);
        user.username = SharedPref.GetUsername(context);
        btnAddFriend = (Button)findViewById(R.id.btnFriendsAddFriends);

        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(FriendsActivity.this, PopupAddFriend.class));
            }
        });
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
                            Toast.makeText(FriendsActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        else{
                            Toast.makeText(FriendsActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                            SharedPref.clearPrefs(context);
                            startActivity(new Intent(FriendsActivity.this, LoginActivity.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asc.execute(info);
        }
        //endregion


        AsyncCallInfo info = new AsyncCallInfo();
        info.command = "GetFriends";
        info.user = user;
        info.context = context;





        AsyncCallArray asc = new AsyncCallArray(){
            @Override
            protected void onPostExecute(JSONArray jsonArray) {

                try {
                    int code = (int)jsonArray.getJSONObject(0).get("code");
                    if(code == 200) {
                        ArrayList<User> friends = new ArrayList<User>();
                        for (int i = 1; i < jsonArray.length(); i++) {

                            User friend = new User();
                            friend = (User)jsonArray.get(i);
                            friends.add(friend);
                        }
                        populateUsersList(friends);
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




    }
    private void populateUsersList(ArrayList<User> friendsArraylist) {
        // Construct the data source
        //ArrayList<User> arrayOfUsers = User.setFriend(friendsArraylist);
        // Create the adapter to convert the array to views
        CustomUsersAdapter adapter = new CustomUsersAdapter(this, friendsArraylist);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvUsers);
        listView.setAdapter(adapter);
    }

}
