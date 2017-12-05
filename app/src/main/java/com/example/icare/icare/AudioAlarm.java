/*
 * Copyright (c) 2017.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.example.icare.icare;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by alex on 05/12/17.
 */

public class AudioAlarm {

    /* variables to store audio reminder of drug */

    String audioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    MediaPlayer mediaPlayer ;
    public static final int MICROPHONE_AUDIO_REQUEST = 1;
    AppCompatActivity app;

    public AudioAlarm(AppCompatActivity app, String fileName) {
        this.app = app;
        this.audioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + fileName + ".3gp";
        //this.audioSavePathInDevice = "./" + fileName + ".3gp";
    }

    /**
     * Checks whether the application has permissions to record audio.
     * @return
     */
    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(app.getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(app.getApplicationContext(),
                RECORD_AUDIO);
        return ((result == PackageManager.PERMISSION_GRANTED) &&
                (result1 == PackageManager.PERMISSION_GRANTED));
    }

    /**
     * requestPermission - asks for user permission to use microphone and storage
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this.app, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, MICROPHONE_AUDIO_REQUEST);
    }

    /**
     * setupMediaRecorder - initialize basic settings of audio recorder
     */
    public void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(this.audioSavePathInDevice);
    }

    /**
     * initAudioRecord - records the audio reminder which will be played from time to time
     *                  (when it's time to take the medicines)
     */
    public void initAudioRecord() {
        if(checkPermission()) {
            setupMediaRecorder();
            Log.i("ALEX_", "FILE: " + audioSavePathInDevice);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                Log.i("ALEX_", "gravando...\n");
                PersonalToast.toastMessage(app, app.getString(R.string.drougs_recording));
            } catch (IllegalStateException e) {
                e.printStackTrace();
                PersonalToast.toastMessage(app, "Exception: IllegalStateException");
            } catch (IOException e) {
                e.printStackTrace();
                PersonalToast.toastMessage(app, "Exception: IOException");
            }
        } else {
            requestPermission();
        }
    }

    /**
     * stopAudioRecord - stops the audio record and play the recoreded audio reminder
     */
    public void stopAudioRecord() {
        mediaRecorder.stop();
        PersonalToast.toastMessage(app, app.getString(R.string.drougs_recorded));
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(audioSavePathInDevice);
            mediaPlayer.prepare();
            Log.i("ALEX_", "gravado...\n");
        } catch (IOException e) {
            e.printStackTrace();
            PersonalToast.toastMessage(app, "Exception: IOException");
        }

        mediaPlayer.start();
    }
}
