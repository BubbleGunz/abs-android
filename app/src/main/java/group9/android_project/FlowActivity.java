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
public class FlowActivity extends AppCompatActivity
{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        Context context = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flow_layout);



    }
}
