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

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.util.Random;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


/**
 * Created by alex on 03/12/17.
 */

public class InsertDrug extends AppCompatActivity {

    /* code for take picture request */
    static final int CAMERA_PIC_REQUEST = 1;
    /* stores the bitmap of drug photo */
    ImageView imgDrugPhoto;
    /* Button to record audio reminder*/
    Button btnRecordAudioReminder;
    boolean recording = false;

    /* variables to store audio reminder of drug */
    String AudioSavePathInDevice = null;
    MediaRecorder mediaRecorder ;
    Random random ;
    String RandomAudioFileName = "ABCDEFGHIJKLMNOP";
    public static final int MICROPHONE_AUDIO_REQUEST = 1;
    MediaPlayer mediaPlayer ;

    /**
     * dispatchTakePictureIntent - takes a photo from user.
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
        }
    }

    /**
     * onActivityResult - gets a result from an activity. (callback function)
     * @param requestCode: The request code you passed to startActivityForResult().
     * @param resultCode: A result code specified by the second activity.
     * @param data: An Intent that carries the result data.
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CAMERA_PIC_REQUEST:
                if (resultCode == RESULT_OK) {
                    Bitmap thumbail = (Bitmap) data.getExtras().get("data");
                    imgDrugPhoto.setImageBitmap(thumbail);
                }
        }
    }

    /**
     * checkPermission - Checks whether the application has the needed permissions to
     *          record audio and stores locally
     *
     * @return true, if the application has the needed privilleges
     *          false, otherwise.
     */
    public boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplicationContext(),
                WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(),
                RECORD_AUDIO);
        return ((result == PackageManager.PERMISSION_GRANTED) &&
                (result1 == PackageManager.PERMISSION_GRANTED));
    }

    /**
     * requestPermission - asks for user permission to use microphone and storage
     */
    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new
                String[]{WRITE_EXTERNAL_STORAGE, RECORD_AUDIO}, MICROPHONE_AUDIO_REQUEST);
    }

    /**
     * getAudioFileName - creates an string name for audio
     */
    private String getAudioFileName() {
        /*@todo fix me*/
        return "const";
    }

    /**
     * setupMediaRecorder - initialize basic settings of audio recorder
     */
    public void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(AudioSavePathInDevice);
    }

    /**
     * initAudioRecord - records the audio reminder which will be played from time to time
     *                  (when it's time to take the medicines)
     */
    public void initAudioRecord() {
        if(checkPermission()) {

            AudioSavePathInDevice =
                    Environment.getExternalStorageDirectory().getAbsolutePath() + "/" +
                            getAudioFileName() + "AudioRecording.3gp";

            setupMediaRecorder();
            Log.i("LOG", "FILE: " + AudioSavePathInDevice);

            try {
                mediaRecorder.prepare();
                mediaRecorder.start();
                PersonalToast.toastMessage(this, getString(R.string.drugs_recording));
            } catch (IllegalStateException e) {
                e.printStackTrace();
                PersonalToast.toastMessage(this, "Exception: IllegalStateException");
            } catch (IOException e) {
                e.printStackTrace();
                PersonalToast.toastMessage(this, "Exception: IOException");
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
            PersonalToast.toastMessage(this, getString(R.string.drugs_stop_record));
            mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(AudioSavePathInDevice);
            mediaPlayer.prepare();
        } catch (IOException e) {
            e.printStackTrace();
            PersonalToast.toastMessage(this, "Exception: IOException");
        }
    }

    /**
     * handleAudioRecord - stores (and sends) an audio reminder to the old person use
     *                      to take his/her medicines
     */
    public void handleAudioRecord() {
        if (recording == false) {
            /* record audio */
            recording = true;
            btnRecordAudioReminder.setText(getString(R.string.drugs_stop_record));
            initAudioRecord();
        } else {
            /* stop record audio */
            recording = false;
            btnRecordAudioReminder.setText(getString(R.string.drugs_record_voice));
            stopAudioRecord();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_drugs);

        /* initialize graphical componentes here */
        imgDrugPhoto = (ImageView) findViewById(R.id.imgDrugPhoto);
        btnRecordAudioReminder = (Button) findViewById(R.id.save_drug_reminder);

        /*defines behavior of graphical components here */
        imgDrugPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });
        btnRecordAudioReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAudioRecord();
            }
        });

    }
}
