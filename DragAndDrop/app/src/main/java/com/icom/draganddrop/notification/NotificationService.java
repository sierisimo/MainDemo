package com.icom.draganddrop.notification;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.icom.draganddrop.R;

/**
 * Created by davidcordova on 12/04/16.
 */
public class NotificationService extends IntentService {
    private static final String TAG = "NotificationService";

    NotificationCompat.Builder builder;
    private NotificationManager mNotificationManager;
    private String mMessage;
    private int mMillis;

    public NotificationService() {
        super("com.icom.draganddrop.notification");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mMessage = intent.getStringExtra("MESSAGE");
        mMillis = intent.getIntExtra("TIMER", 1000);
        NotificationManager nm = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));

        String action = intent.getAction();

        switch (action) {
            case "ACTION":
                issueNotification(intent, mMessage);
                break;

            case "SNOOZE":
                nm.cancel(001);
                issueNotification(intent, "Snoozing");
                break;

            case "DISMISS":
                nm.cancel(001);
                break;
        }
    }

    private void issueNotification(Intent intent, String msg) {
        mNotificationManager = ((NotificationManager) getSystemService(NOTIFICATION_SERVICE));

        Intent dismissIntent = new Intent(this, NotificationService.class);
        dismissIntent.setAction("DISMISS");
        PendingIntent piDismiss = PendingIntent.getService(this, 0, dismissIntent, 0);

        Intent snoozeIntent = new Intent(this, NotificationService.class);
        snoozeIntent.setAction("SNOOZE");
        PendingIntent piSnooze = PendingIntent.getService(this, 0, snoozeIntent, 0);

        builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_notification)
                .setContentTitle("Notification")
                .setContentText(msg)
                .setDefaults(Notification.DEFAULT_ALL)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .addAction(R.drawable.ic_stat_dismiss, "Dismiss", piDismiss)
                .addAction(R.drawable.ic_stat_snooze, "Snooze", piSnooze)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        Intent resultIntent = new Intent(this, ResultActivity.class);
        resultIntent.putExtra("MESSAGE", msg);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent resultPendingIntent = PendingIntent.getActivity(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder.setContentInfo("What the duck is this shit ????");
        builder.setContentIntent(resultPendingIntent);

        try {
            Thread.sleep(mMillis);
        } catch (InterruptedException e) {
            Log.e(TAG, "issueNotification: ", e);
        }

        mNotificationManager.notify(001, builder.build());
    }
}
