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

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v4.app.NotificationCompat;
import android.os.PowerManager;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

/**
 * Created by alex on 04/12/17.
 */

public class AlarmReceiver  extends BroadcastReceiver {
    String audioSavedPathInDevice;

    private void playAlarm(Context appContext) {
        MediaPlayer mp = MediaPlayer.create(appContext, R.raw.ganhou1);
        for (int i = 0; i < 5; i++) {
            mp.start();
        }
    }

    private void raiseDrugNotification(Context appContext) {
        /* creates an intent which does nothing */
        final Intent emptyIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, 1, emptyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        /* build Android user notification */
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(appContext)
                        .setSmallIcon(R.drawable.logocuidador)
                        .setContentTitle("iCare")
                        .setContentText(appContext.getString(R.string.drougs_notification))
                        .setContentIntent(pendingIntent);


        /* notify user */
        NotificationManager notificationManager = (NotificationManager) appContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, mBuilder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /* power up the device */
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YOUR TAG");
        wl.acquire();

        /* shows the notifcation and toast */
        PersonalToast.toastMessage(context, "Dançando no ritmo do carreta furacão");
        raiseDrugNotification(context);
        playAlarm(context);

        /* release the screen power manager */
        wl.release();
    }
}