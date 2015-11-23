package group9.android_project;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
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
    Button btnDelete;
    Context context = this;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.user_layout);
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvName = (TextView) findViewById(R.id.tvName);
        tvVacation = (TextView) findViewById(R.id.tvVacation);
        tvSlash = (TextView) findViewById(R.id.tvSlash);
        tvMemory = (TextView) findViewById(R.id.tvMemory);
        tvVacation.setVisibility(View.INVISIBLE);
        tvSlash.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);

        boolean isUserMe = false;
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
                            Toast.makeText(UserProfileActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
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
        final User profileUser = (User) i.getSerializableExtra("userObject");

        tvUsername.setText(profileUser.username);
        tvName.setText(profileUser.firstname + " " + profileUser.lastname);

        btnDelete = (Button) findViewById(R.id.btnRemoveFriend);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.removefriend_layout);
                dialog.show();
                Button btnCancel;
                btnCancel = (Button) dialog.findViewById(R.id.btnCancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        dialog.dismiss();
                    }
                });
                Button btnDiaRemoveFriend;
                btnDiaRemoveFriend = (Button) dialog.findViewById(R.id.btnconfirmRemove);
                btnDiaRemoveFriend.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v2) {
                        AsyncCallInfo info = new AsyncCallInfo();
                        info.command = "RemoveFriend";
                        info.user = profileUser;
                        info.context = context;
                        AsyncCall asc = new AsyncCall() {
                            @Override
                            protected void onPostExecute(JSONObject jsonObject) {

                                try {
                                    int code = (int) jsonObject.get("code");
                                    if (code == 204) {
                                        Toast.makeText(context, profileUser.username + " removed from friends!", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(context, FriendsActivity.class));
                                    } else {

                                    }

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        };
                        asc.execute(info);
                    }
                });
            }
        });
        populateVacationsList(profileUser);
    }


        //Fill the listview with vacations

            private void populateVacationsList(final User profileUser) {

                //Getting vacations: Fetching the users vacations
                //region GETTING VACATIONS
                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "GetVacations";
                info.user = profileUser;
                info.context = context;

                AsyncCall asc = new AsyncCall() {
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        try {
                            int code = (int) jsonObject.get("code");
                            if (code == 200) {
                                ArrayList<Vacation> vacationList = (ArrayList<Vacation>) jsonObject.get("vacations");
                                tvVacation.setVisibility(View.INVISIBLE);
                                tvSlash.setVisibility(View.INVISIBLE);
                                tvMemory.setVisibility(View.INVISIBLE);


                                // Create the adapter to convert the array to views
                                CustomVacationsAdapter adapter = new CustomVacationsAdapter(context, vacationList);
                                // Attach the adapter to a ListView
                                GridView listView = (GridView) findViewById(R.id.gvItems);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                            long arg3) {
                                        Vacation vacation = (Vacation) adapter.getItemAtPosition(position);
                                        tvVacation.setText(vacation.title);
                                        tvVacation.setVisibility(View.VISIBLE);
                                        tvVacation.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                populateVacationsList(profileUser);
                                            }
                                        });
                                        populateMemoriesList(vacation);
                                    }
                                });

                                listView.setAdapter(adapter);
                            } else {
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

            //Fill the listview with Memories
            private void populateMemoriesList(final Vacation vacation) {
                tvSlash.setVisibility(View.INVISIBLE);
                tvMemory.setVisibility(View.INVISIBLE);

                //Getting memories: Fetching the memories from the clicked vacation
                //region GETTING MEMORIES


                AsyncCallInfo info = new AsyncCallInfo();
                info.command = "GetMemories";
                info.vacation = vacation;
                info.context = context;

                AsyncCall asc = new AsyncCall() {
                    @Override
                    protected void onPostExecute(JSONObject jsonObject) {

                        try {
                            int code = (int) jsonObject.get("code");
                            if (code == 200) {
                                ArrayList<Memory> memoriesList = (ArrayList<Memory>) jsonObject.get("memories");
                                // Create the adapter to convert the array to views
                                CustomMemoriesAdapter adapter = new CustomMemoriesAdapter(context, memoriesList);
                                // Attach the adapter to a ListView
                                GridView listView = (GridView) findViewById(R.id.gvItems);

                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                            long arg3) {
                                        Memory memory = (Memory) adapter.getItemAtPosition(position);
                                        tvSlash.setVisibility(View.VISIBLE);
                                        tvMemory.setText(memory.title);
                                        tvMemory.setVisibility(View.VISIBLE);
                                        tvMemory.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View view) {
                                                populateMemoriesList(vacation);
                                            }
                                        });


                                        //Getting media: Fetching media from the clicked Memory
                                        //region GETTING MEDIA

                                        AsyncCallInfo info = new AsyncCallInfo();
                                        info.command = "GetMedia";
                                        info.memory = memory;
                                        info.context = context;

                                        AsyncCall asc = new AsyncCall() {
                                            @Override
                                            protected void onPostExecute(JSONObject jsonObject) {

                                                try {
                                                    int code = (int) jsonObject.get("code");
                                                    if (code == 200) {
                                                        ArrayList<Media> mediaList = (ArrayList<Media>) jsonObject.get("media");
                                                        populateMediaList(mediaList);
                                                    } else {
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                };
                asc.execute(info);
                //endregion


            }

            //Fill the listview with Media
            private void populateMediaList(ArrayList<Media> mediaArrayList) {

                // Create the adapter to convert the array to views
                CustomMediaAdapter adapter = new CustomMediaAdapter(context, mediaArrayList);
                // Attach the adapter to a ListView
                GridView listView = (GridView) findViewById(R.id.gvItems);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter, View v, int position,
                                            long arg3) {
                        Media media = (Media) adapter.getItemAtPosition(position);
                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                        dialog.setContentView(R.layout.showimage_layout);
                        ImageView imgView = (ImageView) dialog.findViewById(R.id.ivimagefull);
                        imgView.setImageBitmap(media.bitmap);
                        dialog.show();
                    }
                });

                listView.setAdapter(adapter);
            }


        }