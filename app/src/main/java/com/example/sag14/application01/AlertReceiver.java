package com.example.sag14.application01;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationBuilderWithBuilderAccessor;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by sag14 on 05/12/16.
 */

public class AlertReceiver extends BroadcastReceiver {

    private DatabaseReference mDatabase;
    @Override
    public void onReceive(final Context context, Intent intent) {
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Event");
        String action = intent.getAction();

        Log.i("Receiver", "Broadcast received: " + action);

        if(action.equals("my.action.string")) {
             String  state = intent.getExtras().getString("state");

            Log.i("postkey", state);
            mDatabase.child(state).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String post_title=(String)dataSnapshot.child("title").getValue();
                    String post_time=(String)dataSnapshot.child("Settime").getValue();
                    String post_startDate=(String)dataSnapshot.child("startdate").getValue();
                    String post_endDate=(String)dataSnapshot.child("enddate").getValue();
                    String Time="Start"+post_startDate+"End"+post_endDate;

                        createNotification(context,post_title,post_time,Time);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }

    public void createNotification(Context context,String msg,String msgText,String msgAlert){

        PendingIntent notification = PendingIntent.getActivity(context,0,
                new Intent(context,EventSingleActivity.class),0);

        NotificationCompat.Builder mBuilder =  new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(msg)
                .setTicker(msgAlert)
                .setContentText(msgText);

        mBuilder.setContentIntent(notification);
        mBuilder.setDefaults(NotificationCompat.DEFAULT_SOUND);
        mBuilder.setAutoCancel(true);

        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(1,mBuilder.build());

    }
}
