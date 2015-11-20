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

    TextView tvUsername;
    TextView tvName;
    TextView tvVacation;
    TextView tvMemory;
    TextView tvSlash;
    Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_layout);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        tvName = (TextView)findViewById(R.id.tvName);
        tvVacation = (TextView)findViewById(R.id.tvVacation);
        tvSlash = (TextView)findViewById(R.id.tvSlash);
        tvMemory = (TextView)findViewById(R.id.tvMemory);

        tvVacation.setVisibility(View.INVISIBLE);
        tvSlash.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);


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

        tvUsername.setText(profileUser.username);
        tvName.setText(profileUser.firstname + " "+ profileUser.lastname);

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
        tvVacation.setVisibility(View.INVISIBLE);
        tvSlash.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);

        // Create the adapter to convert the array to views
        CustomVacationsAdapter adapter = new CustomVacationsAdapter(this, vacationsArraylist);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Vacation vacation = (Vacation) adapter.getItemAtPosition(position);
                tvVacation.setText(vacation.title);
                tvVacation.setVisibility(View.VISIBLE);
                //Getting memories: Fetching the memories from the clicked vacation
                //region GETTING MEMORIES


                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "GetMemories";
                info.vacation = vacation;
                info.context = context;

                AsyncCall asc = new AsyncCall(){
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        try {
                            int code = (int)jsonObject.get("code");
                            if(code == 200) {
                                ArrayList<Memory> memoriesList = (ArrayList<Memory>)jsonObject.get("memories");
                                populateMemoriesList(memoriesList);
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
        });

        listView.setAdapter(adapter);
    }
    //Fill the listview with Memories
    private void populateMemoriesList(ArrayList<Memory> memoriesArrayList) {
        // Create the adapter to convert the array to views
        CustomMemoriesAdapter adapter = new CustomMemoriesAdapter(this, memoriesArrayList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Memory memory = (Memory) adapter.getItemAtPosition(position);
                tvSlash.setVisibility(View.VISIBLE);
                tvMemory.setText(memory.title);
                tvMemory.setVisibility(View.VISIBLE);
                //Getting media: Fetching media from the clicked Memory
                //region GETTING MEDIA

                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "GetMedia";
                info.memory = memory;
                info.context = context;

                AsyncCall asc = new AsyncCall(){
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        try {
                            int code = (int)jsonObject.get("code");
                            if(code == 200) {
                                ArrayList<Media> mediaList = (ArrayList<Media>)jsonObject.get("media");
                                populateMediaList(mediaList);
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
        });

        listView.setAdapter(adapter);
    }
    //Fill the listview with Media
    private void populateMediaList(ArrayList<Media> mediaArrayList) {

        // Create the adapter to convert the array to views
        CustomMediaAdapter adapter = new CustomMediaAdapter(this, mediaArrayList);
        // Attach the adapter to a ListView
        ListView listView = (ListView) findViewById(R.id.lvItems);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                Memory memory = (Memory) adapter.getItemAtPosition(position);
                //Getting media: Fetching media from the clicked Memory
                //region GETTING MEDIA

                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "GetMedia";
                info.memory = memory;
                info.context = context;

                AsyncCall asc = new AsyncCall(){
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        try {
                            int code = (int)jsonObject.get("code");
                            if(code == 200) {
                                ArrayList<Media> mediaList = (ArrayList<Media>)jsonObject.get("media");
                                populateMediaList(mediaList);
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
        });

        listView.setAdapter(adapter);
    }

}
