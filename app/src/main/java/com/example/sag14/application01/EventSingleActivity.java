package com.example.sag14.application01;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnMenuTabSelectedListener;
import com.squareup.picasso.Picasso;

import java.util.GregorianCalendar;

public class EventSingleActivity extends AppCompatActivity {

private String mPost_key=null;
    private DatabaseReference mDatabase;
    private ImageView mEventSingleImage;
    private TextView mEventSingleTitle;
    private TextView mEventSingleTime;
    private TextView mEventSingleStartDate;
    private TextView mEventSingleEndDate;
    private Button mSingleRemoveBtn;
    private Button mGallery,mChat;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_single);
        mAuth=FirebaseAuth.getInstance();
        mSingleRemoveBtn=(Button) findViewById(R.id.singleRemoveBtn);
        mGallery=(Button) findViewById(R.id.nxtBtn);
        mChat=(Button) findViewById(R.id.chatBtn);
        mEventSingleImage=(ImageView)findViewById(R.id.singleEventImage);
        mEventSingleTitle=(TextView)findViewById(R.id.singleEventTitle);
        mEventSingleTime=(TextView)findViewById(R.id.singleEventTime);
        mEventSingleStartDate=(TextView)findViewById(R.id.singleStartDate);
        mEventSingleEndDate=(TextView)findViewById(R.id.singleEndDate);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Event");
        mPost_key = getIntent().getExtras().getString("blog_id");
        //Toast.makeText(EventSingleActivity.this,post_key, Toast.LENGTH_LONG).show();

        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chatIntent=new Intent (EventSingleActivity.this,Chat.class);
                startActivity(chatIntent);

            }
        });

        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            String post_title=(String)dataSnapshot.child("title").getValue();
            String post_time=(String)dataSnapshot.child("Settime").getValue();
            String post_startDate=(String)dataSnapshot.child("startdate").getValue();
            String post_endDate=(String)dataSnapshot.child("enddate").getValue();
            String post_image=(String)dataSnapshot.child("image").getValue();
            String post_uid=(String)dataSnapshot.child("uid").getValue();


            mEventSingleTitle.setText(post_title);
            mEventSingleEndDate.setText(post_endDate);
            mEventSingleStartDate.setText(post_startDate);
            mEventSingleTime.setText(post_time);
            Picasso.with(EventSingleActivity.this).load(post_image).into(mEventSingleImage);

            if(mAuth.getCurrentUser().getUid().equals(post_uid)){

                mSingleRemoveBtn.setVisibility(View.VISIBLE);

            }
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    });

        mSingleRemoveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               mDatabase.child(mPost_key).removeValue();
                Intent mainIntent=new Intent (EventSingleActivity.this,MainActivity.class);
                startActivity(mainIntent);
            }
        });

        mGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent singleEventIntent = new Intent(EventSingleActivity.this, Gallery.class);
                singleEventIntent.putExtra("blog_id", mPost_key);
                startActivity(singleEventIntent);
            }
        });







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_single,menu);


        return super.onCreateOptionsMenu(menu);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()==R.id.action_alert){

            //reIntent in = new Intent("my.action.string");
           // in.putExtra("state", mPost_key);
           // sendBroadcast(in);







        }




        return super.onOptionsItemSelected(item);


    }

    public void setAlarm(MenuItem item){
        Intent in = new Intent("my.action.string");
       in.putExtra("state", mPost_key);
        sendBroadcast(in);

        Long alertTime= new GregorianCalendar().getTimeInMillis()+5*1000;
        Intent alertIntent = new Intent(this,AlertReceiver.class);

        AlarmManager alarmManager = (AlarmManager)
                getSystemService(Context.ALARM_SERVICE);



        /*alarmManager.set(AlarmManager.RTC_WAKEUP,alertTime,
                PendingIntent.getBroadcast(this,1,alertIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT));*/

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, alertTime, AlarmManager.INTERVAL_DAY, PendingIntent.getBroadcast(this,1,alertIntent,
                PendingIntent.FLAG_UPDATE_CURRENT));

    }






}
