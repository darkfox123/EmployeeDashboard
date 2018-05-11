package com.digitalhomeland.employeedashboard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;
import android.widget.TextView;

import com.digitalhomeland.employeedashboard.R;
import com.digitalhomeland.employeedashboard.models.Application;

public class ApplicationNotifViewActivity extends AppCompatActivity {

    TextView dateView, titleView, subjectView, acceptStatusTv;
    String applicationId;
    String acceptStatus;
    TableRow acceptStatusRow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_notif_view);
        //set bkg
        final DatabaseHandler db = new DatabaseHandler(this);
        getWindow().setBackgroundDrawableResource(R.drawable.btr);
        acceptStatusRow = (TableRow) findViewById(R.id.accept_status_tv) ;
        acceptStatusTv = (TextView) findViewById(R.id.accept_status_textview);
        dateView = (TextView)findViewById(R.id.notif_date_textview);
        titleView = (TextView)findViewById(R.id.notif_title_textview);
        subjectView = (TextView) findViewById(R.id.notif_subject_textview);
        applicationId = getIntent().getStringExtra("applicationid");
        Log.d("myTag", "appliId recived : " + applicationId);
        Application appli = db.getApplicationById(applicationId);
        acceptStatus = getIntent().getStringExtra("acceptstatus");
        Log.d("myTag", "acceptstatus recived : " + getIntent().getStringExtra("acceptstatus"));

        if(acceptStatus.equals("")){
            acceptStatusTv.setVisibility(View.GONE);
            acceptStatusRow.setVisibility(View.GONE);
        }else{
            if(acceptStatus.equals("true")) {
                acceptStatusRow.setBackgroundColor(Color.parseColor("#708238"));
                acceptStatusTv.setText("Approved");
            }
            else if(acceptStatus.equals("false")) {
                acceptStatusRow.setBackgroundColor(Color.parseColor("#c34242"));
                //acceptStatusTv.setBackgroundColor(Color.RED);
                acceptStatusTv.setText("Rejected");
            }
            acceptStatusTv.setVisibility(View.VISIBLE);
        }

        if (getIntent().hasExtra("date") && getIntent().hasExtra("title") && getIntent().hasExtra("subject")){
            dateView.setText(getIntent().getStringExtra("date"));
            titleView.setText(getIntent().getStringExtra("title"));
            subjectView.setText(getIntent().getStringExtra("subject"));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //try using this intent
        if (getIntent().hasExtra("date") && getIntent().hasExtra("title") && getIntent().hasExtra("subject")){
            dateView.setText(getIntent().getStringExtra("date"));
            titleView.setText(getIntent().getStringExtra("title"));
            subjectView.setText(getIntent().getStringExtra("subject"));
        }
    }
}
