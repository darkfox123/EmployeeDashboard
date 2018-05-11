package com.digitalhomeland.employeedashboard;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.digitalhomeland.employeedashboard.models.TaskInst;
import com.digitalhomeland.employeedashboard.models.Taskd;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Asus on 2/13/2018.
 */

public class TaskListAdapter  extends BaseAdapter implements ListAdapter {
    private List<TaskInst> tasksList  = new ArrayList<>();
    private Context mContext;

    public TaskListAdapter(Context mContext ,List<TaskInst> tskList) {
        this.mContext = mContext;
        this.tasksList = tskList;
    }

    @Override
    public int getCount() {
        return tasksList.size();
    }

    @Override
    public Object getItem(int position) {
        return tasksList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return tasksList.get(position).hashCode(); //fix it later
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater =  (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null) {
            view = inflater.inflate(R.layout.view_task_item, null);
        }
        //Handle TextView and display string from your list
        TextView taskTitleText = (TextView) view.findViewById(R.id.task_title);
        taskTitleText.setText(tasksList.get(position).getTitle() + " | ");
        TextView dateText = (TextView) view.findViewById(R.id.task_time);
        dateText.setText(tasksList.get(position).getHours() + ":" + tasksList.get(position).getMinutes());
        TextView statusText = (TextView) view.findViewById(R.id.task_status);
        Log.d("myTag", "task accept : " + tasksList.get(position).getAcceptedAt());
        if(tasksList.get(position).getAcceptedAt().equals("")) {
            statusText.setText("Todo");
        } else {
            statusText.setText("Done");
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Log.d("myTag", "notiflistadapter : " + tasksList.get(position).getId() + ":" + tasksList.get(position).getDate());
                Intent i = new Intent( context , TaskViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("Id", tasksList.get(position).getId());
                i.putExtra("type", tasksList.get(position).getType());
                context.startActivity(i);
            }
        });
        return view;
    }
}
