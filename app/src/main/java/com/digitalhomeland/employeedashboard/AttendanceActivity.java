package com.digitalhomeland.employeedashboard;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.digitalhomeland.employeedashboard.models.EmpRoster;
import com.digitalhomeland.employeedashboard.models.StRoster;
import com.digitalhomeland.employeedashboard.models.Taskd;
import com.digitalhomeland.employeedashboard.models.Tasko;
import com.digitalhomeland.employeedashboard.models.Taskw;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class AttendanceActivity extends AppCompatActivity implements LocationListener {

    private int progressStatus = 0;
    private Handler handler = new Handler();
    LocationManager locationManager;
    static Location testLoc;
    static Address testAddress;
    static DatabaseHandler db;
    private static Volley_Request postRequest;
    static Context mContext;
    static Activity mActivity;
    String action;
    static String checkTime;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        db = new DatabaseHandler(this);
        mContext = this;
        mActivity = AttendanceActivity.this;
        Bundle bundle = getIntent().getExtras();
        action = bundle.getString("action");
        final LinearLayout rl = (LinearLayout) findViewById(R.id.r1);
        final Button btn = (Button) findViewById(R.id.btn);
        final TextView tv = (TextView) findViewById(R.id.tv);
        final ProgressBar pb = (ProgressBar) findViewById(R.id.pb);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the progress status zero on each button click
                progressStatus = 0;
                getLocation();
                // Start the lengthy operation in a background thread
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while(progressStatus < 100){
                            // Update the progress status
                            progressStatus +=1;

                            // Try to sleep the thread for 20 milliseconds
                            try{
                                Thread.sleep(30);
                            }catch(InterruptedException e){
                                e.printStackTrace();
                            }

                            // Update the progress bar
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    pb.setProgress(progressStatus);
                                    // Show the progress on TextView
                                    tv.setText(progressStatus+"");
                                    // If task execution completed
                                    if(progressStatus == 100){
                                        // Set a message of completion
                                        tv.setText("Operation completed.");
                                    }
                                }
                            });
                        }
                    }
                }).start(); // Start the operation
            }
        });
    }

    void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, (LocationListener) this);
        }
        catch(SecurityException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        //  locationText.setText("Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude());
        testLoc = location;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Log.d("myTag","Latitude: " + location.getLatitude() + "\n Longitude: " + location.getLongitude() + "\n" + addresses.get(0).getAddressLine(0)+" : "+
                    addresses.get(0).getAddressLine(1)+" : "+addresses.get(0).getAddressLine(2) );
            testAddress = addresses.get(0);
            Log.d("myTag", addresses.get(0).getAddressLine(0) + " : " + db.getStore().getAddress().toString());
            //if((((location.getLatitude()*1.00)-1 <= (Double.parseDouble(db.getStore().getLat()) * 1.00)) || ((location.getLatitude()*1.00)-1 >= (Double.parseDouble(db.getStore().getLat()) * 1.00))) && (((location.getLongitude()*1.00)-1 <= (Double.parseDouble(db.getStore().getLongi()) * 1.00)) || ((location.getLongitude()*1.00)-1 >= (Double.parseDouble(db.getStore().getLongi()) * 1.00)))){
              if(true){
                  Calendar c = Calendar.getInstance();
                  checkTime = Utils.getCurrentTime();
                String currentDate = Utils.getCurrentDate();
                String req;
                  /*
                  int i;
                  for(i=0; i<3; i++) {
                      c.add(Calendar.DAY_OF_WEEK, i);
                      SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
                      String currentDate = df.format(c.getTime());
                      */
                      if (action.equals("checkIn") && db.getEmpRosterByDate(Utils.getCurrentDate()).getCheckIn().equals("")) {
                          // req = "{\"reciever\":\"checkIn\",\"params\":{\"storeId\":\"" + db.getStore().getStoreId() + "\",\"empId\":\"" + db.getUser().getEmployeeId() + "\",\"date\":\"" + currentDate + "\",\"checkIn\":\"" + checkTime + "\"}}";
                          req = "{\"reciever\":\"CheckIn\",\"params\":{\"storeId\":\"" + db.getStore().getStoreId() + "\",\"empId\":\"" + db.getUser().getEmployeeId() + "\",\"date\":\"" + currentDate + "\",\"checkIn\":\"" + checkTime + "\"}}";
                          postRequest = new Volley_Request();
                          postRequest.createRequest(mContext, getResources().getString(R.string.mJSONURL_emproster), "POST", "CheckIn", req);
                      } else if (action.equals("checkOut") && db.getEmpRosterByDate(Utils.getCurrentDate()).getCheckOut().equals("")) {
                          req = "{\"reciever\":\"CheckOut\",\"params\":{\"storeId\":\"" + db.getStore().getStoreId() + "\",\"empId\":\"" + db.getUser().getEmployeeId() + "\",\"date\":\"" + currentDate + "\",\"checkOut\":\"" + checkTime + "\"}}";
                          postRequest = new Volley_Request();
                          postRequest.createRequest(mContext, getResources().getString(R.string.mJSONURL_emproster), "POST", "CheckOut", req);
                      }
                  //}
                  locationManager.removeUpdates((LocationListener) this);
                  locationManager = null;
              }
        }catch(Exception e)
        {
Log.d("myTag", " Error : ", e);
        }
    }

    public static void checkInResponse(String response){
        if(db.getEmpRosterByDate(Utils.getCurrentDate()) != null) {
            EmpRoster emr = db.getEmpRosterByDate(Utils.getCurrentDate());
            db.updateCheckIn(emr.getEmpId(), checkTime);
            emr.setCheckIn(checkTime);
        }
        db.deleteTasks();
        Log.d("myTag", "taskd count : " + db.getTaskdCount());
        //String req = "{\"reciever\":\"getEmp\", \"params\" :{ \"employeeId\":\""+ db.getUser().getEmployeeId() + "\" , \"storeId\":\"" + db.getStore().getStoreId() + "\"}}";
        String req = "{\"reciever\":\"getEmp\", \"params\" :{ \"employeeId\":\""+ db.getUser().getEmployeeId() + "\" , \"storeId\":\"" + db.getStore().getStoreId() + "\" , \"date\":\"" + Utils.getCurrentDate() + "\"}}";
        postRequest = new Volley_Request();
        postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_taskd), "POST", "loadTD", req);
    }

    public static void loadTdResponse(String response){
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String calDate = df2.format(c.getTime());
            JSONObject empO = new JSONObject(response);
            JSONArray empArr = empO.getJSONArray("taskds");
            //ArrayList<Taskd> taskList = db.getAllEmployees();
            ArrayList<Taskd> tdList = new ArrayList<>();
            for(int i=0; i<empArr.length(); i++){
                JSONObject tempEmp = (JSONObject) empArr.get(i);
                    Taskd taskd = new Taskd(tempEmp.getString("_id"),calDate,tempEmp.getString("title"),tempEmp.getString("subject"),tempEmp.getString("empId"),tempEmp.getString("hours"),tempEmp.getString("minutes"), tempEmp.getString("seqId"));
                    db.addTaskd(taskd);
                setAlarmTaskd(taskd.getHours(), taskd.getMinutes(), "taskd", taskd.get_id());
                }
            //Log.d("myTag", "empcount : " + db.getTaskdCount());
            String[] days = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
            String req = "{\"reciever\":\"getEmp\", \"params\" :{ \"employeeId\":\""+ db.getUser().getEmployeeId() + "\" , \"storeId\":\"" + db.getStore().getStoreId() + "\" , \"date\":\"" + Utils.getCurrentDate() + "\" , \"dayOfW\":\"" + "Monday" + "\"}}";
            //String req = "{\"reciever\":\"getEmp\", \"params\" :{ \"employeeId\":\""+ db.getUser().getEmployeeId() + "\" , \"storeId\":\"" + db.getStore().getStoreId() + "\" , \"dayOfW\":\"" + days[c.DAY_OF_WEEK] + "\"}}";
            postRequest = new Volley_Request();
            postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_taskw), "POST", "loadTW", req);
        }catch(JSONException e){
            Log.d("myTag", "error : " + e, e);
            e.printStackTrace();}
    }

    public static void loadTwResponse(String response){
        try {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
            String calDate = df2.format(c.getTime());
            JSONObject empO = new JSONObject(response);
            JSONArray empArr = empO.getJSONArray("taskws");
            //ArrayList<Taskd> taskList = db.getAllEmployees();
            ArrayList<Taskw> tdList = new ArrayList<>();
            for(int i=0; i<empArr.length(); i++){
                JSONObject tempEmp = (JSONObject) empArr.get(i);
                Taskw taskw = new Taskw(tempEmp.getString("_id"),calDate,tempEmp.getString("title"),tempEmp.getString("subject"),tempEmp.getString("empId"),tempEmp.getString("hours"),tempEmp.getString("minutes"),tempEmp.getString("dayOfW"),tempEmp.getString("seqId"));
                db.addTaskw(taskw);
                setAlarmTaskd(taskw.getHours(), taskw.getMinutes(),"taskw", taskw.getId());
            }
            SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            String curDate = df.format(c.getTime());
            String req = "{\"reciever\":\"getEmp\", \"params\" :{ \"employeeId\":\""+ db.getUser().getEmployeeId() + "\" , \"storeId\":\"" + db.getStore().getStoreId() + "\" , \"dateToSet\":\"" + curDate + "\"}}";
            postRequest = new Volley_Request();
            postRequest.createRequest(mContext, mContext.getResources().getString(R.string.mJSONURL_tasko), "POST", "loadTO", req);
        }catch(JSONException e){
            Log.d("myTag", "error : " + e, e);
            e.printStackTrace();}
    }

    public static void loadToResponse(String response){
        try {
            JSONObject empO = new JSONObject(response);
            JSONArray empArr = empO.getJSONArray("taskos");
            //ArrayList<Taskd> taskList = db.getAllEmployees();
            ArrayList<Tasko> tdList = new ArrayList<>();
            for(int i=0; i<empArr.length(); i++){
                JSONObject tempEmp = (JSONObject) empArr.get(i);
                Tasko tasko = new Tasko(tempEmp.getString("_id"),tempEmp.getString("title"),tempEmp.getString("subject"),tempEmp.getString("empId"),tempEmp.getString("hours"),tempEmp.getString("minutes"),tempEmp.getString("dateToSet"),"",tempEmp.getString("seqId"));
                db.addTasko(tasko);
                setAlarmTaskd(tasko.getHours(), tasko.getMinutes(), "tasko", tasko.getId());
            }
            Log.d("myTag", "Transaction Complete");
        }catch(JSONException e){
            Log.d("myTag", "error : " + e, e);
            e.printStackTrace();}
    }

    public static void setAlarmTaskd(String hours, String minutes, String type, String id){
        // Launch Time Picker Dialog
                        Log.d("myTag", hours + " : " + minutes);
                        Calendar c = Calendar.getInstance();
                        c.set(Calendar.HOUR_OF_DAY,Integer.parseInt(hours));
                        c.set(Calendar.MINUTE,Integer.parseInt(minutes));

                        Intent intent = new Intent(mContext, Mote.class);
        intent.putExtra("type", type);
        intent.putExtra("id", id);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

                        AlarmManager alarmManager = (AlarmManager) mActivity.getSystemService(ALARM_SERVICE);

                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_FIFTEEN_MINUTES,pendingIntent );
                        Toast.makeText(mActivity, "Alarm Set.", Toast.LENGTH_LONG).show();
    }

    public static void checkOutResponse(String response){
        if(db.getEmpRosterByDate(Utils.getCurrentDate()) != null) {
            EmpRoster emr = db.getEmpRosterByDate(Utils.getCurrentDate());
            db.updateCheckOut(emr.getEmpId(), checkTime);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(AttendanceActivity.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

}
