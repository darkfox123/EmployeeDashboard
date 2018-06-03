package com.digitalhomeland.employeedashboard;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewRoster extends AppCompatActivity {

    Button nextNotification;
    Button prevNotification;
    DatabaseHandler db;
    static String[] dateList = new String[7];
    static String[] colorList = new String[7];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_roster);
        getWindow().setBackgroundDrawableResource(R.drawable.btr);
        nextNotification = (Button) findViewById(R.id.next_notification_button);
        prevNotification = (Button) findViewById(R.id.prev_notification_button);
        db = new DatabaseHandler(this);
        // setListView(db);
        ListView listView = (ListView) findViewById(R.id.weekly_list_view);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = df2.format(c.getTime());
        for (int i = 0; i < 7; i++) {
            if(db.getSTRosterByDate(df2.format(c.getTime())) != null) {
                dateList[i] = df2.format(c.getTime());
            } else {
                dateList[i] = " ";
            }
            c.add(Calendar.DAY_OF_WEEK, 1);
        }
        RooasterWeeklyListAdapter adapter = new RooasterWeeklyListAdapter(getApplicationContext(), dateList, colorList, ViewRoster.this);
        listView.setAdapter(adapter);

    }

    public static void setWeek(Calendar c, SimpleDateFormat sdf) {
        int dayOfWeek = (c.get(Calendar.DAY_OF_WEEK));
        //dayOfWeek -= 1;
        Calendar tempC = c;
        for (int i = 1; i < 8; i++) {
            if (dayOfWeek == i) {
                dateList[i - 1] = sdf.format(tempC.getTime());
                colorList[i - 1] = "GREEN";
            }
            if (dayOfWeek < i) {
                Log.d("myTag", "date fut : " + tempC.getTime());
                tempC.add(Calendar.DAY_OF_MONTH, i - dayOfWeek);
                Log.d("myTag", "date fut : " + tempC.getTime());
                dateList[i - 1] = sdf.format(tempC.getTime());
                colorList[i - 1] = "YELLOW";
                tempC.add(Calendar.DAY_OF_MONTH, -(i - dayOfWeek));
                Log.d("myTag", "date fut reset : " + tempC.getTime());
            }
            if (dayOfWeek > i) {
                tempC.add(Calendar.DAY_OF_MONTH, (i - dayOfWeek));
                Log.d("myTag", "date past : " + tempC.getTime());
                dateList[i - 1] = sdf.format(tempC.getTime());
                colorList[i - 1] = "RED";
                tempC.add(Calendar.DAY_OF_MONTH, -(i - dayOfWeek));
                Log.d("myTag", "date past reset : " + tempC.getTime());
            }
        }
    }
}
