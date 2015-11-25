package group9.android_project;

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
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Fetes on 2015-11-19.
 */
public class UserProfileActivity extends AppCompatActivity{

    TextView tvUsername;
    TextView tvName;
    TextView tvVacation;
    TextView tvMemory;
    TextView tvSlash,tvStartDate,tvEndDate,tvLongtide,tvLatitude;
    EditText etTitle, etDescription, etPlace;
    private static final int SELECTED_PICTURE=1;
    private static final int PICK_IMAGE=1;

    ImageView imgViewAdd;

    Button btnDelete,btnAdd,btnCalender,btnCalender2, btnConfirmVacation,btnGetFile;


    Context context = this;
    boolean isUserMe = false;

    int year_x,month_x,day_x;
    static final int DILOG_ID = 0;
    static final int DILOG_ID2 = 1;
    static final int DILOG_ID3 = 2;

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
        tvUsername = (TextView) findViewById(R.id.tvUsername);
        tvName = (TextView) findViewById(R.id.tvName);
        tvVacation = (TextView) findViewById(R.id.tvVacation);
        tvSlash = (TextView) findViewById(R.id.tvSlash);
        tvMemory = (TextView) findViewById(R.id.tvMemory);
        btnDelete = (Button) findViewById(R.id.btnRemoveFriend);
        btnAdd = (Button) findViewById(R.id.btnAdd);


        btnAdd.setVisibility(View.INVISIBLE);
        btnDelete.setVisibility(View.INVISIBLE);
        tvVacation.setVisibility(View.INVISIBLE);
        tvSlash.setVisibility(View.INVISIBLE);
        tvMemory.setVisibility(View.INVISIBLE);

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


        }

        if(!isUserMe) {
            btnDelete.setVisibility(View.VISIBLE);
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
                                            startActivity(new Intent(context, FriendsActivity.class));
                                        } else {

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
                        btnAdd.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                final Dialog dialog = new Dialog(context);
                                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                                dialog.setContentView(R.layout.addvacation_layout);
                                btnCalender = (Button)dialog.findViewById(R.id.btnCalender);
                                btnCalender2 = (Button)dialog.findViewById(R.id.btnCalender2);
                                tvStartDate = (TextView)dialog.findViewById(R.id.tvStartDate);
                                tvEndDate = (TextView)dialog.findViewById(R.id.tvEndDate);
                                etTitle = (EditText)dialog.findViewById(R.id.etTitle);
                                etDescription = (EditText)dialog.findViewById(R.id.etDescription);
                                etPlace = (EditText)dialog.findViewById(R.id.etPlace);
                                btnConfirmVacation = (Button)dialog.findViewById(R.id.btnConfirmVacation);
                                showDialogOnButtonClick();
                                dialog.show();

                                btnConfirmVacation.setOnClickListener(new View.OnClickListener() {
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
                                                        finish();
                                                        startActivity(getIntent());
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
                                btnConfirmVacation = (Button) dialog.findViewById(R.id.btnConfirmVacation);
                                showDialogMemoryOnButtonClick();
                                dialog.show();

                                btnConfirmVacation.setOnClickListener(new View.OnClickListener() {
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
        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);

                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.addmedia_layout);
                imgViewAdd = (ImageView) dialog.findViewById(R.id.imgViewAdd);
                btnGetFile = (Button) dialog.findViewById(R.id.btnGetFile);
                btnGetFile.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent getIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        getIntent.setType("image/*");

                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");

                        Intent chooserIntent = Intent.createChooser(getIntent, "Select Image");
                        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] {pickIntent});

                        startActivityForResult(chooserIntent, PICK_IMAGE);
                    }
                });
                dialog.show();


                /*tvStartDate = (TextView) dialog.findViewById(R.id.tvStartDate);
                btnCalender = (Button) dialog.findViewById(R.id.btnCalender);
                tvLatitude = (TextView) dialog.findViewById(R.id.tvLatitude);
                tvLongtide = (TextView) dialog.findViewById(R.id.tvLongitude);


                Position position = MyLocationListener.GetCurrentPostion(context);
                tvLongtide.setText("" + position.longitude);
                tvLatitude.setText("" + position.latitude);

                etTitle = (EditText) dialog.findViewById(R.id.etTitle);
                etDescription = (EditText) dialog.findViewById(R.id.etDescription);
                etPlace = (EditText) dialog.findViewById(R.id.etPlace);
                btnConfirmVacation = (Button) dialog.findViewById(R.id.btnConfirmVacation);
                showDialogMemoryOnButtonClick();
                dialog.show();

                btnConfirmVacation.setOnClickListener(new View.OnClickListener() {
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
                });*/
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case SELECTED_PICTURE:
                if(resultCode==RESULT_OK) {
                    Uri uri = data.getData();
                    String[]projection= {MediaStore.Images.Media.DATA};

                    Cursor cursor = getContentResolver().query(uri,projection,null,null,null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(projection[0]);
                    String filePath = cursor.getString(columnIndex);
                    cursor.close();

                    Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                    Drawable d = new BitmapDrawable(yourSelectedImage);
                    imgViewAdd.setBackground(d);

                }
                break;
        }
    }



}