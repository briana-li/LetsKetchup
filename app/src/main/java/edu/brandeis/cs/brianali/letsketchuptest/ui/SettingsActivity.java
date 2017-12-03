package edu.brandeis.cs.brianali.letsketchuptest.ui;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.SeekBar;
import android.widget.Switch;

import edu.brandeis.cs.brianali.letsketchuptest.MainActivity;
import edu.brandeis.cs.brianali.letsketchuptest.R;

/**
 * Created by stevenchen on 11/29/17.
 */

public class SettingsActivity extends AppCompatActivity {

    private Switch notifications;
    private SeekBar volumeSeekbar = null;
    private AudioManager audioManager = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        notifications = (Switch) findViewById(R.id.not_switch);
        initControls();
    }

    private void initControls() {
        try {
            volumeSeekbar = (SeekBar) findViewById(R.id.seekBar1);
            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
            volumeSeekbar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volumeSeekbar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            volumeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onStopTrackingTouch(SeekBar arg0) {

                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0) {

                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void notifMessage(View view){
        if (notifications.isChecked()){
            //turn on notifications
        } else {
            //turn off notifications
        }
    }

    public void saveMessage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void cancelMessage(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
