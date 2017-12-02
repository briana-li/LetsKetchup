package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.util.Calendar;
import java.util.Date;

import edu.brandeis.cs.brianali.letsketchuptest.R;

public class CreateChat extends AppCompatActivity {
    private DatePicker date;
    private TimePicker time;
    private TextView title;
    private Button buttonTime;
    private Button buttonDate;
    private String finalDate;
    private String finalTime;



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
                Intent naming = new Intent(CreateChat.this,ChatActivity.class);
                naming.putExtra("time",finalTime);
                naming.putExtra("date",finalDate);
                startActivity(naming);
            }
        });



        dateLayout();
    }

    public static String getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        String fullDate =  calendar.getTime().toString();
        String[] words = fullDate.split(" ");
        return words[0] + ", " + words[1] + " " + words[2];
    }

    public static String getTimeFromTimePicker(TimePicker timePicker){
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
       // title.setText(R.string.time_title);
        buttonDate.setVisibility(View.GONE);
        date.setVisibility(View.GONE);
        buttonTime.setVisibility(View.VISIBLE);
        time.setVisibility(View.VISIBLE);


    }

    public void dateLayout(){
      //  title.setText(R.string.date_title);
        buttonTime.setVisibility(View.GONE);
        time.setVisibility(View.GONE);
        buttonDate.setVisibility(View.VISIBLE);
        date.setVisibility(View.VISIBLE);
    }

}
