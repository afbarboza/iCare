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

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by alex on 04/12/17.
 */

public final class DrugAlarm {

    private static DrugAlarm            singletonAlarmManager = null;
    private static  AlarmManager        alarmMgr;
    private static AppCompatActivity    activity;
    private static PendingIntent        alarmIntent;

    private static final int millisecondsPerHour = 3600000;

    private DrugAlarm(AppCompatActivity activity) {
        this.activity = activity;
        alarmMgr = (AlarmManager) this.activity.getApplicationContext().getSystemService(Context.ALARM_SERVICE);

    }

    public static DrugAlarm getInstance(AppCompatActivity activity) {
        if (singletonAlarmManager == null)
            singletonAlarmManager = new DrugAlarm();
        return singletonAlarmManager;
    }

    /**
     * Sets an new alarm.
     * @param frequency
     */
    public static void addAlarm(int frequency) {
        if (singletonAlarmManager == null)

        Intent intent = new Intent(context, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);


        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + AlarmManager.INTERVAL_HALF_HOUR,
                AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
    }
}
