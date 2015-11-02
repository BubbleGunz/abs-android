package group9.android_project;


import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        List<String> testList = new ArrayList<String>();
        String s1 ="Magnus";
        String s2 ="Sven";
        String s3 ="Josef";
        testList.add(s1);
        testList.add(s2);
        testList.add(s3);
        testList.add(s3);
        testList.add(s3);
        testList.add(s3);
        testList.add(s3);
        testList.add(s3);
        testList.add(s3);
        LinearLayout ll = (LinearLayout) findViewById(R.id.friendsLayout);

        for (int i = 0; i < testList.size(); i++) {
            Button btn = new Button(this);
            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            Resources r = getResources();
            float pxHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
            int px = Math.round(pxHeight);
            btn.setHeight(px);
            btn.setId(i);
            btn.setText(testList.get(i));

            btn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Toast.makeText(FriendsActivity.this, "Button " + v.getId() + " clicked", Toast.LENGTH_SHORT).show();

                }
            });

            ll.addView(btn);

        }

    }

}
