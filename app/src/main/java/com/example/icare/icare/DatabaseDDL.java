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

package com.example.icare.icare;/**
 * Created by alex on 03/12/17.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseDDL extends SQLiteOpenHelper{
    public static final String databaseName = "ICARE.db";

    /* table drugs columns */
    public static final String tableDrugs                   = "drugs";
    public static final String tableDrugs_DrugId            = "drug_id";
    public static final String tableDrugs_DrugName          = "drug_name";
    public static final String tableDrugs_DrugDosage        = "drug_dosage";
    public static final String tableDrugs_DrugFrequency     = "drug_frequency";
    public static final String tableDrugs_DrugObservations  = "drug_observations";

    /* table old_person columns */
    public static final String tableOldPerson           = "old_person";
    public static final String tableOldPerson_OldName   = "old_name";
    public static final String tableOldPerson_Birth     = "old_birth";
    public static final String tableOldPerson_Phone     = "old_birth";

    public DatabaseDDL(Context context) {
        super(context, databaseName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        /* creates the drug table */
        String sql = "CREATE TABLE " + tableDrugs + " ( "
                + tableDrugs_DrugId             + " integer primary key autoincrement, "
                + tableDrugs_DrugDosage         + " text, "
                + tableDrugs_DrugFrequency      + " integer, "
                + tableDrugs_DrugObservations   + " text null "
                + ")";
        sqLiteDatabase.execSQL(sql);

        /* creates the old_person table */
        sql = "CREATE TABLE " + tableOldPerson + " ( "
                + tableOldPerson_OldName    + " text, "
                + tableOldPerson_Birth      + " text, "
                + tableOldPerson_Phone      + " text primary key "
                + ")";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableDrugs);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + tableOldPerson);
        /*@todo update other tables */
        onCreate(sqLiteDatabase);
    }
}