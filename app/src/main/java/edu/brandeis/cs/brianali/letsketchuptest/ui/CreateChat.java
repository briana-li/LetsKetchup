package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.util.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.model.EventDateTime;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

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


    private static final String APPLICATION_NAME =
            "LetsKetchup";

    /** Directory to store user credentials for this application. */
    private static final java.io.File DATA_STORE_DIR = new java.io.File(
            System.getProperty("user.home"), ".credentials/calendar-java-quickstart");

    /** Global instance of the {@link FileDataStoreFactory}. */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /** Global instance of the JSON factory. */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /** Global instance of the HTTP transport. */
    private static HttpTransport HTTP_TRANSPORT;

    /** Global instance of the scopes required by this quickstart.
     *
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR);

//    static {
//        try {
//            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
//        } catch (Throwable t) {
//            t.printStackTrace();
//            System.exit(1);
//        }
//    }

    public static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                CreateChat.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .setAccessType("offline")
                        .build();
        Credential credential = new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("user");
        return credential;
    }

    public static com.google.api.services.calendar.Calendar
    getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public void makeEvent( String summary) throws IOException{
        Event event = new Event();
        event.setSummary(summary);
        calendar.set(Calendar.HOUR,time.getHour());
        calendar.set(Calendar.MINUTE,time.getMinute());
        Log.v("DATETIMESTRING", calendar.getTime().toString());
        DateTime dateTime = new DateTime(calendar.getTime());
        EventDateTime start = new EventDateTime()
                .setDateTime(dateTime);
        event.setStart(start);

        com.google.api.services.calendar.Calendar service =
                getCalendarService();
        event = service.events().insert("primary", event).execute();
        System.out.printf("Event created: %s\n", event.getHtmlLink());
    }

}
