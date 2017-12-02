package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;

import edu.brandeis.cs.brianali.letsketchuptest.MainActivity;
import edu.brandeis.cs.brianali.letsketchuptest.R;

//created by Jonas Tjahjadi on 12/2/17

//SPECIAL NEW FEATURE WE DIDN'T IMPLEMENT IN CLASS
public class SplashActivity extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new Handler().postDelayed(new Runnable() {
            //splash screen w/ timer
            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
