package edu.brandeis.cs.brianali.letsketchuptest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

public class CreateChat extends AppCompatActivity {
    private DatePicker date;
    private TimePicker time;
    private TextView title;
    private Button buttonTime;
    private Button buttonDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chat);

        date = (DatePicker) findViewById(R.id.datePicker);
        time = (TimePicker) findViewById(R.id.timePicker);
        title = (TextView) findViewById(R.id.title);


    }

    public void timeLayout(){
        title.setText(R.string.time_title);
        date.setVisibility(View.GONE);
        time.setVisibility(View.VISIBLE);

    }

    public void dateLayout(){
        title.setText(R.string.date_title);
        time.setVisibility(View.GONE);
        date.setVisibility(View.VISIBLE);
    }
}
