package group9.android_project;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Halling- on 2015-11-28.
 */
public class SettingsActivity extends AppCompatActivity {

    Button btnApplyChanges, btnLogout, btnDeleteAccount;
    EditText etFirstname,etLastname, etEmail;
    Context context = this;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_layout);

        etFirstname = (EditText)findViewById(R.id.etFirstname);
        etLastname = (EditText)findViewById(R.id.etLastname);
        etEmail = (EditText)findViewById(R.id.etEmail);

        btnApplyChanges = (Button)findViewById(R.id.btnApplyChanges);
        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnDeleteAccount = (Button)findViewById(R.id.btnDeleteAccount);


        boolean isTokenValid = TokenHandler.checkToken(this);
        User user = SharedPref.GetUserPw(this);
        //Checking token: If token not valid - try to refresh token with savedprefs, else send to loginscreen
        //region CHECKING TOKEN
        if (!isTokenValid) {
            AsyncCallInfo info = new AsyncCallInfo();
            info.command = "GetToken";
            info.user = user;
            info.context = context;
            AsyncCall asc = new AsyncCall() {
                @Override
                protected void onPostExecute(JSONObject jsonObject) {

                    try {
                        int code = (int) jsonObject.get("code");
                        String ResponseMsg = (String) jsonObject.get("message");
                        if (code == 200) {
                            Toast.makeText(SettingsActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(SettingsActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                            SharedPref.clearPrefs(context);
                            startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
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
        final User profileUser = (User) i.getSerializableExtra("user");

        etFirstname.setText(profileUser.firstname);
        etLastname.setText(profileUser.lastname);
        etEmail.setText(profileUser.email);

        final User userToEdit = (User) i.getSerializableExtra("user");

        btnApplyChanges.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //region APPLYCHANGES

                final AsyncCallInfo info = new AsyncCallInfo();

                userToEdit.firstname = etFirstname.getText().toString().trim();
                userToEdit.lastname = etLastname.getText().toString();
                userToEdit.email = etEmail.getText().toString();
                info.user = userToEdit;
                info.command = "PatchUser";
                info.context = context;
                AsyncCall asc = new AsyncCall() {
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        try {
                            int code = (int) jsonObject.get("code");
                            //String responseMsg = (String)jsonObject.get("message");

                            if (code == 204) {
                                Toast.makeText(SettingsActivity.this, info.user.username + " changed!", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(SettingsActivity.this, MainActivity.class);
                                SettingsActivity.this.finish();
                                i.putExtra("whichtab", 0);
                                startActivity(i);
                            }
                            else {
                                Toast.makeText(SettingsActivity.this, code + " - someting went wrong!", Toast.LENGTH_SHORT).show();

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

        btnLogout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SharedPref.clearPrefs(context);
                startActivity(new Intent(SettingsActivity.this, LoginActivity.class));
            }
        });
        btnDeleteAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.remove_layout);
                TextView tvRemoveTitle,tvRemoveInfo;

                tvRemoveTitle = (TextView)dialog.findViewById(R.id.tvRemoveTitle);
                tvRemoveInfo = (TextView)dialog.findViewById(R.id.tvRemoveInfo);

                tvRemoveInfo.setText("Are you sure you want to delete your account?");
                tvRemoveTitle.setText("Delete Account");
                dialog.show();
                Button btnCancel;
                btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        dialog.dismiss();
                    }
                });
                Button btnDiaDeleteAccount;
                btnDiaDeleteAccount = (Button) dialog.findViewById(R.id.btnconfirmRemove);
                btnDiaDeleteAccount.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        //Delete Account: Delete an account
                        //region DELETE ACCOUNT
                        AsyncCallInfo info = new AsyncCallInfo();
                        info.command = "DeleteAccount";
                        info.user = profileUser;
                        info.context = context;
                        AsyncCall asc = new AsyncCall() {
                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {

                                try {
                                    int code = (int) jsonObject.get("code");
                                    if (code == 204) {
                                        Toast.makeText(context, profileUser.username + " - account deleted", Toast.LENGTH_SHORT).show();
                                        SharedPref.clearPrefs(context);
                                        startActivity(new Intent(context, LoginActivity.class));
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










    }
}
