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
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class CaregiverDashboardDrougs extends AppCompatActivity {
    private List<Droug> listDrougs = new ArrayList<Droug>();
    //private ListView lv_drougs = (ListView) findViewById(R.id.lv_drougs);

    public void handleAddDroug(View v) {
        Intent i = new Intent(this, CaregiverDashboard.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_dashboard_drougs);

        Intent intent = getIntent();
        final OldPerson oldPerson = (OldPerson) intent.getSerializableExtra("selectedOld");

        if(intent != null){
            listDrougs.clear();
            listDrougs = oldPerson.getDrougs();
            Log.e("giovannidebug",oldPerson.getName());
        }


    }
}
