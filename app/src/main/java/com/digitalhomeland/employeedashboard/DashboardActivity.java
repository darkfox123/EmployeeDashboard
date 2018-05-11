package com.digitalhomeland.employeedashboard;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.digitalhomeland.employeedashboard.models.Application;
import com.digitalhomeland.employeedashboard.models.EmpRoster;
import com.digitalhomeland.employeedashboard.models.LocationObj;
import com.digitalhomeland.employeedashboard.models.Notif;
import com.digitalhomeland.employeedashboard.models.StRoster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.digitalhomeland.employeedashboard.LandingActivity.db;

public class DashboardActivity extends AppCompatActivity{

    private static Volley_Request getRequest, postRequest;
    LocationManager locationManager;
    static Location testLoc;
    static Address testAddress;
    static String buttonAction = "";
    static Activity mActivity;
    static Context mContext;
    static DatabaseHandler db = null;
    static String[] dates = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        mActivity = DashboardActivity.this;
        mContext=this;
        db = new DatabaseHandler(this);
        //db.deleteAppli();
        //db.deleteTasks();
        getWindow().setBackgroundDrawableResource(R.drawable.btr);
        Calendar currCal = Calendar.getInstance();
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        Button loadRoster = (Button) findViewById(R.id.load_roster);
        loadRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteRoster();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
                String currentDate = df2.format(c.getTime());
                if(c.DAY_OF_WEEK == c.DAY_OF_WEEK && db.getEmpRosterByDate(currentDate) == null && db.getSTRosterByDate(currentDate) == null) {
                    String req = "{\"reciever\":\"EmpRead\", \"params\" :{\"storeId\":\"" + db.getUser().getStoreId() + "\", \"date\":[" ;
                            //+ calDate + "\"}}";

                    for (int i = 0; i < 7; i++) {
                        dates[i] = df2.format(c.getTime());
                        if(i == 6){
                            req += "\"" + df2.format(c.getTime()) + "\"]}}";
                        }
                        else {
                            req += "\"" + df2.format(c.getTime()) + "\",";
                        }
                        c.add(Calendar.DAY_OF_WEEK, 1);
                    }
                    postRequest = new Volley_Request();
                    postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_stroster), "POST", "getSTRforDate", req);
                }
            }
        });

        Button viewRoster = (Button) findViewById(R.id.view_rooster);
        viewRoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, ViewRoster.class);
                startActivity(i);
            }
        });

        Button viewTasks = (Button) findViewById(R.id.view_tasks);
        viewTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, TasksActivity.class);
                startActivity(i);
            }
        });

        Button checkIn = (Button) findViewById(R.id.check_in);
        checkIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction = "1";
                Intent i = new Intent(DashboardActivity.this, AttendanceActivity.class);
                i.putExtra("action", "checkIn");
                startActivity(i);
            }
        });

        Button checkOut = (Button) findViewById(R.id.check_out);
        checkOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonAction = "2";
                Intent i = new Intent(DashboardActivity.this, AttendanceActivity.class);
                i.putExtra("action", "checkOut");
                startActivity(i);
            }
        });

        Button sendApplication = (Button) findViewById(R.id.send_appli);
        sendApplication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DashboardActivity.this, ApplicationCreateActivity.class);
                // i.putExtra("empId", studentId);
                startActivity(i);
            }
        });

        Button syncB = (Button) findViewById(R.id.sync_button);
        syncB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNotifs(db.getUser().getEmployeeId());
                            }
        });

        Button viewNotificationButton = (Button) findViewById(R.id.view_notifs);
        viewNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                Intent i = new Intent(DashboardActivity.this, NotificationActivity.class);
                startActivity(i);

            }
        });

        Button viewApplicationButton = (Button) findViewById(R.id.view_appli);
        viewApplicationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                Intent i = new Intent(DashboardActivity.this, ApplicationViewActivity.class);
                i.putExtra("studentid", db.getUser().getEmployeeId());
                i.putExtra("name", db.getUser().getFirstName());
                //i.putExtra("rollno", rollNo);
                startActivity(i);
            }
        });

        Button viewAttendanceButton = (Button) findViewById(R.id.view_attendance);
        viewAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                Intent i = new Intent(DashboardActivity.this, AttendanceView.class);
                i.putExtra("empId", db.getUser().getEmployeeId());
                startActivity(i);
            }
        });

    }

    public static void getSTRforDateResp(String response){
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray rosterObj = responseObj.getJSONArray("storeRoster");
            for(int i = 0; i< rosterObj.length(); i++) {
                JSONObject tmp = (JSONObject)rosterObj.get(i);
                StRoster str = new StRoster(tmp.get("_id").toString(), tmp.get("dayOfW").toString(), tmp.get("date").toString(), tmp.get("storeStatus").toString(), tmp.get("events").toString(), tmp.get("eventTitle").toString(), tmp.get("eventSubject").toString());
                if(db.containsSTR(str.getDate(), str.getId())) {
                    db.addSTRoster(str);
                }
            }
            String req = "{\"reciever\":\"EmpRead\", \"params\" :{\"storeId\":\"" + db.getUser().getStoreId() +"\",\"empId\":\"" + db.getUser().getEmployeeId() + "\", \"date\":[" ;
            //+ calDate + "\"}}";

            for (int i = 0; i < 7; i++) {
                if(i == 6){
                    req += "\"" + dates[i] + "\"]}}";
                }
                else {
                    req += "\"" + dates[i] + "\",";
                }
            }
            postRequest = new Volley_Request();
            postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_emproster), "POST", "getEMRforDate", req);
        }catch (Exception e) {
            Log.d("myTag", "error : " + e, e);
            e.printStackTrace();
        }
    }

    public static void getEMRforDateResp(String response){
        try {
            JSONObject responseObj = new JSONObject(response);
            JSONArray rosterObj = responseObj.getJSONArray("empRoster");
            for(int i = 0; i< rosterObj.length(); i++) {
                JSONObject tmp = (JSONObject)rosterObj.get(i);
                EmpRoster emr = new EmpRoster(tmp.get("_id").toString(),tmp.get("dayOfW").toString(),tmp.get("date").toString(),tmp.get("empId").toString(),tmp.get("status").toString(),tmp.get("shift").toString());
                if(db.containsEMR(emr.getDate(), emr.getId()))
                db.addEmpRoster(emr);
            }
        }catch (Exception e) {
            Log.d("myTag", "error : " + e, e);
            e.printStackTrace();
        }
    }


    public static void requestNotifs(String empid){
        String requestNotif = "{\"reciever\":\"EmpRead\", \"params\":{\"storeId\":\"" + db.getStore().getStoreId() + "\",\"seqId\":\"" + db.getNotifSeq("B") + "\"}}";
        Log.d("myTag", "requestNotif : " + requestNotif);
        postRequest = new Volley_Request();
        postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_notifb), "POST", "loadNotifbSync",requestNotif);
    }

    public static void getloadNotifbResponse(String responseString){
        try {
            Log.d("myTag", "got load notif resp : " + responseString);
            JSONObject responeObj = new JSONObject(responseString);
            JSONArray notifArr = responeObj.getJSONArray("notifs");
            for(int i=0; i< notifArr.length(); i++){
                JSONObject notifEle = (JSONObject) notifArr.get(i);
                Notif notifObj = new Notif(notifEle.getString("_id"),"B",notifEle.getString("title"),notifEle.getString("subject"),notifEle.getString("date"), db.getUser().getEmployeeId(), notifEle.getInt("seqId"));
                db.addNotif(notifObj);
                showNotificationNotif(notifObj ,i*i);
            }
            String applicationRequest = "{\"reciever\":\"empsync\",\"params\":{\"empId\":\""+ db.getUser().getEmployeeId() + "\",\"storeId\":\"" + db.getUser().getStoreId() +"\"}}";
            postRequest = new Volley_Request();
            postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_appli), "POST", "SyncAppli",applicationRequest);
        }
        catch(Exception e){Log.d("myTag", "getloadNotifResponse : " , e);}
    }

    public static void getloadNotifeResponse(String responseString){
        try {
            Log.d("myTag", "got load notif resp : " + responseString);
            JSONObject responeObj = new JSONObject(responseString);
            JSONArray notifArr = responeObj.getJSONArray("notifs");
            for(int i=0; i< notifArr.length(); i++){
                JSONObject notifEle = (JSONObject) notifArr.get(i);
                Notif notifObj = new Notif(notifEle.getString("_id"),"E",notifEle.getString("title"),notifEle.getString("subject"),notifEle.getString("date"), db.getUser().getEmployeeId(), notifEle.getInt("seqId"));
                db.addNotif(notifObj);
                showNotificationNotif(notifObj ,i*i);
            }}
        catch(Exception e){Log.d("myTag", "getloadNotifResponse : " , e);}
    }


    public static void showNotificationNotif(Notif notif ,int id){
        Intent i = new Intent(mActivity, NotificationView.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("myTag", "notif to disp : " + notif.getId());
        i.putExtra("date", notif.getDate());
        i.putExtra("title", notif.getTitle());
        i.putExtra("subject", notif.getSubject());
        i.putExtra("notifid", notif.getId());
        PendingIntent pi = PendingIntent.getActivity(mActivity, 0, i, 0);
        //Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(mActivity)
                .setTicker(notif.getDate())
                .setColor(Color.parseColor("#9920443F"))
                .setSmallIcon(R.drawable.notife)
                .setContentTitle(notif.getTitle())
                .setContentText(notif.getSubject())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
    }

    public static void getloadAppliResponse(String responseString){
        try{
            JSONObject responseObj = new JSONObject(responseString);
            JSONArray responseArr = responseObj.getJSONArray("applications");
            for(int i=0; i<responseArr.length(); i++){
                JSONObject applicationObject = responseArr.getJSONObject(i);
                Application application = new Application(applicationObject.getString("id"), applicationObject.getString("accepted"),applicationObject.getString("title"),applicationObject.getString("subject"),applicationObject.getString("date"),applicationObject.getString("empId"),applicationObject.getString("seqId"));
                Log.d("myTag", "sync:addingApplication: " + application.getTitle() + " : " + application.getSubject());
                db.addApplications(application);
                showApplicationNotif(application,applicationObject.getString("accepted")  ,i*i);
            }
            String requestNotif = "{\"reciever\":\"EmpRead\", \"params\":{\"storeId\":\"" + db.getStore().getStoreId() + "\",\"seqId\":\"" + db.getNotifSeq("E") + "\"}}";
            Log.d("myTag", "requestNotif : " + requestNotif);
            postRequest = new Volley_Request();
            postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_notife), "POST", "loadNotifeSync",requestNotif);
        }catch(JSONException e){
            Log.d("myTag", "json error " ,e);
        }
    }


    public static void syncCheckInResponse(String responseString){
        try{
            JSONObject responseObj = new JSONObject(responseString);

                LocationObj locRes = new LocationObj(responseObj.getString("lat"),responseObj.getString("longi"),responseObj.getString("address"));
            //List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d("myTag", "sync:addingApplication: " + testAddress.getAddressLine(0).equals(locRes.getAddress()) + " : " + locRes.getLat().equals(testLoc.getLatitude()) + " : " + locRes.getLong().equals(testLoc.getLongitude()));
        }catch(JSONException e){
            Log.d("myTag", "json error " ,e);
        }
    }

    public static void getEmpSyncResponse(String addResponse){
        try {
            JSONObject responseObj = new JSONObject(addResponse);
            JSONArray responseArr = responseObj.getJSONArray("applications");
            for(int i=0; i<responseArr.length(); i++){
                JSONObject applicationObject = responseArr.getJSONObject(i);
                db.updateApplicationAcceptStatus(applicationObject.getString("accepted"), applicationObject.getString("id"));
                Application application = new Application(applicationObject.getString("title"),applicationObject.getString("subject"),applicationObject.getString("date"),applicationObject.getString("studentid"),applicationObject.getString("accepted"), applicationObject.getString("id"));
                showApplicationNotif(application,applicationObject.getString("accepted") ,i*i);
                String requestNotif = "{\"reciever\":\"EmpRead\", \"params\":{\"storeId\":\"" + db.getStore().getStoreId() +  "\",\"empId\":\"" + db.getUser().getEmployeeId() +  "\",\"seqId\":\"" + db.getNotifSeq("E") + "\"}}";
                Log.d("myTag", "requestNotif : " + requestNotif);
                postRequest = new Volley_Request();
                postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_notife), "POST", "loadNotifeSync",requestNotif);

            }
        }  catch(Exception e){
            Log.d("myTag", "json exception", e);
        }
    }

    public static void showApplicationNotif(Application application ,String acceptStatus,int id){
        Intent i = new Intent(mActivity, ApplicationNotifViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d("myTag", "application to disp : " + application.getAcceptStatus());
        i.putExtra("date", application.getDate());
        i.putExtra("title", application.getTitle());
        i.putExtra("subject", application.getSubject());
        i.putExtra("applicationid", application.getId());
        i.putExtra("acceptstatus", acceptStatus);
        PendingIntent pi = PendingIntent.getActivity(mActivity, 0, i, PendingIntent.FLAG_CANCEL_CURRENT);
        //Resources r = getResources();
        Notification notification = new NotificationCompat.Builder(mActivity)
                .setTicker(application.getAcceptStatus())
                .setSmallIcon(android.R.drawable.ic_menu_report_image)
                .setContentTitle(application.getTitle())
                .setContentText(application.getSubject())
                .setContentIntent(pi)
                .setAutoCancel(true)
                .build();

        NotificationManager notificationManager = (NotificationManager) mActivity.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(id, notification);
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
/*
    public static void requestNotifs(String studentid){
        String requestNotif = "{\"reciever\":\"load\", \"params\":{\"studentid\":\"" + studentid + "\"}}";
        Log.d("myTag", "requestNotif : " + requestNotif);
        postRequest = new Volley_Request();
        postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_notif), "POST", "loadNotifsSync",requestNotif);
    }

    public static void getloadNotifResponse(String responseString){
        try {
            Log.d("myTag", "got load notif resp : " + responseString);
            JSONObject responeObj = new JSONObject(responseString);
            JSONArray notifArr = responeObj.getJSONArray("notifs");
            for(int i=0; i< notifArr.length(); i++){
                JSONObject notifEle = (JSONObject) notifArr.get(i);
                Notif notifObj = new Notif(notifEle.getString("_id"),"5916b119aec2b708a0b960e1",notifEle.getString("title"),notifEle.getString("subject"),notifEle.getString("time"), studentId, notifEle.getInt("seqId"));
                db.addNotif(notifObj);
                showNotificationNotif(notifObj ,i*i);
                String applicationRequest = "{\"reciever\":\"studentsync\",\"params\":{\"studentid\":\""+ studentId +"\"}}";
                postRequest = new Volley_Request();
                postRequest.createRequest(mActivity, mActivity.getResources().getString(R.string.mJSONURL_appli), "POST", "SyncActivityStudent",applicationRequest);
            }}
        catch(Exception e){Log.d("myTag", "getloadNotifResponse : " , e);}
    }

*/

    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
