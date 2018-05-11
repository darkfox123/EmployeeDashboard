package com.digitalhomeland.employeedashboard;

/**
 * Created by Asus on 2/2/2018.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class Mote extends BroadcastReceiver{

    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub

        Bundle bundle = intent.getExtras();
        String type = bundle.getString("type");
        String id = bundle.getString("id");
        Toast.makeText(context, "Alarm worked.", Toast.LENGTH_LONG).show();
        final Calendar c = Calendar.getInstance();
        long mHour = c.get(Calendar.HOUR_OF_DAY);
        long mMinute = c.get(Calendar.MINUTE);
        Log.d("myTag", "Alarm executed at : " + mHour + ":" + mMinute);

        Intent alarmIntent = new Intent("android.intent.action.MAIN");
        alarmIntent.setClass(context, AlertDailogActivity.class);
        alarmIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("hour", mHour);
        intent.putExtra("minutes", mMinute);
        intent.putExtra("type",type);
        intent.putExtra("id",id);
        // Start the popup activity
        context.startActivity(alarmIntent);
    }



}