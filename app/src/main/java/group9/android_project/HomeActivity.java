package group9.android_project;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Fetes on 2015-10-20.
 */
public class HomeActivity extends AppCompatActivity{

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_layout);

        TextView labelUsername = (TextView)findViewById(R.id.labelHomeUsername);

        User user = SharedPref.GetUserPw(this);
        labelUsername.setText(user.username);

        boolean validToken = CheckValidToken.checkToken(this);
        if(!validToken)
        {
            CheckValidToken.RefreshToken(this);// FRÅGA HUR VI FÅR TILLBAKA RESPONSEN FRÅN DOINBACKGROUND
            startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        }


        Button btnFriends = (Button)findViewById(R.id.btnHomeFriends);
        btnFriends.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, FriendsActivity.class));
            }
        });

    }

}
