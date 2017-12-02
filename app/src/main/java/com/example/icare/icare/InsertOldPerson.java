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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class InsertOldPerson extends AppCompatActivity {

    EditText txtOldName;
    EditText txtOldBirth;


    /**
     * handleTxtBirthClick - handles the click on the birth date edit text.
     * Pop up a date picker, so user can select the OldPerson birth date.
     *
     * @param v basic graphic building block.
     */
    public void handleTxtBirthClick(View v) {
        /* basic sanity check */
        String tmp = txtOldBirth.getText().toString();
        if (tmp.compareTo(getString(R.string.acitivity_insert_old_str2)) == 0) {
            txtOldBirth.setText("");
        }

        /* popup a date picker */
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_old_person);

        txtOldName = (EditText) findViewById(R.id.txtOldName);
        txtOldBirth = (EditText) findViewById(R.id.txtOldBirth);
    }
}
