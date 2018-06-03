package com.digitalhomeland.employeedashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digitalhomeland.employeedashboard.models.EmpRoster;
import com.digitalhomeland.employeedashboard.models.StRoster;

import java.util.ArrayList;

/**
 * Created by Asus on 3/10/2018.
 */

public class RooasterWeeklyListAdapter extends BaseAdapter implements ListAdapter {
    static String[] dateList = new String[7];
    static String[] colorList = new String[7];
    static String[] dayofW = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
    private Context mContext;
    public static Activity mActivity;
    DatabaseHandler db;

    public RooasterWeeklyListAdapter(Context mContext, String[] dateList, String[] colorList, Activity mActivity) {
        this.mContext = mContext;
        this.dateList = dateList;
        this.colorList = colorList;
        this.mActivity = mActivity;
        db = new DatabaseHandler(mContext);
    }

    @Override
    public int getCount() {
        return dateList.length;
    }

    @Override
    public Object getItem(int position) {
        return dateList[position];
    }

    @Override
    public long getItemId(int position) {
        return dateList[position].hashCode(); //fix it later
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.emp_weekly_roster_item, null);

        }
        DatabaseHandler db = new DatabaseHandler(mContext);
        //Handle TextView and display string from your list
        if(dateList[position].equals(" ")) { return view;}
        final StRoster str = db.getSTRosterByDate(dateList[position]);
        final EmpRoster emr = db.getEmpRosterByDate(dateList[position]);
        Log.d("myTag", "str : " + str.getStoreStatus());
        Log.d("myTag", "emr : " + emr.getEmpStatus());
        //RelativeLayout listItem = (RelativeLayout) view.findViewById(R.id.list_item);
        TextView dateText = (TextView) view.findViewById(R.id.week_day);
        dateText.setText(dateList[position] + " | " + dayofW[position] + "  " + str.getStoreStatus());
        TextView dutyStatus = (TextView) view.findViewById(R.id.duty_status);
        dutyStatus.setText(str.getStoreStatus());
        TextView shiftVal = (TextView) view.findViewById(R.id.shift_val);
        TextView events = (TextView) view.findViewById(R.id.event_status);
        Button detailsBtn = (Button) view.findViewById(R.id.event_details_btn);
        final TextView eventTitle = (TextView) view.findViewById(R.id.event_title);
        final TextView eventSub = (TextView) view.findViewById(R.id.event_subject);
        final LinearLayout eventDetailsLayout = (LinearLayout) view.findViewById(R.id.event_details_layout);
        LinearLayout shiftLayout = (LinearLayout) view.findViewById(R.id.shift_layout);
        LinearLayout eventsLayout = (LinearLayout) view.findViewById(R.id.event_layout);


        if(str.getStoreStatus().equals("Closed")){
            //dutyStatus.setTextColor(Color.);
            shiftLayout.setVisibility(View.GONE);
            eventsLayout.setVisibility(View.GONE);
            eventDetailsLayout.setVisibility(View.GONE);
        } else if (str.getStoreStatus().equals("Open")){
            //dutyStatus.setTextColor(Color.GREEN);
            shiftLayout.setVisibility(View.VISIBLE);
            shiftVal.setText(emr.getShift());
            eventDetailsLayout.setVisibility(View.GONE);
        }

        if(str.getEvents().equals("None")){
            detailsBtn.setVisibility(View.GONE);
            eventDetailsLayout.setVisibility(View.GONE);
            events.setText("None");
        } else {
            detailsBtn.setVisibility(View.VISIBLE);
            events.setText("Yes");
            eventTitle.setText(str.getEventTitle());
            eventSub.setText(str.getEventSub());
            eventsLayout.setVisibility(View.VISIBLE);
        }
        dutyStatus.setText(emr.getEmpStatus());
        shiftVal.setText(emr.getShift());

        detailsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventDetailsLayout.setVisibility(View.VISIBLE);
            }
        });

        dutyStatus.setText(emr.getEmpStatus());
        dutyStatus.setEnabled(false);

         dateText.setTextColor(Color.GREEN);
            view.setBackgroundResource(R.drawable.backwithbordergreen);

        return view;
    }
}