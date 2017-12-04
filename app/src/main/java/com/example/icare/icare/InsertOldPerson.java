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

import android.app.DialogFragment;
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

public class InsertOldPerson extends AppCompatActivity {

    EditText txtOldName;
    static EditText txtOldBirth;
    ImageView imgOldPhoto;
    EditText txtOldPhone;
    Button saveOldRegister;


    static final int CAMERA_PIC_REQUEST = 1;

    /**
     * handleTxtBirthClick - handles the click on the birth date edit text.
     * Pop up a date picker, so user can select the OldPerson birth date.
     *
     */
    public void handleTxtBirthClick() {
        /* popup a date picker */
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");


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
                    imgOldPhoto.setImageBitmap(thumbail);
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_old_person);


        /* initialize graphic components here */
        txtOldName = (EditText) findViewById(R.id.txtOldName);
        txtOldBirth = (EditText) findViewById(R.id.txtOldBirth);
        txtOldPhone = (EditText) findViewById(R.id.txtOldPhoneNumber);
        imgOldPhoto = (ImageView) findViewById(R.id.imgOldPhoto);
        saveOldRegister = (Button) findViewById(R.id.save_old_register);


        txtOldBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleTxtBirthClick();
            }
        });

        txtOldBirth.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    handleTxtBirthClick();
                }
            }
        });


        imgOldPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        saveOldRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldName = txtOldName.getText().toString();
                String oldBirth = txtOldBirth.getText().toString();
                String oldPhone = txtOldPhone.getText().toString();

                OldPerson oldPerson = new OldPerson();
                oldPerson.setName(oldName);
                oldPerson.setDateBirth(oldBirth);
                oldPerson.setPhone(oldPhone);

                Intent it = new Intent(InsertOldPerson.this, CaregiverDashboard.class);
                it.putExtra("oldPerson", (Serializable) oldPerson);
                startActivity(it);
            }
        });

    }

    /**
     * notifyDateChanged - notifies GUI that the birth date has been updated.
     */
    public static void notifyDateChanged() {
        int day = BirthDate.getDay();
        int month = BirthDate.getMonth();
        int year = BirthDate.getYear();

        txtOldBirth.setText(day + "/" + month + "/" + year);
    }
}
