package com.mind.INFINITO;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
   String TAG = "INFINITO NotiServices";
   String msgTitle, msgBody;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
           // Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            msgTitle = remoteMessage.getNotification().getTitle();
            msgBody = remoteMessage.getNotification().getBody();
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "From: " + remoteMessage.getNotification().getBody());
            sendNotification( msgTitle, msgBody);
            handleNow();
        }
    }


    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        sendRegistrationToServer(token);
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void  handleNow(){
        Log.d(TAG, "Short Lived Task is Done");
    }

    private void sendNotification(String msgTitle, String msgBody){
        String channelId = getString(R.string.default_notification_channel_ID);
        Intent intent = new Intent(this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri sounduri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,channelId).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(msgTitle)
                .setContentText(msgBody)
                .setAutoCancel(true)
                .setSound(sounduri)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(channelId,"Sport Fest", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
         notificationManager.notify(0,notificationBuilder.build());

    }

}
