package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import edu.brandeis.cs.brianali.letsketchuptest.MainActivity;
import edu.brandeis.cs.brianali.letsketchuptest.R;

/**
 * Created by stevenchen on 11/29/17.
 */

public class AboutActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
