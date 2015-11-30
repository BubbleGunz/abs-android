package group9.android_project;


import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.addfriend_layout);
                dialog.show();
                Button btnDiaAddFriend;
                btnDiaAddFriend = (Button)dialog.findViewById(R.id.btnDiaAddFriend);
                btnDiaAddFriend.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        SearchView svFriend = (SearchView) dialog.findViewById(R.id.svFriend);
                        CharSequence svFriendCharSeq = svFriend.getQuery();
                        String svFriendString = svFriendCharSeq.toString();

                        //Add friend: Add a friend to the logged in user
                        //region ADD FRIEND
                        AsyncCallInfo info = new AsyncCallInfo();
                        final User searchedUser = new User();
                        searchedUser.username = svFriendString;
                        info.command = "AddFriend";
                        info.context = context;
                        info.user = searchedUser;

                        AsyncCall asc = new AsyncCall() {
                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {

                                try {
                                    int code = (int) jsonObject.get("code");
                                    //String responseMsg = (String)jsonObject.get("message");

                                    if (code == 204) {
                                        Toast.makeText(FriendsActivity.this, searchedUser.username + " added as friend!", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(FriendsActivity.this, MainActivity.class);
                                        i.putExtra("whichtab", 2);
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(FriendsActivity.this, code + " - User not found!", Toast.LENGTH_SHORT).show();

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        asc.execute(info);
                        //endregion
                    }

                });
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

        //Getting friends: Fetching the users friends
        //region GETTING FRIENDS
        AsyncCallInfo info = new AsyncCallInfo();
        info.command = "GetFriends";
        info.user = user;
        info.context = context;

        AsyncCall asc = new AsyncCall(){
            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    int code = (int)jsonObject.get("code");
                    if(code == 200) {
                        ArrayList<User> friendList = (ArrayList<User>)jsonObject.get("friends");
                        Collections.sort(friendList, new Comparator<User>() {
                                    @Override
                                    public int compare(User e1, User e2) {
                                        // ascending order
                                        return e1.firstname.compareTo(e2.firstname);

                                        // descending order
                                        //return id2.compareTo(id1);
                                    }
                                });
                            populateUsersList(friendList);
                    }
                    else{
                        return;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        asc.execute(info);
        //endregion



    }


    //Fill the listview with friends
    private void populateUsersList(ArrayList<User> friendsArraylist) {
        // Construct the data source
        //ArrayList<User> arrayOfUsers = User.setFriend(friendsArraylist);
        // Create the adapter to convert the array to views
        CustomUsersAdapter adapter = new CustomUsersAdapter(this, friendsArraylist);
        // Attach the adapter to a ListView
        final ListView listView = (ListView) findViewById(R.id.lvUsers);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                User user = (User) adapter.getItemAtPosition(position);
                Intent i = new Intent(FriendsActivity.this, UserProfileActivity.class);
                i.putExtra("userObject", user);
                startActivity(i);
            }

        });

        listView.setAdapter(adapter);
    }

}
