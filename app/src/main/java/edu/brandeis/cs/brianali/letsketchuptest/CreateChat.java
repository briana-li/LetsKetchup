package edu.brandeis.cs.brianali.letsketchuptest;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import static android.R.attr.button;

public class CreateChat extends AppCompatActivity {
    private DatePicker date;
    private TimePicker time;
    private TextView title;
    private Button buttonTime;
    private Button buttonDate;
    private String finalDate;
    private String finalTime;
    private String[] friends;


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
                finalDate = date.toString();
                timeLayout();

            }
        });
        buttonTime = (Button) findViewById(R.id.buttonTime);
        buttonTime.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                finalTime = time.toString();
                startActivityForResult(new Intent(CreateChat.this,FriendSelectChat.class),1);
            }
        });



        dateLayout();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                friends = data.getStringArrayExtra("friends");
                String notes = data.getStringExtra("notes");


                Intent values = new Intent();
                values.putExtra("date",finalDate);
                values.putExtra("time",finalTime);
                values.putExtra("friends",friends);

                setResult(RESULT_OK,values);
                finish();
            }
        }
    }
}
