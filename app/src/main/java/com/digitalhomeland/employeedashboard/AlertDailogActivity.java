package com.digitalhomeland.employeedashboard;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.digitalhomeland.employeedashboard.models.Taskd;
import com.digitalhomeland.employeedashboard.models.Tasko;
import com.digitalhomeland.employeedashboard.models.Taskw;

public class AlertDailogActivity extends AppCompatActivity {

    AlertDialog.Builder mAlertDlgBuilder;
    AlertDialog mAlertDialog;
    View mDialogView = null;
    Button mOKBtn, mCancelBtn;
    static DatabaseHandler db;
    static String type, id, hours, minutes, acceptedAt;
    private static Volley_Request postRequest;
    static Context mContext;
    static Taskd taskd;
    static Taskw taskw;
    static Tasko tasko;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_alert_dailog);
        LayoutInflater inflater = getLayoutInflater();
        db = new DatabaseHandler(this);
        // Build the dialog
        mContext = getApplicationContext();
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        id = bundle.getString("id");
        hours = bundle.getString("hour");
        minutes = bundle.getString("minutes");
        acceptedAt = hours + ":" + minutes;
        mAlertDlgBuilder = new AlertDialog.Builder(this);
        mDialogView = inflater.inflate(R.layout.activity_alert_dailog, null);
        mOKBtn = (Button)mDialogView.findViewById(R.id.ID_Ok);
        mCancelBtn = (Button)mDialogView.findViewById(R.id.ID_Cancel);
        mOKBtn.setOnClickListener(mDialogbuttonClickListener);
        mCancelBtn.setOnClickListener(mDialogbuttonClickListener);
        mAlertDlgBuilder.setCancelable(false);
        mAlertDlgBuilder.setInverseBackgroundForced(true);
        mAlertDlgBuilder.setView(mDialogView);
        mAlertDialog = mAlertDlgBuilder.create();
        mAlertDialog.show();
    }

    View.OnClickListener mDialogbuttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.ID_Ok)
            {
                if(type.equals("taskd")){
                    mAlertDialog.dismiss();
                    taskd = db.getTaskdById(id);
                    String responseString = "{\"reciever\":\"TDAcceptAlarm\",\"" + "taskId" + "\":\"" + taskd.get_id() + "\",\"acceptedAt" + "\":\"" + acceptedAt + "\"}";
                    Log.d("myTag", "appliTeacherResponseRequest : " + responseString);
                    postRequest = new Volley_Request();
                    postRequest.createRequest(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mJSONURL_taskd), "POST", "TDAcceptAlarm", responseString);
                    finish();
                }
                else if(type.equals("taskw")){
                    mAlertDialog.dismiss();
                    taskw = db.getTaskwById(id);
                    String responseString = "{\"reciever\":\"TWAcceptAlarm\",\"" + "taskId" + "\":\"" + taskw.getId() + "\",\"acceptedAt" + "\":\"" + acceptedAt + "\"}";
                    Log.d("myTag", "appliTeacherResponseRequest : " + responseString);
                    postRequest = new Volley_Request();
                    postRequest.createRequest(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mJSONURL_taskw), "POST", "TWAcceptAlarm", responseString);
                    finish();
                }
                else if(type.equals("tasko")) {
                    mAlertDialog.dismiss();
                    tasko = db.getTaskoById(id);
                    String responseString = "{\"reciever\":\"TOAcceptAlarm\",\"" + "taskId" + "\":\"" + tasko.getId()  + "\",\"acceptedAt" + "\":\"" + acceptedAt + "\"}";
                    Log.d("myTag", "appliTeacherResponseRequest : " + responseString);
                    postRequest = new Volley_Request();
                    postRequest.createRequest(getApplicationContext(), getApplicationContext().getResources().getString(R.string.mJSONURL_tasko), "POST", "TOAcceptAlarm", responseString);
                    finish();
                }
            }
            else if(v.getId() == R.id.ID_Cancel)
            {
                mAlertDialog.dismiss();
                finish();
            }
        }
    };

    public static void TDAcceptAlarmString(String responseString) {
        if(db.updateTaskDAcceptStatus(taskd.get_id(), hours + " : " + minutes)) {
            //send Response
            Intent i = new Intent(mContext, DashboardActivity.class);
            mContext.startActivity(i);
        }
    }

    public static void TWAcceptAlarmString(String responseString) {
        if(db.updateTaskWAcceptStatus(taskw.getId(), hours + " : " + minutes)) {
            //send Response
            Intent i = new Intent(mContext, DashboardActivity.class);
            mContext.startActivity(i);
        }
    }

    public static void TOAcceptAlarmString(String responseString) {
        if(db.updateTaskOAcceptStatus(tasko.getId(), hours + " : " + minutes)) {
            //send Response
            Intent i = new Intent(mContext, DashboardActivity.class);
            mContext.startActivity(i);
        }
    }
}
