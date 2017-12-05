package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import edu.brandeis.cs.brianali.letsketchuptest.MainActivity;
import edu.brandeis.cs.brianali.letsketchuptest.R;

/**
 * Created by stevenchen on 12/2/17.
 */

public class Credits extends AppCompatActivity {

    private Toolbar mToolBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.credits_activity);
        initializeScreen();
    }

    private void initializeScreen(){
        mToolBar = (Toolbar) findViewById(R.id.toolbar);
        mToolBar.setTitle("About Us");
        setSupportActionBar(mToolBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

