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
import android.media.MediaPlayer;
import android.os.SystemClock;

/**
 * Created by alex on 04/12/17.
 *
 */

public class DrugAlarm {
    private static int i = 0;

    public void addAlarm(Context appContext, int initHour, int frequency) {
        AlarmManager manager = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent = new Intent(appContext, AlarmReceiver.class);
        i++;
        myIntent.putExtra("msg", "" + i);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(appContext, i, myIntent, 0);
        manager.setRepeating(AlarmManager.RTC_WAKEUP,
                SystemClock.elapsedRealtime() + (initHour * 1000),
                frequency * 1000, pendingIntent);


    }
}