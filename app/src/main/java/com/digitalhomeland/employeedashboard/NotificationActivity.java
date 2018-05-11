package com.digitalhomeland.employeedashboard;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.digitalhomeland.employeedashboard.models.Notif;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    static int LIMIT = 4;
    static  int OFFSET = 0;
    static int prevClicksAllowed ;
    static int nextClicksAllowed;

    Button nextNotification;
    Button prevNotification;
    DatabaseHandler db;
    private static ArrayList<Notif> notifList = new ArrayList<Notif>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        //set bkg
        getWindow().setBackgroundDrawableResource(R.drawable.btr);
        nextNotification = (Button) findViewById(R.id.next_notification_button);
        prevNotification = (Button) findViewById(R.id.prev_notification_button);
        db = new DatabaseHandler(this);
        int totalRows = db.getNotifCount();
        notifList = db.getAllNotifications(LIMIT, OFFSET);
        Log.d("myTag", "totalRows notif : " + totalRows + " : " + totalRows%LIMIT + " : " + totalRows/LIMIT);
        for(int i =0; i< notifList.size(); i++){
            Log.d("myTag", "notiflist : " + notifList.get(i).getDate());
        }
        if(totalRows%LIMIT > 0){
            prevClicksAllowed = totalRows/LIMIT;
        }else {
            prevClicksAllowed = totalRows/LIMIT -1;
        }
        nextClicksAllowed = 0;
        setListView(db);
        if(totalRows/LIMIT == 0){
            prevNotification.setVisibility(View.INVISIBLE);
            nextNotification.setVisibility(View.INVISIBLE);
        }
        else {
            prevNotification.setVisibility(View.VISIBLE);
            nextNotification.setVisibility(View.INVISIBLE);
        }

        prevNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                OFFSET += LIMIT;
                setListView(db);
                prevClicksAllowed--;
                nextClicksAllowed++;
                Log.d("myTag", "prev notif : " + prevClicksAllowed + " : " + " next notif : " + nextClicksAllowed);
                if(prevClicksAllowed == 0){
                    prevNotification.setVisibility(View.INVISIBLE);
                }
                if(nextClicksAllowed > 0){
                    nextNotification.setVisibility(View.VISIBLE);
                }
            }
        });

        nextNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //intent for Create Notification Page
                OFFSET -= LIMIT;
                setListView(db);
                prevClicksAllowed++;
                nextClicksAllowed--;
                Log.d("myTag", "prev notif: " + prevClicksAllowed + " : " + " next notif: " + nextClicksAllowed);
                if(nextClicksAllowed == 0){
                    nextNotification.setVisibility(View.INVISIBLE);
                }
                if(prevClicksAllowed > 0){
                    prevNotification.setVisibility(View.VISIBLE);
                }
            }
        });
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



    public int setListView(DatabaseHandler db){
        List<Notif> notifSubList;
        if(OFFSET + LIMIT < notifList.size())
            notifSubList = notifList.subList(OFFSET,OFFSET + LIMIT );
        else notifSubList = notifList.subList(OFFSET,notifList.size() );
        ListView listView = (ListView)findViewById(R.id.notif_list_view);
        NotifListAdapter adapter = new NotifListAdapter(getApplicationContext(), notifSubList, this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                Notif clickedObj = (Notif) parent.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(),
                        NotificationView.class);
                Log.d("myTag", "selected notif date : " + clickedObj.getDate());
                i.putExtra("date", clickedObj.getDate());
                i.putExtra("title", clickedObj.getTitle());
                i.putExtra("subject", clickedObj.getSubject());
                startActivity(i);

            }});
        return notifList.size();
    }
}
