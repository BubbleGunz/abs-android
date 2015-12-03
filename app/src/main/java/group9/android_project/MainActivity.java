package group9.android_project;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import javax.sql.StatementEvent;

/**
 * Created by Halling- on 2015-11-24.
 */
public class MainActivity extends TabActivity
{
    TextView tvName,tvUsername;
    Button btnSettings;
    SearchView svSearchMemory;
    User userInfo  = new User();


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Resources res = getResources();

        tvName = (TextView)findViewById(R.id.tvName);
        tvUsername = (TextView)findViewById(R.id.tvUsername);
        btnSettings = (Button)findViewById(R.id.btnSettings);
        svSearchMemory = (SearchView)findViewById(R.id.search_bar);


        //GetUserInfo: Get the information of an user
        //region GetUserInfo
        AsyncCallInfo info = new AsyncCallInfo();
        User myUser = SharedPref.GetTokenInfo(context);
        myUser.username = SharedPref.GetUsername(context);
        info.command = "GetUserInfo";
        info.context = context;
        info.user = myUser;



        AsyncCall asc = new AsyncCall() {
            @Override
            protected void onPostExecute(JSONObject jsonObject) {

                try {
                    int code = (int) jsonObject.get("code");
                    //String responseMsg = (String)jsonObject.get("message");

                    if (code == 200) {
                        userInfo = (User)jsonObject.get("user");
                        tvName.setText(userInfo.firstname+" "+userInfo.lastname);
                        tvUsername.setText(userInfo.username);
                        return;
                    } else {
                        Toast.makeText(MainActivity.this, code + " - User not found!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        };
        asc.execute(info);
        //endregion

        // create the TabHost that will contain the Tabs
        //region TAB
        final TabHost tabHost = getTabHost();

        User user = new User();
        user.username = SharedPref.GetUsername(context);

        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_directions))//ic_menu_home
                .setContent(new Intent(this, FlowActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));


        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_gallery))
                .setContent(new Intent(this, UserProfileActivity.class)
                        .putExtra("userObject", user)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));


        tabHost.addTab(tabHost.newTabSpec("tab3")
                .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_view))
                .setContent(new Intent(this, FriendsActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)));

        tabHost.addTab(tabHost.newTabSpec("tab4")
                .setIndicator("", res.getDrawable(android.R.drawable.ic_menu_camera))
                .setContent(new Intent(this, CameraActivity.class)));


        //Om vi vill ändra tabsens färg / bakgrund

        /*tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String arg0) {
                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i)
                        .setBackgroundResource(R.drawable.tabborder); // unselected
                }
                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab())
                        .setBackgroundResource(R.color.tabSelected); // unselected

            }
        });*/

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected


        Intent i = getIntent();
        final int tabActive = (int) i.getSerializableExtra("whichtab");
        if(tabActive > -1)
        {
            tabHost.setCurrentTab(tabActive);
        }


        //endregion
        btnSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                        Intent i = new Intent(MainActivity.this, SettingsActivity.class);
                        i.putExtra("user",userInfo);
                        startActivity(i);
            }
        });

        svSearchMemory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                svSearchMemory.setIconified(false);
            }
        });

    }

}
