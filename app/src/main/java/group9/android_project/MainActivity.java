package group9.android_project;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TabHost;

import javax.sql.StatementEvent;

/**
 * Created by Halling- on 2015-11-24.
 */
public class MainActivity extends TabActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        Resources res = getResources();

        // create the TabHost that will contain the Tabs
        final TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");
        TabHost.TabSpec tab3 = tabHost.newTabSpec("Third Tab");
        TabHost.TabSpec tab4 = tabHost.newTabSpec("Fourth Tab");

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
        tab1.setIndicator("",
                res.getDrawable(android.R.drawable.ic_menu_gallery));
        Intent flowIntent = new Intent(this, FlowActivity.class);
        tab1.setContent(flowIntent);
        tabHost.addTab(tab1);

        tab2.setIndicator("",
                res.getDrawable(android.R.drawable.btn_star_big_on));
        Intent vacationsIntent = new Intent(MainActivity.this, UserProfileActivity.class);
        User user = new User();
        user.username = SharedPref.GetUsername(context);
        vacationsIntent.putExtra("userObject", user);
        tab2.setContent(vacationsIntent);
        tabHost.addTab(tab2);

        tab3.setIndicator("",
                res.getDrawable(android.R.drawable.btn_star_big_on));
        Intent friendsIntent = new Intent(this, FriendsActivity.class);
        tab3.setContent(friendsIntent);
        tabHost.addTab(tab3);

        tab4.setIndicator("",
                res.getDrawable(android.R.drawable.btn_star_big_on));
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        tab4.setContent(settingsIntent);
        tabHost.addTab(tab4);

    }
}
