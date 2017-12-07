package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;




import java.io.IOException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


import edu.brandeis.cs.brianali.letsketchuptest.R;


public class CreateChat extends AppCompatActivity {
    private DatePicker date;
    private TimePicker time;
    private TextView title;
    private Button buttonTime;
    private Button buttonDate;
    private String finalDate;
    private String finalTime;
    private Calendar calendar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        date = (DatePicker) findViewById(R.id.datePicker);
        time = (TimePicker) findViewById(R.id.timePicker);
        title = (TextView) findViewById(R.id.title);
        buttonDate = (Button) findViewById(R.id.buttonDate);
        buttonDate.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finalDate = getDateFromDatePicker(date);
                timeLayout();

            }
        });
        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finalTime = (getTimeFromTimePicker(time));
                //makeEvent("");
                Intent naming = new Intent(CreateChat.this,ChatActivity.class);
                naming.putExtra("time",finalTime);
                naming.putExtra("date",finalDate);

                naming.putExtra("year",date.getYear());
                naming.putExtra("month",date.getMonth());
                naming.putExtra("day",date.getDayOfMonth());
                naming.putExtra("hour",time.getHour());
                naming.putExtra("minute",time.getMinute());
                startActivity(naming);
            }
        });



        dateLayout();
    }

    public String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String fullDate =  calendar.getTime().toString();
        String[] words = fullDate.split(" ");
        return words[0] + ", " + words[1] + " " + words[2];
    }

    public String getTimeFromTimePicker(TimePicker timePicker){
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();
        String ampm = " AM";
        if(hour > 12){
            hour -= 12;
            ampm = " PM";
        }
        String min = String.valueOf(minute);
        if(minute<10){
            min = "0"  +minute;
        }

        return hour + ":" + minute + ampm;
    }

    public void timeLayout(){
        title.setText(R.string.time_title);
        buttonDate.setVisibility(View.INVISIBLE);
        date.setVisibility(View.INVISIBLE);
        buttonTime.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);


    }

    public void dateLayout(){
        title.setText(R.string.date_title);
        buttonTime.setVisibility(View.INVISIBLE);
        time.setVisibility(View.INVISIBLE);
        buttonDate.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
    }



//    public static void addEvent(int hour, int minute, int year, int month, int day,  String summary) throws IOException {
//        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
//        mUser.
//        HttpTransport transport = AndroidHttp.newCompatibleTransport();
//        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
//
//        GoogleAccountCredential mCredential = GoogleAccountCredential.usingOAuth2(
//                getApplicationContext(), Arrays.asList(SCOPES))
//                .setBackOff(new ExponentialBackOff());
//
//        Event event = new Event();
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(year, month, day);
//
//        calendar.set(Calendar.HOUR,hour);
//        calendar.set(Calendar.MINUTE,minute);
//        Log.v("DATETIMESTRING", calendar.getTime().toString());
//        DateTime dateTime = new DateTime(calendar.getTime());
//        EventDateTime start = new EventDateTime()
//                .setDateTime(dateTime);
//        event.setStart(start);
//
//        event.setSummary(summary);
//        com.google.api.services.calendar.Calendar mService = new com.google.api.services.calendar.Calendar.Builder(
//                transport, jsonFactory, credential)
//                .setApplicationName("LetsKetchup")
//                .build();
//        event = service.events().insert("primary", event).execute();
//        System.out.printf("Event created: %s\n", event.getHtmlLink());
//    }

}
