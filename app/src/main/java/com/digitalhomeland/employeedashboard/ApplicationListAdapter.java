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

import com.digitalhomeland.employeedashboard.models.Application;

import java.util.ArrayList;

/**
 * Created by prthma on 14-06-2017.
 */

public class ApplicationListAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<Application> applicationList  = new ArrayList<>();
    private String parentId = "";
    private String request = "";
    private String mJSONURL = "https://fast-shelf-51581.herokuapp.com/api/parents";
    private Context mContext;
    private Context context;
    DatabaseHandler db;

    public ApplicationListAdapter(Context mContext , ArrayList<Application> applicationList, Context context) {
        this.mContext = mContext;
        this.applicationList = applicationList;
        this.context = context;
        db = new DatabaseHandler(mContext);
    }

    @Override
    public int getCount() {
        return applicationList.size();
    }

    @Override
    public Object getItem(int position) {
        return applicationList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return applicationList.get(position).hashCode(); //fix it later
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater =  (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(view == null) {
            view = inflater.inflate(R.layout.view_appli_list_item, null);
        }
        //Handle TextView and display string from your list
      //  Student student = db.getStudentById(applicationList.get(position).getStudentId());
        Log.d("myTag", "application rendred : " + applicationList.get(position).getDate() + " : " + applicationList.get(position).getTitle() + " : " + applicationList.get(position).getSubject());
        TextView studentNameText = (TextView) view.findViewById(R.id.task_title);
        studentNameText.setText(applicationList.get(position).getDate());
        TextView studentRollNoText = (TextView) view.findViewById(R.id.task_subject);
        studentRollNoText.setText(applicationList.get(position).getTitle());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = v.getContext();
                Intent i = new Intent( context , ApplicationNotifViewActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("date", applicationList.get(position).getDate() );
                i.putExtra("title",applicationList.get(position).getTitle() );
                i.putExtra("subject",applicationList.get(position).getSubject() );
                i.putExtra("applicationid", applicationList.get(position).getId());
                i.putExtra("studentid",applicationList.get(position).getEmployeeId());
                i.putExtra("acceptstatus",applicationList.get(position).getAcceptStatus());
                mContext.startActivity(i);
            }
        });
        return view;
    }
}
