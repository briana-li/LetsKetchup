package edu.brandeis.cs.brianali.letsketchuptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import edu.brandeis.cs.brianali.letsketchuptest.ui.*;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(this, ChatListActivity.class);
        startActivity(intent);
    }
}
