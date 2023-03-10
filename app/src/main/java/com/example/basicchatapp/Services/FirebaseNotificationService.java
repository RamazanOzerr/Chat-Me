package com.example.basicchatapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import com.example.basicchatapp.Activities.MainActivity;
import com.example.basicchatapp.Notifications.OreoNotification;
import com.example.basicchatapp.R;
import com.google.firebase.auth.FirebaseAuth;
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
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendOreoNotification(remoteMessage);
        }else{
            sendNotification(remoteMessage);
        }
    }
    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String icon = remoteMessage.getData().get("icon");
        String title = "new message";
        String body = remoteMessage.getNotification().getBody();

        int j = 0;
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,
                intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body,
                pendingIntent, defaultSound, icon);

        int i = 0;
        if(j > 0){
            i = j;
        }
        oreoNotification.getManager().notify(i, builder.build());
    }

    private void sendNotification(RemoteMessage remoteMessage){
        String user = remoteMessage.getData().get("user");
        String title = "new message";

        String body = remoteMessage.getNotification().getBody();

        RemoteMessage.Notification notification = remoteMessage.getNotification();
        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
        Intent intent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("userId",user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_notification).setContentTitle(title)
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

}
