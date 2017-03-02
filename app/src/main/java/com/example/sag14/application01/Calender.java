package com.example.sag14.application01;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class Calender extends AppCompatActivity {
    private String mEvent_Date=null;
    private FirebaseAuth mAuth;
    private String uid;
    CompactCalendarView compactCalendar;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);
        mAuth=FirebaseAuth.getInstance();
        uid=mAuth.getCurrentUser().getUid();
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setTitle(null);
        compactCalendar = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendar.setUseThreeLetterAbbreviation(true);

       // mEvent_Date = getIntent().getExtras().getString("eventDate");
        Toast.makeText(getApplicationContext(),mEvent_Date,Toast.LENGTH_LONG).show();

        if(uid.equals("iX7pq4oDHFNdgKTMhd1VKGfMlRA2")) {
            Event ev1 = new Event(Color.RED, 1483401600000L, "BEBE REXHA");
            compactCalendar.addEvent(ev1);
            Event ev2 = new Event(Color.RED, 1484524800000L, "MOON");
            compactCalendar.addEvent(ev2);
            Event ev3 = new Event(Color.RED, 1483228800000L, "DOMINOS");
            compactCalendar.addEvent(ev3);
        }

        if(uid.equals("yIi2mrAtOGMU7ZP4AkjN6xjyNfL2")) {
            Event ev1 = new Event(Color.RED, 1484006400000L, "MUSIC");
            compactCalendar.addEvent(ev1);
            Event ev2 = new Event(Color.RED, 1484524800000L, "MOON");
            compactCalendar.addEvent(ev2);

        }








        compactCalendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                Context context = getApplicationContext();


              //  if (dateClicked.toString().compareTo("Tue Jan 03 00:00:00 AST 2016 ") == 0) {
                 //   Toast.makeText(context, "BEBE REHXA", Toast.LENGTH_SHORT).show();
               // } else {
                   // Toast.makeText(context, "No Events Planned for that day", Toast.LENGTH_SHORT).show();
               // }
            }




            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                    actionBar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });


    }
}
