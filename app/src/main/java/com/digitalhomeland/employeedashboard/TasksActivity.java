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
import android.widget.ListView;
import android.widget.Toast;

import com.digitalhomeland.employeedashboard.models.TaskInst;
import com.digitalhomeland.employeedashboard.models.Taskd;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TasksActivity extends AppCompatActivity {

    DatabaseHandler db;
    private static ArrayList<Taskd> taskdList = new ArrayList<Taskd>();
    private static ArrayList<Taskd> taskwList = new ArrayList<Taskd>();
    private static ArrayList<Taskd> taskoList = new ArrayList<Taskd>();
    private static ArrayList<TaskInst> taskList = new ArrayList<TaskInst>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks);
        getWindow().setBackgroundDrawableResource(R.drawable.btr);
        db = new DatabaseHandler(this);
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yyyy");
        String calDate = df2.format(c.getTime());
        if(db.getTaskdByDate(calDate) != null){
        taskList = db.getTaskdByDate(calDate);}
        if(db.getTaskwByDate(calDate) != null){
            taskList.addAll(db.getTaskwByDate(calDate));}
        if(db.getTaskoByDate(calDate) != null){
            taskList.addAll(db.getTaskoByDate(calDate));}
        setListView(db);
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
        ListView listView = (ListView)findViewById(R.id.taskd_list_view);
        TaskListAdapter adapter = new TaskListAdapter(getApplicationContext(), taskList);
        listView.setAdapter(adapter);
        return taskdList.size();
    }
}
