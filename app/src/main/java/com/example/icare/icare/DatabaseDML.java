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

/**
 * Created by alex on 03/12/17.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

public class DatabaseDML {
    private SQLiteDatabase db;
    private DatabaseDDL dbDDL;

    public static final boolean TRANSATION_SUCCESS = true;
    public static final boolean TRANSATION_FAILURE = false;

    public DatabaseDML(Context context) {
        this.dbDDL = new DatabaseDDL(context);
    }

    private boolean doSanityCheck(String name, String dosage, String frequency) {
        return (name == null || dosage == null || frequency == null);
    }


    /**
     * Insert a new drug at local database
     * @param name: name of the new medicine
     * @param dosage: amount of medicine the old person should take
     * @param frequency: the frequency (in hours) the old person should take the medicine
     * @param observations: any kind of medical observations
     *
     * @return: false, if something went wrong.
     *          true, otherwise
     */
    public boolean insertNewDrug(String name, String dosage, String frequency, String observations) {
        ContentValues newTuple;
        long result;

        /* prohibits the insertion of null values in local database */
        if (doSanityCheck(name, dosage, frequency) == true) {
            return TRANSATION_FAILURE;
        }

        /* init database reference and the object storing the new tuple */
        db = dbDDL.getWritableDatabase();
        newTuple = new ContentValues();

        /* insert the new values */
        newTuple.put(DatabaseDDL.tableDrugs_DrugName, name);
        newTuple.put(DatabaseDDL.tableDrugs_DrugDosage, dosage);
        newTuple.put(DatabaseDDL.tableDrugs_DrugFrequency, frequency);

        if (observations == null)
            newTuple.put(DatabaseDDL.tableDrugs_DrugObservations, "null");
        else
            newTuple.put(DatabaseDDL.tableDrugs_DrugObservations, observations);

        /* execute the INSERT sql statement */
        result = db.insert(DatabaseDDL.tableDrugs, null, newTuple);
        if (result == -1)
            return TRANSATION_FAILURE;
        else
            return TRANSATION_SUCCESS;
    }

    /**
     * insertNewOld - inserts a new old person in local database
     *
     * @param name: the name of the old person.
     * @param birthDate: the birth date of the old person.
     * @param phone: the cell-phone of the old person
     *
     * @return: false, if something went wrong.
     *          true, otherwise
     */
    public boolean insertNewOld(String name, String birthDate, String phone) {
        ContentValues newTuple;
        long result;

        if (doSanityCheck(name, birthDate, phone) == true) {
            return TRANSATION_FAILURE;
        }

        /* init database reference and the object storing the new tuple */
        db = dbDDL.getWritableDatabase();
        newTuple = new ContentValues();

        newTuple.put(DatabaseDDL.tableOldPerson_OldName, name);
        newTuple.put(DatabaseDDL.tableOldPerson_Birth, birthDate);
        newTuple.put(DatabaseDDL.tableOldPerson_Phone, phone);

        /*execute the INSERT sql statement */
        result = db.insert(DatabaseDDL.tableOldPerson, null, newTuple);
        if (result == -1)
            return TRANSATION_FAILURE;
        else
            return TRANSATION_SUCCESS;
    }
}
