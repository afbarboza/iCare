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
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by alex on 03/12/17.
 */

public class InsertDrug extends AppCompatActivity {

    /* code for take picture request */
    static final int CAMERA_PIC_REQUEST = 1;
    /* stores the bitmap of drug photo */
    ImageView imgDrugPhoto;
    EditText txtDrougName;
    EditText txtDrougDosage ;
    EditText txtDrougPeriod;
    EditText txtDrougObservations;
    Button saveDrougRegister;
    private boolean recording = false;
    AudioAlarm am = null;
    Button btnRecordAudioReminder;

    public void handleAudioRecord() {
        if (recording == false) {
            am = new AudioAlarm(this, txtDrougName.getText().toString());

            /* record audio */
            recording = true;
            btnRecordAudioReminder.setText(getString(R.string.drougs_recording));
            am.initAudioRecord();
        } else {
            /* stop record audio */
            recording = false;
            btnRecordAudioReminder.setText(getString(R.string.drougs_recorded));
            am.stopAudioRecord();
        }
    }

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

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_drugs);

        txtDrougName = (EditText) findViewById(R.id.txtNameInsertDroug);
        txtDrougDosage = (EditText) findViewById(R.id.txtDosageInsertDroug);
        txtDrougObservations = (EditText) findViewById(R.id.txtObservationsInsertDroug);
        txtDrougPeriod = (EditText) findViewById(R.id.txtPeriodInsertDroug);
        saveDrougRegister = (Button) findViewById(R.id.save_droug_register);

        /* initialize graphical componentes here */
        imgDrugPhoto = (ImageView) findViewById(R.id.imgDrugPhoto);
        btnRecordAudioReminder = (Button) findViewById(R.id.btnRecordAudioReminder);


        saveDrougRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String drougName = txtDrougName.getText().toString();
                String drougDosage = txtDrougDosage.getText().toString();
                String drougPeriod = txtDrougPeriod.getText().toString();
                String drougObservations = txtDrougObservations.getText().toString();

                DrugAlarm d = new DrugAlarm();
                d.addAlarm(InsertDrug.this, Integer.parseInt(drougPeriod), Integer.parseInt(drougPeriod), txtDrougName.getText().toString());

                Droug droug = new Droug();
                droug.setName(drougName);
                droug.setDosage(drougDosage);
                droug.setPeriod(drougPeriod);
                droug.setObservations(drougObservations);

                Intent it = new Intent(InsertDrug.this, CaregiverDashboard.class);
                it.putExtra("newDroug", (Serializable) droug);
                startActivity(it);
            }
        });

        btnRecordAudioReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleAudioRecord();
            }
        });


        /*defines behavior of graphical components here */
        imgDrugPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }
}
