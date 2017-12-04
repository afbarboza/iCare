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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CaregiverDashboard extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private static boolean calledAlready = false;
    private FloatingActionButton btn_add_person;
    /**
     * Handles the Add Old Person button click event, redirecting to the Insert Old Person view.
     *
     * @param v: basic building block for user interface components
     */
    public void handleAddOldPerson(View v) {
        Intent i = new Intent(this, InsertOldPerson.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_dashboard);
        btn_add_person = findViewById(R.id.btn_add_old_person);

        FirebaseApp.initializeApp(CaregiverDashboard.this);
        if (!calledAlready){
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            calledAlready = true;
        }
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        ref = database.getReference();

        Intent it = getIntent();
        final OldPerson oldPerson = (OldPerson) it.getSerializableExtra("oldPerson");
        if(oldPerson != null) {
            ref.child("olds").child(oldPerson.getPhone()).setValue(oldPerson);
        }

        ref.child("olds").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<OldPerson> listOfOlds = new ArrayList<OldPerson>();
                listOfOlds.clear();
                for(DataSnapshot objectSnapshot: dataSnapshot.getChildren()){
                    OldPerson oldPersonReceived = objectSnapshot.getValue(OldPerson.class);
                    listOfOlds.add(oldPersonReceived);
                    Log.e("testeGiovanni", oldPersonReceived.getName());
                }

                createListView(listOfOlds);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void createListView(List<OldPerson> oldPersonList){
        ListView lv_OldPerson = (ListView) findViewById(R.id.lv_olds);
        final ListOldsAdapter listOldsAdapter = new ListOldsAdapter(CaregiverDashboard.this, oldPersonList);
        lv_OldPerson.setAdapter(listOldsAdapter);

        lv_OldPerson.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("testeGiovanniclick", listOldsAdapter.getItem(i).toString());
                Intent intent = new Intent(CaregiverDashboard.this, CaregiverDashboardDrougs.class);
                intent.putExtra("selectedOld", (Serializable) listOldsAdapter.getItem(i));
                startActivity(intent);
            }
        });

    }
}
