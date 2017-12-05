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
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.os.PowerManager;

import java.io.IOException;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;

/**
 * Created by alex on 04/12/17.
 */

public class AlarmReceiver  extends BroadcastReceiver {

    /**
     * Displays personalized song when it's time to take the medicine
     * @param appContext: the current app context running
     */
    private void playAlarm(Context appContext, Intent it) {
        /* plays default songs */
        MediaPlayer mp = MediaPlayer.create(appContext, R.raw.ganhou1);
        mp.start();

        /* gets audio file name */
        String audio = it.getStringExtra("audio_file");
        String audioSavePathInDevice = Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/" + audio + ".3gp";

        /* set up media player */
        mp = new MediaPlayer();
        try {
            mp.setDataSource(audioSavePathInDevice);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            mp = MediaPlayer.create(appContext, R.raw.ganhou1);
            mp.start();
        }
    }

    /**
     * raiseDrugNotification - adds a notification that its time to take the medicine to
     *                          upper android sidebar.
     * @param appContext: the current app context running (not null)
     */
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

    /**
     * Warns user (visually and sonorously) that it's time to take the medicines.
     *
     * @param context
     * @param intent
     *
     * @see Intent for more information about @intent
     * @see Context for more information about @context
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        /* power up the device */
        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock((PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP), "YOUR TAG");
        wl.acquire();

        /* shows the notifcation and toast */
        PersonalToast.toastMessage(context, context.getString(R.string.drougs_notification));
        raiseDrugNotification(context);
        playAlarm(context, intent);

        /* release the screen power manager */
        wl.release();

        /* vibrates the device for 4 seconds */
        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(4000);

    }
}