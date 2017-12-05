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
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CaregiverDashboard extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private static boolean calledAlready = false;
    /**
     * Handles the Add Old Person button click event, redirecting to the Insert Old Person view.
     *
     * @param v: basic building block for user interface components
     */
    public void handleAddOldPerson(View v) {
        Intent i = new Intent(this, InsertDrug.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_dashboard);

        FirebaseApp.initializeApp(CaregiverDashboard.this);
        Log.i("alex", "Firebase initialized.");

        if (!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        Log.i("alex", "Local persistence enabled");


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Intent it = getIntent();

        final Droug newDroug = (Droug) it.getSerializableExtra("newDroug");
        if(newDroug != null) {
            Random random = new Random();
            Integer intRand = random.nextInt(9999);
            Log.i("debugGiovanni", newDroug.getName());

            ref.child("drougs").child(intRand.toString()).setValue(newDroug);
        }

        ref.child("drougs").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Droug> listOfDrougs = new ArrayList<Droug>();
                listOfDrougs.clear();
                for(DataSnapshot objectSnapshot: dataSnapshot.getChildren()){
                    Droug drougReceived = objectSnapshot.getValue(Droug.class);
                    listOfDrougs.add(drougReceived);
                    Log.e("testeGiovanni", drougReceived.getName());
                }

                createListView(listOfDrougs);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void createListView(List<Droug> drougList){
        ListView lv_OldPerson = (ListView) findViewById(R.id.lv_olds);
        final ListDrougsAdapter listDrougsAdapter = new ListDrougsAdapter(CaregiverDashboard.this, drougList);
        lv_OldPerson.setAdapter(listDrougsAdapter);
    }
}
