package com.digitalhomeland.employeedashboard;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalhomeland.employeedashboard.models.Application;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ApplicationCreateActivity extends AppCompatActivity {
    //UI References
    private TextView selectDate;
    private EditText titleText;
    private EditText subjectText;
    private Button sendApplicationButton;

    private DatePickerDialog fromDatePickerDialog;

    private SimpleDateFormat dateFormatter;
    private static String returnDate;
    private static String returnString, titleString, subjectString;
    static DatabaseHandler db;
    SharedPreferences pref;
    private static Activity mActivity;
    private static Volley_Request postRequest;
    private static TableRow dateRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pref = getApplicationContext().getSharedPreferences("UserPrefs", MODE_PRIVATE);
        setContentView(R.layout.activity_application_create);
        //set bkg
        getWindow().setBackgroundDrawableResource(R.drawable.btr);
        db = new DatabaseHandler(this);
        mActivity = ApplicationCreateActivity.this;
        dateRow = (TableRow) findViewById(R.id.date_row);
        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
        Bundle bundle = getIntent().getExtras();
        findViewsById();
        setDateTimeField();
    }

    private void findViewsById() {
        selectDate = (TextView) findViewById(R.id.etxt_fromdate);
        //selectDate.setInputType(InputType.TYPE_NULL);
        selectDate.setText("Select Date");
        selectDate.requestFocus();
        titleText = (EditText) findViewById(R.id.application_title);
        subjectText = (EditText) findViewById(R.id.application_subject_text);
        sendApplicationButton = (Button) findViewById(R.id.application_done_button);
        sendApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                titleString = titleText.getText().toString();
                subjectString = subjectText.getText().toString();
                returnString  = "{" + "\"reciever\":\"recieve\",\"params\":{\"storeId\":" + "\""+ db.getUser().getStoreId() + "\"," + "\"empId\":\"" + db.getUser().getEmployeeId() + "\"," +"\"date\":\"" + returnDate + "\",\"title\":\"" + titleText.getText() + "\",\"subject\":\"" + subjectText.getText() + "\"}}";
                Log.d("myTag", "ReturnString : " + returnString);
                postRequest = new Volley_Request();
                postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_appli) , "POST", "SendApplicationActivity",returnString);

//  ReturnString : {"reciever":"recieve","params":{"teacherid":"123","date":"15-06-2017","title":"For Leave of 2 dayz","subject":"My sample leave application yo"}}
            }
        });
    }

    private void setDateTimeField() {
        selectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                if(view == selectDate) {
                    fromDatePickerDialog.show();
                }
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                returnDate = dateFormatter.format(newDate.getTime());
                selectDate.setText(returnDate);
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.action_refresh:
                Toast.makeText(this, "Refresh selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            // action with ID action_settings was selected
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
        }
        return true;
    }

    public static void getSendApplicationResponse(String responseString){
        try {
            Log.d("myTag", "got load attendance resp : " + responseString);
            JSONObject responeObj = new JSONObject(responseString);
            String applicationId = responeObj.getString("res");
            String seqId = responeObj.getString("seqId");
            Log.d("myTag", "creating appli : " + returnDate + " : " + titleString + " : " + subjectString);
            Application application = new Application(applicationId,titleString,subjectString,returnDate,db.getUser().getEmployeeId(),seqId);
            if(!db.containsAppli(returnDate)) {
                db.addApplications(application);
            }
            Intent i = new Intent(mActivity, DashboardActivity.class);
            mActivity.startActivity(i);
        }
        catch(Exception e){Log.d("myTag", "getloadAttendanceResponse : " , e);}
    }

}
