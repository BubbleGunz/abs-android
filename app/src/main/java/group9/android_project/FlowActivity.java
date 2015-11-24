package group9.android_project;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TabHost;

import junit.framework.Test;

/**
 * Created by Halling- on 2015-11-24.
 */
public class FlowActivity extends TabActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_layout);

        // create the TabHost that will contain the Tabs
        TabHost tabHost = (TabHost)findViewById(android.R.id.tabhost);


        TabHost.TabSpec tab1 = tabHost.newTabSpec("First Tab");
        TabHost.TabSpec tab2 = tabHost.newTabSpec("Second Tab");

        // Set the Tab name and Activity
        // that will be opened when particular Tab will be selected
        tab1.setIndicator("Users");
        tab1.setContent(new Intent(this, Test.class));

        tab2.setIndicator("Friends");
        tab2.setContent(new Intent(this, FriendsActivity.class));


        /** Add the tabs  to the TabHost to display. */
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

    }
}
