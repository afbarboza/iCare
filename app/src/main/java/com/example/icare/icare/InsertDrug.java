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
import android.widget.ImageView;

/**
 * Created by alex on 03/12/17.
 */

public class InsertDrug extends AppCompatActivity {

    /* code for take picture request */
    static final int CAMERA_PIC_REQUEST = 1;
    /* stores the bitmap of drug photo */
    ImageView imgDrugPhoto;

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

        /* initialize graphical componentes here */
        imgDrugPhoto = (ImageView) findViewById(R.id.imgDrugPhoto);


        /*defines behavior of graphical components here */
        imgDrugPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

    }
}
