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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        final Context context = this;

        boolean isTokenValid = TokenHandler.checkToken(this);
        User user = new User();
        user = SharedPref.GetTokenInfo(context);
        user.username = SharedPref.GetUsername(context);

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
                        LinearLayout ll = (LinearLayout) findViewById(R.id.friendsLayout);
                        for (int i = 1; i < jsonArray.length(); i++) {
                            User friend = new User();
                            friend = (User)jsonArray.get(i);
                            Button btn = new Button(context);
                            btn.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.WRAP_CONTENT));
                            Resources r = getResources();
                            float pxHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80, r.getDisplayMetrics());
                            int px = Math.round(pxHeight);
                            btn.setHeight(px);
                            btn.setId(i);
                            btn.setText(friend.username);

                            btn.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(FriendsActivity.this, "Button " + v.getId() + " clicked", Toast.LENGTH_SHORT).show();

                                }
                            });

                            ll.addView(btn);


                        }
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

}
