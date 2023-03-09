package com.example.basicchatapp.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.basicchatapp.Activities.MainActivity;
import com.example.basicchatapp.Notifications.OreoNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        System.out.println("message received çalıştı");
        String sented = remoteMessage.getData().get("sented");
        System.out.println(remoteMessage.getData());
        System.out.println(sented);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(firebaseUser != null && sented.equals(firebaseUser.getUid())){
//            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                sendOreoNotification(remoteMessage);
//            }else{
//                sendNotification(remoteMessage);
//            }
//        }
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendOreoNotification(remoteMessage);
        }else{
            sendNotification(remoteMessage);
        }
    }
    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

//        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body, pendingIntent, defaultSound, icon);

        int i = 0;
        if(j > 0){
            i = j;
        }
        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("body");

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(Integer.parseInt(icon)).setContentTitle(title)
                .setContentText(body).setAutoCancel(true)
                .setSound(defaultSound).setContentIntent(pendingIntent);
        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // bize unique channel id ler lazım olduğu için böyle bir yol izledik, her yeni bildirimde
        // sıralı şekilde yeni bir channel id oluşturulmuş olacak
        int i = 0;
        if(j > 0){
            i = j;
        }
        noti.notify(i, builder.build());

    }

    @Override
    public void onNewToken(@NonNull String token) {
        updateToken(token);
        super.onNewToken(token);
    }

    // token ı app in yüklendiği cihazın id si gibi düşünebiliriz, app reinstall yapıldığında ya da
    // önbellek temizleme işlemi yapıldığında ya da farklı cihaz a geçildiğinde token değişir,
    // bizim de bu token değerini save etmemiz gerektiği için bunu Users ın altında yapıyorum
    private void updateToken(String token){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(FirebaseAuth.getInstance()
                        .getCurrentUser().getUid());
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        databaseReference.updateChildren(map);
    }

    private void sendNotification(String messageBody) {
        Intent intent = new Intent(this, FirebaseNotificationService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_IMMUTABLE);

        String channelId = "1";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setContentTitle("FCM Message")
                        .setContentText(messageBody)
                        .setAutoCancel(true)
                        .setSound(defaultSoundUri)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
