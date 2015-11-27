package group9.android_project;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;

/**
 * Created by Halling- on 2015-11-27.
 */
public class EditActivity extends AppCompatActivity {
    TextView tvEditTitle,tvStartDate,tvEndDate;
    EditText etFirst, etSecond, etThird;
    Button btnApplyChanges;
    Button btnCalender,btnCalender2;
    LinearLayout llVacationDateHolder;
    Context context = this;

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.edit_layout);
        Intent i = getIntent();
        final User profileUser = (User) i.getSerializableExtra("user");

        tvEditTitle = (TextView)findViewById(R.id.tvEditTitle);
        tvEndDate = (TextView)findViewById(R.id.tvEndDate);
        tvStartDate = (TextView)findViewById(R.id.tvStartDate);
        etFirst = (EditText)findViewById(R.id.etFirst);
        etSecond = (EditText)findViewById(R.id.etSecond);
        etThird = (EditText)findViewById(R.id.etThird);
        btnApplyChanges = (Button)findViewById(R.id.btnApplyChanges);
        llVacationDateHolder = (LinearLayout)findViewById(R.id.llVacationDateHolder);

        //region getCurrent time for the calender
        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);
        //endregion

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
                            Toast.makeText(EditActivity.this, "Token Refreshed!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Toast.makeText(EditActivity.this, "Coulndt create token with savedprefs!", Toast.LENGTH_SHORT).show();
                            SharedPref.clearPrefs(context);
                            startActivity(new Intent(EditActivity.this, LoginActivity.class));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            };
            asc.execute(info);
        }
        //endregion


        if(profileUser.username == null)
        {
            //EDIT VACATION
            final Vacation vacationToEdit = (Vacation) i.getSerializableExtra("vacation");
            etFirst.setText(vacationToEdit.title);
            etSecond.setText(vacationToEdit.description);
            etThird.setText(vacationToEdit.place);
            tvEndDate.setText(Integer.toString(vacationToEdit.end));
            tvStartDate.setText(Integer.toString(vacationToEdit.start));
            btnCalender = (Button)findViewById(R.id.btnCalender);
            btnCalender2 = (Button)findViewById(R.id.btnCalender2);
            showDialogOnButtonClick();

            btnApplyChanges.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //region APPLYCHANGES

                    final AsyncCallInfo info = new AsyncCallInfo();

                    vacationToEdit.title = etFirst.getText().toString().trim();
                    vacationToEdit.description = etSecond.getText().toString();
                    vacationToEdit.place = etThird.getText().toString();
                    String start = tvStartDate.getText().toString().trim();
                    vacationToEdit.start = Integer.parseInt(start.replaceAll("\\s+", ""));
                    String end = tvEndDate.getText().toString().trim();
                    vacationToEdit.end = Integer.parseInt(end.replaceAll("\\s+", ""));

                    info.vacation = vacationToEdit;
                    info.command = "PatchVacation";
                    info.context = context;
                    AsyncCall asc = new AsyncCall() {
                        @Override
                        protected void onPostExecute(JSONObject jsonObject) {

                            try {
                                int code = (int) jsonObject.get("code");
                                //String responseMsg = (String)jsonObject.get("message");

                                if (code == 204) {
                                    Toast.makeText(EditActivity.this, info.vacation.title + " changed!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditActivity.this, UserProfileActivity.class));
                                } else {
                                    Toast.makeText(EditActivity.this, code + " - someting went wrong!", Toast.LENGTH_SHORT).show();

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
        else
        {
            //EDIT USER
            etFirst.setHint("Firstname");
            etSecond.setHint("Lastname");
            etSecond.setLines(1);
            etThird.setHint("Email");
            etThird.setInputType( InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS );
            tvEditTitle.setText("Edit User");

            llVacationDateHolder.setVisibility(View.INVISIBLE);

            final User userToEdit = (User) i.getSerializableExtra("user");
            etFirst.setText(userToEdit.firstname);
            etSecond.setText(userToEdit.lastname);
            etThird.setText(userToEdit.email);

            btnApplyChanges.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    //region APPLYCHANGES

                    final AsyncCallInfo info = new AsyncCallInfo();

                    userToEdit.firstname = etFirst.getText().toString().trim();
                    userToEdit.lastname = etSecond.getText().toString();
                    userToEdit.email = etThird.getText().toString();

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
                                    Toast.makeText(EditActivity.this, info.user.username + " changed!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(EditActivity.this, UserProfileActivity.class));
                                } else {
                                    Toast.makeText(EditActivity.this, code + " - someting went wrong!", Toast.LENGTH_SHORT).show();

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
    }

    final Calendar cal = Calendar.getInstance();

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
            Toast.makeText(EditActivity.this, year_x +" / " + month_x+ " / "+day_x,Toast.LENGTH_SHORT).show();
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
            Toast.makeText(EditActivity.this, year_x +" / " + month_x+ " / "+day_x,Toast.LENGTH_SHORT).show();
            tvEndDate.setText(year_x + " " + month_x + " " + day_x);
        }
    };

    //endregion
}
