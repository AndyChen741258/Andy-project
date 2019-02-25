package com.radaee.Broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.radaee.reader.PDFLayoutView;

public class MyBroadcast extends BroadcastReceiver {
    public static String StudentsName;
    public static String ShareStudentsName;
    public static String Topic;
    public static String StudentSelectedName;
    @Override
    public void onReceive(Context context, Intent intent) {
        String name = intent.getStringExtra("name");
        String share = intent.getStringExtra("ShareStudentsName");
        String topic = intent.getStringExtra("Topic");
        StudentSelectedName = intent.getStringExtra("StudentSelectedName");
        StudentsName = name;
        ShareStudentsName = share;
        Topic = topic;
        PDFLayoutView.WordNum ="學生"+name+"號";
        Toast.makeText(context, StudentSelectedName, Toast.LENGTH_SHORT).show();
        Log.d("Broadcast",name);
    }
}
