package group9.android_project;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Fetes on 2015-11-19.
 */
public class UserProfileActivity extends AppCompatActivity{

    TextView tvUsername;
    TextView tvName;
    TextView tvVacation;
    TextView tvMemory,tvFilepath,tvRemoveTitle,tvRemoveInfo;
    TextView tvSlash,tvStartDate,tvEndDate,tvLongtide,tvLatitude;
    EditText etTitle, etDescription, etPlace;
    private static final int SELECTED_PICTURE=1;
    private static final int PICK_IMAGE=1;
    private static final int SELECT_VIDEO = 3;
    ArrayList<Media> mediaList = new ArrayList<>();
    ImageView imgViewAdd;

    Button btnDelete,btnAdd,btnCalender,btnCalender2, btnConfirm,btnGetFile,btnEdit;


    Context context = this;
    boolean isUserMe = false;

    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;
    static final int DILOG_ID2 = 1;

    //Dialog calender-popup - Shows a calender-popup and put the date in a textview.
    //region DIALOG CALENDER-POPUP

    public void showDialogOnButtonClick(){
        btnCalender.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG_ID);
                    }
                }
        );
        btnCalender2.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG_ID2);
                    }
                }
        );


    }
    public void showDialogMemoryOnButtonClick() {
        btnCalender.setOnClickListener(
                new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        showDialog(DILOG_ID);
                    }
                }
        );
    }


        @Override
    protected Dialog onCreateDialog(int id){
        if(id == DILOG_ID)
        {
            return new DatePickerDialog(this,dpickerListner,year_x,month_x,day_x);
        }
        if(id == DILOG_ID2)
        {
            return new DatePickerDialog(this,dpickerListner2,year_x,month_x,day_x);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener dpickerListner
            = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year,int monthOfYear,int dayOfMonth) {
                year_x = year;
                month_x = monthOfYear + 1;
                day_x = dayOfMonth;
                Toast.makeText(UserProfileActivity.this, year_x +" / " + month_x+ " / "+day_x,Toast.LENGTH_SHORT).show();
                String date = year_x +" "+ month_x +" "+ day_x;
                tvStartDate.setText(date);
            }
    };
    private DatePickerDialog.OnDateSetListener dpickerListner2
            = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year,int monthOfYear,int dayOfMonth) {
            year_x = year;
            month_x = monthOfYear + 1;
            day_x = dayOfMonth;
            Toast.makeText(UserProfileActivity.this, year_x +" / " + month_x+ " / "+day_x,Toast.LENGTH_SHORT).show();
            tvEndDate.setText(year_x + " " + month_x + " " + day_x);
        }
    };

    //endregion
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        //region Getting layout items
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvName = (TextView) findViewById(R.id.tvName);
        tvVacation = (TextView) findViewById(R.id.tvVacation);
        tvSlash = (TextView) findViewById(R.id.tvSlash);
        tvMemory = (TextView) findViewById(R.id.tvMemory);
        btnDelete = (Button) findViewById(R.id.btnRemoveFriend);
        btnAdd = (Button) findViewById(R.id.btnAdd);

        //endregion

        //region Set visibility
        btnAdd.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        tvVacation.setVisibility(View.INVISIBLE);
        tvSlash.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);
        //endregion

        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);

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
        if(profileUser.username.equals(SharedPref.GetUsername(context)))
        {
            isUserMe = true;
            tvName.setText("Welcome");
            btnAdd.setVisibility(View.VISIBLE);
            LinearLayout titleHolder = (LinearLayout)findViewById(R.id.titleHolder);
            titleHolder.setVisibility(View.GONE);
        }

        if(!isUserMe) {
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.remove_layout);
                    tvRemoveTitle = (TextView)dialog.findViewById(R.id.tvRemoveTitle);
                    tvRemoveInfo = (TextView)dialog.findViewById(R.id.tvRemoveInfo);

                    tvRemoveInfo.setText("Are you sure you want to remove " + profileUser.username+" from your friendslist?");
                    tvRemoveTitle.setText("Remove Friend");

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
                            //Remove friend: Removes a friend
                            //region REMOVE FRIEND
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
                                            Intent i = new Intent(UserProfileActivity.this, MainActivity.class);
                                            UserProfileActivity.this.finish();
                                            i.putExtra("whichtab", 2);
                                            startActivity(i);
                                        }
                                        else{
                                            Toast.makeText(context, "Couldnt remove friend", Toast.LENGTH_SHORT).show();

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
                        //File[] fileToCache = vacationList.toArray(new File[vacationList.size()]);
                        try {
                            JSONObject json = new JSONObject();
                            json.put("vacs",vacationList);
                            String jsonString = json.toString();
                            CacheData.createCachedFile(UserProfileActivity.this, "vacationlist", jsonString);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


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

                        //region If the user browsing it own vacations make it possible to longclick to edit/delete them
                        if(isUserMe) {
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                                               int position, long id) {

                                        final Vacation vacation = (Vacation) adapter.getItemAtPosition(position);
                                        final Dialog dialog = new Dialog(context);
                                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                        dialog.setContentView(R.layout.longclick_layout);
                                        btnDelete = (Button) dialog.findViewById(R.id.btnRemove);
                                        btnDelete.setText("Delete Vacation");
                                        btnEdit = (Button) dialog.findViewById(R.id.btnEdit);


                                        dialog.show();
                                        btnEdit.setOnClickListener(new View.OnClickListener() {
                                            public void onClick(View v2) {
                                                Intent i = new Intent(UserProfileActivity.this, EditActivity.class);
                                                i.putExtra("vacation", vacation);
                                                User nullUser = new User();
                                                i.putExtra("user", nullUser);
                                                startActivity(i);
                                            }
                                        });
                                        Button btnDiaRemove;
                                        btnDiaRemove = (Button) dialog.findViewById(R.id.btnRemove);
                                        btnDiaRemove.setOnLongClickListener(new View.OnLongClickListener() {
                                            @Override
                                            public boolean onLongClick(View v) {
                                                //Remove vacation: Removes a vacation
                                                //region REMOVE VACATION
                                                AsyncCallInfo info = new AsyncCallInfo();
                                                info.command = "RemoveVacation";
                                                info.user = profileUser;
                                                info.vacation = vacation;
                                                info.context = context;
                                                AsyncCall asc = new AsyncCall() {
                                                    @Override
                                                    protected void onPostExecute(JSONObject jsonObject) {

                                                        try {
                                                            int code = (int) jsonObject.get("code");
                                                            if (code == 204) {
                                                                Toast.makeText(context, vacation.title + " removed from vacations!", Toast.LENGTH_SHORT).show();
                                                                Intent i = new Intent(UserProfileActivity.this, MainActivity.class);
                                                                UserProfileActivity.this.finish();
                                                                dialog.dismiss();
                                                                i.putExtra("whichtab", 1);
                                                                startActivity(i);
                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                        }

                                                    }
                                                };
                                                asc.execute(info);
                                                //endregion
                                                return true;
                                            }
                                        });
                                        return true;
                                }
                            });
                        }
                        //endregion

                        listView.setAdapter(adapter);
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.addvacation_layout);

                                //region find layout items
                                btnCalender = (Button)dialog.findViewById(R.id.btnCalender);
                                btnCalender2 = (Button)dialog.findViewById(R.id.btnCalender2);
                                tvStartDate = (TextView)dialog.findViewById(R.id.tvStartDate);
                                tvEndDate = (TextView)dialog.findViewById(R.id.tvEndDate);
                                etTitle = (EditText)dialog.findViewById(R.id.etTitle);
                                etDescription = (EditText)dialog.findViewById(R.id.etDescription);
                                etPlace = (EditText)dialog.findViewById(R.id.etPlace);
                                btnConfirm = (Button)dialog.findViewById(R.id.btnConfirm);
                                //endregion

                                showDialogOnButtonClick();
                                dialog.show();

                                btnConfirm.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        //Add friend: Add a friend to the logged in user
                                        //region ADD FRIEND
                                        final AsyncCallInfo info = new AsyncCallInfo();
                                        Vacation vac = new Vacation();
                                        vac.title = (String)etTitle.getText().toString();
                                        vac.description = etDescription.getText().toString();
                                        vac.place = etPlace.getText().toString();

                                        String start = tvStartDate.getText().toString().trim();
                                        vac.start = Integer.parseInt(start.replaceAll("\\s+",""));
                                        String end = tvEndDate.getText().toString().trim();
                                        vac.end = Integer.parseInt(end.replaceAll("\\s+",""));
                                        info.vacation = vac;
                                        info.command = "AddVacation";
                                        info.context = context;
                                        AsyncCall asc = new AsyncCall() {
                                            @Override
                                            protected void onPostExecute(JSONObject jsonObject) {

                                                try {
                                                    int code = (int) jsonObject.get("code");
                                                    //String responseMsg = (String)jsonObject.get("message");

                                                    if (code == 204) {
                                                        Toast.makeText(UserProfileActivity.this, info.vacation.title + " added!", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(UserProfileActivity.this, MainActivity.class);
                                                        UserProfileActivity.this.finish();
                                                        dialog.dismiss();
                                                        i.putExtra("whichtab", 1);
                                                        startActivity(i);
                                                    } else {
                                                        Toast.makeText(UserProfileActivity.this, code + " - someting went wrong!", Toast.LENGTH_SHORT).show();

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

                    } else {
                        try {
                            JSONObject vacsJson = (JSONObject)CacheData.readCachedFile(UserProfileActivity.this, "vacationlist");

                            tvVacation.setVisibility(View.INVISIBLE);
                            tvSlash.setVisibility(View.INVISIBLE);
                            tvMemory.setVisibility(View.INVISIBLE);
                            Object json = vacsJson.get("vacs");
                            ArrayList<Vacation> vacationlistsCache = (ArrayList<Vacation>) json;

                            // Create the adapter to convert the array to views
                            CustomVacationsAdapter adapter = new CustomVacationsAdapter(context, vacationlistsCache);
                            // Attach the adapter to a ListView
                            GridView listView = (GridView) findViewById(R.id.gvItems);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                        long arg3) {
                                    Vacation vacation = (Vacation) adapter.getItemAtPosition(position);
                                    tvVacation.setText(vacation.title);
                                    tvVacation.setVisibility(View.VISIBLE);
                                }
                            });

                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
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
                        final ArrayList<Memory> memoriesList = (ArrayList<Memory>) jsonObject.get("memories");
                        // Create the adapter to convert the array to views
                        CustomMemoriesAdapter adapter = new CustomMemoriesAdapter(context, memoriesList);
                        // Attach the adapter to a ListView
                        GridView listView = (GridView) findViewById(R.id.gvItems);

                        //region If user is browsing its own memories make it possible to longclick on them to delete them
                        if(isUserMe) {
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                                               int position, long id) {

                                    final Memory memory = (Memory) adapter.getItemAtPosition(position);
                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.longclick_layout);
                                    btnDelete = (Button) dialog.findViewById(R.id.btnRemove);
                                    btnDelete.setText("Delete Memory");
                                    btnEdit = (Button) dialog.findViewById(R.id.btnEdit);
                                    btnEdit.setVisibility(View.GONE);

                                    dialog.show();

                                    Button btnDiaRemove;
                                    btnDiaRemove = (Button) dialog.findViewById(R.id.btnRemove);
                                    btnDiaRemove.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            //Remove vacation: Removes a vacation
                                            //region REMOVE VACATION
                                            AsyncCallInfo info = new AsyncCallInfo();
                                            info.command = "DeleteMemory";
                                            info.vacation = vacation;
                                            info.memory = memory;
                                            info.context = context;
                                            AsyncCall asc = new AsyncCall() {
                                                @Override
                                                protected void onPostExecute(JSONObject jsonObject) {

                                                    try {
                                                        int code = (int) jsonObject.get("code");
                                                        if (code == 204) {
                                                            Toast.makeText(context, memory.title + " deleted from memory!", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                            populateMemoriesList(vacation);
                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            };
                                            asc.execute(info);
                                            //endregion
                                            return true;
                                        }
                                    });
                                    return true;
                                }
                            });
                        }
                        //endregion

                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                                    long arg3) {
                                final Memory memory = (Memory) adapter.getItemAtPosition(position);
                                tvSlash.setVisibility(View.VISIBLE);
                                tvMemory.setText(memory.title);
                                tvMemory.setVisibility(View.VISIBLE);
                                tvMemory.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        populateMemoriesList(vacation);
                                    }
                                });
                                populateMediaList(memory);
                            }
                        });

                        listView.setAdapter(adapter);

                        //region Add memory dialog
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.addmemory_layout);
                                tvStartDate = (TextView) dialog.findViewById(R.id.tvStartDate);
                                btnCalender = (Button)dialog.findViewById(R.id.btnCalender);
                                tvLatitude = (TextView) dialog.findViewById(R.id.tvLatitude);
                                tvLongtide = (TextView) dialog.findViewById(R.id.tvLongitude);

                                Position position = MyLocationListener.GetCurrentPostion(context);
                                tvLongtide.setText(""+position.longitude);
                                tvLatitude.setText(""+position.latitude);

                                etTitle = (EditText) dialog.findViewById(R.id.etTitle);
                                etDescription = (EditText) dialog.findViewById(R.id.etDescription);
                                etPlace = (EditText) dialog.findViewById(R.id.etPlace);
                                btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                                showDialogMemoryOnButtonClick();
                                dialog.show();

                                btnConfirm.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(final View v) {
                                        //Add memory : adds a memory to the user's vacation
                                        //region ADD MEMORY
                                        final AsyncCallInfo info = new AsyncCallInfo();
                                        Memory memory = new Memory();
                                        memory.title = (String) etTitle.getText().toString();
                                        memory.description = etDescription.getText().toString();
                                        memory.place = etPlace.getText().toString();
                                        String start = tvStartDate.getText().toString().trim();
                                        memory.time = Integer.parseInt(start.replaceAll("\\s+", ""));
                                        Position pos = new Position();
                                        pos.latitude = Float.parseFloat(tvLatitude.getText().toString());
                                        pos.longitude = Float.parseFloat(tvLongtide.getText().toString());
                                        memory.position = pos;

                                        info.memory = memory;
                                        info.vacation = vacation;
                                        info.command = "AddMemory";
                                        info.context = context;
                                        AsyncCall asc = new AsyncCall() {
                                            @Override
                                            protected void onPostExecute(JSONObject jsonObject) {

                                                try {
                                                    int code = (int) jsonObject.get("code");
                                                    //String responseMsg = (String)jsonObject.get("message");

                                                    if (code == 204) {
                                                        Toast.makeText(UserProfileActivity.this, info.memory.title + " added!", Toast.LENGTH_SHORT).show();
                                                        populateMemoriesList(vacation);
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(UserProfileActivity.this, code + " - someting went wrong!", Toast.LENGTH_SHORT).show();

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
                        //endregion
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
    private void populateMediaList(final Memory  memory) {

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
                        mediaList = (ArrayList<Media>) jsonObject.get("media");

                        // Create the adapter to convert the array to views
                        CustomMediaAdapter adapter = new CustomMediaAdapter(context,mediaList);
                        // Attach the adapter to a ListView
                        GridView listView = (GridView) findViewById(R.id.gvItems);

                        //region Delete Media on longclick if the media is the logged in users media
                        if(isUserMe) {
                            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                                @Override
                                public boolean onItemLongClick(AdapterView<?> adapter, View v,
                                                               int position, long id) {

                                    final Media media = (Media) adapter.getItemAtPosition(position);
                                    final Dialog dialog = new Dialog(context);
                                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                    dialog.setContentView(R.layout.longclick_layout);
                                    btnDelete = (Button) dialog.findViewById(R.id.btnRemove);
                                    btnDelete.setText("Delete Media");
                                    btnEdit = (Button) dialog.findViewById(R.id.btnEdit);
                                    btnEdit.setVisibility(View.GONE);

                                    dialog.show();

                                    Button btnDiaRemove;
                                    btnDiaRemove = (Button) dialog.findViewById(R.id.btnRemove);
                                    btnDiaRemove.setOnLongClickListener(new View.OnLongClickListener() {
                                        @Override
                                        public boolean onLongClick(View v) {
                                            //Remove vacation: Removes a vacation
                                            //region REMOVE VACATION
                                            AsyncCallInfo info = new AsyncCallInfo();
                                            info.command = "DeleteMedia";
                                            info.media = media;
                                            info.context = context;
                                            AsyncCall asc = new AsyncCall() {
                                                @Override
                                                protected void onPostExecute(JSONObject jsonObject) {

                                                    try {
                                                        int code = (int) jsonObject.get("code");
                                                        if (code == 204) {
                                                            Toast.makeText(context, "File deleted from memory!", Toast.LENGTH_SHORT).show();
                                                            dialog.dismiss();
                                                            populateMediaList(memory);
                                                        } else {
                                                            Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                                                        }

                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }

                                                }
                                            };
                                            asc.execute(info);
                                            //endregion
                                            return true;
                                        }
                                    });
                                    return true;
                                }
                            });
                        }
                        //endregion

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
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(context);

                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.addmedia_layout);
                                imgViewAdd = (ImageView) dialog.findViewById(R.id.imgViewAdd);
                                tvFilepath = (TextView) dialog.findViewById(R.id.tvFilepath);
                                Button btnGetVideo = (Button) dialog.findViewById(R.id.btnGetVideo);
                                btnGetFile = (Button) dialog.findViewById(R.id.btnGetFile);
                                btnGetFile.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        getIntent.setType("image/*");

                                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        pickIntent.setType("image/*");

                                        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                                        startActivityForResult(chooserIntent, PICK_IMAGE);
                                    }
                                });
                                btnGetVideo.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {

                                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                                        getIntent.setType("video/*");

                                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        pickIntent.setType("video/*");

                                        Intent chooserIntent = Intent.createChooser(getIntent, "Select a Video ");
                                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{pickIntent});

                                        startActivityForResult(chooserIntent, SELECT_VIDEO);
                                    }
                                });

                                dialog.show();


                                etTitle = (EditText) dialog.findViewById(R.id.etTitle);
                                etDescription = (EditText) dialog.findViewById(R.id.etDescription);
                                etPlace = (EditText) dialog.findViewById(R.id.etPlace);
                                btnConfirm = (Button) dialog.findViewById(R.id.btnConfirm);
                                dialog.show();
                                boolean isFileAdded = false;
                                if (tvFilepath.getText().equals("")) {
                                    btnConfirm.setEnabled(false);
                                }


                                btnConfirm.setOnClickListener(new View.OnClickListener() {
                                    public void onClick(final View v) {
                                        //Add/upload media : Upload an image to the database
                                        //region ADD/UPLOAD MEDIA
                                        final AsyncCallInfo info = new AsyncCallInfo();
                                        imgViewAdd.buildDrawingCache();
                                        String filePath = tvFilepath.getText().toString();
                                        Bitmap imageToUpload = imgViewAdd.getDrawingCache();
                                        info.memory = memory;
                                        info.filePath = filePath;
                                        info.command = "UploadFile";
                                        info.context = context;
                                        AsyncCall asc = new AsyncCall() {
                                            @Override
                                            protected void onPostExecute(JSONObject jsonObject) {

                                                try {
                                                    int code = (int) jsonObject.get("code");
                                                    //String responseMsg = (String)jsonObject.get("message");

                                                    if (code == 200) {
                                                        Toast.makeText(UserProfileActivity.this, "File added to " + memory.title, Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                        populateMediaList(memory);
                                                    } else {
                                                        Toast.makeText(UserProfileActivity.this, code + " - someting went wrong!", Toast.LENGTH_SHORT).show();

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

    //After getting the file from the device
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK) {
                    Uri uri = data.getData();
                    String[]projection= {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    //Drawable d = new BitmapDrawable(yourSelectedImage);
                    tvFilepath.setText(filePath);
                    imgViewAdd.setImageBitmap(yourSelectedImage);
                    btnConfirm.setEnabled(true);

                }
                break;
            case SELECT_VIDEO:
                if (resultCode == RESULT_OK) {
                    System.out.println("SELECT_VIDEO");
                    Uri selectedVideoUri = data.getData();
                    String selectedPath = getPath(selectedVideoUri);
                    System.out.println("SELECT_VIDEO Path : " + selectedPath);

                    tvFilepath.setText(selectedPath);
                    btnConfirm.setEnabled(true);

                }
                break;
        }
    }

    //Video stuff
    private String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA, MediaStore.Video.Media.SIZE, MediaStore.Video.Media.DURATION};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        cursor.moveToFirst();
        String filePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA));
        int fileSize = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE));
        long duration = TimeUnit.MILLISECONDS.toSeconds(cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION)));


        //some extra potentially useful data to help with filtering if necessary
        System.out.println("size: " + fileSize);
        System.out.println("path: " + filePath);
        System.out.println("duration: " + duration);

        return filePath;
    }



}