package group9.android_project;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * Created by Fetes on 2015-11-16.
 */
public class PopupAddFriend extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addfriend_layout);
        DisplayMetrics dm =  new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height  = dm.heightPixels;
        getWindow().setLayout((int)(width*0.8),(int)(height*.6));
    }
}
