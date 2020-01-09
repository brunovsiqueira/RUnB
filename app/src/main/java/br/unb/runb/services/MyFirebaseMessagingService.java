package br.unb.runb.services;


import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.Serializable;

import androidx.core.app.NotificationCompat;
import br.unb.runb.R;
import br.unb.runb.screens.ContainerActivity;
import br.unb.runb.screens.menu.NotificationActivity;
import br.unb.runb.util.UiFunctions;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static int NOTIFY_TAG = 123;
    private static final String CHANNEL_ID = "1";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(this, ContainerActivity.class);
        intent.putExtra("notification_data", (Serializable) remoteMessage.getData());
        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

        createNotificationChannel();


        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //**edit this line to put requestID as requestCode**
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        NotificationManager mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo_original)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000})
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        builder.setContentIntent(contentIntent);
        Notification notification = builder.build();

        mNotificationManager.notify(0, notification);



    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Informes";
            String description = "Informes do Restaurante Universitário";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
