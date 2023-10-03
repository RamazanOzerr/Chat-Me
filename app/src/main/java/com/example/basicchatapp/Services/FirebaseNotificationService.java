package com.example.basicchatapp.Services;


import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import com.example.basicchatapp.Activities.WelcomeActivity;
import com.example.basicchatapp.Notifications.OreoNotification;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.Constants;
import com.example.basicchatapp.Utils.HelperMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import java.util.HashMap;
import java.util.Map;

public class FirebaseNotificationService extends FirebaseMessagingService {

    private final String TAG = "FIREBASE NOTIFICATION SERVICE";
    private final Integer PENDING_INTENT_REQUEST_CODE = 1;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG, "onMessageReceived: remote message user id " + remoteMessage.getData().get("userId"));
        Log.d(TAG, "onMessageReceived: remote message data values: " + remoteMessage.getData().values());
        Log.d(TAG, "onMessageReceived: remote message data keys: " + remoteMessage.getData().keySet());
        Log.d(TAG, "onMessageReceived: notification: " + remoteMessage.getNotification());

//        sendOreoNotification(remoteMessage);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            sendOreoNotification(remoteMessage);
        }else{
            sendNotification(remoteMessage);
        }
    }

    // Android 7 and below
    private void sendNotification(RemoteMessage remoteMessage){
        String user;
        try{
            user = remoteMessage.getData().get("userId");
        } catch (Exception e){
            user = null;
        }

        Log.d(TAG, "onMessageReceived: remote message user id: " + remoteMessage
                .getData().get("userId"));

        Log.d(TAG, "onMessageReceived: remote message " + remoteMessage.getData());
        Log.d(TAG, "onMessageReceived: notification: " + remoteMessage.getNotification());

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        int j = 1;
        Intent intent = new Intent(this, WelcomeActivity.class);
//        Bundle bundle = new Bundle();
//
//        bundle.putString(Constants.TARGET_USER_ID, user);
//        intent.putExtras(bundle);
        intent.putExtra(Constants.TARGET_USER_ID, user);
        Log.d(TAG, "sendNotification: intent" + intent.getExtras());
        Log.d(TAG, "sendNotification: intent" + intent.getExtras().keySet());
//        intent.putExtra(Constants.TARGET_USER_ID, user);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, PENDING_INTENT_REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "1")
                .setSmallIcon(R.mipmap.my_ic_launcher2_round)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(defaultSound)
                .setAutoCancel(true);


        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(1, builder.build());

    }


    private void sendOreoNotification(RemoteMessage remoteMessage){
        String user;
        try{
            user = remoteMessage.getData().get("userId");
        } catch (Exception e){
            user = null;
        }

        Log.d(TAG, "onMessageReceived: remote message user id: " + remoteMessage
                .getData().get("userId"));

        Log.d(TAG, "onMessageReceived: remote message " + remoteMessage.getData());
        Log.d(TAG, "onMessageReceived: user: " + user);

        String title = remoteMessage.getNotification().getTitle();
        String body = remoteMessage.getNotification().getBody();

        int j = 2;
        Intent intent = new Intent(this, WelcomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Constants.TARGET_USER_ID, user);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Log.d(TAG, "sendNotification: intent" + intent.getExtras());
        Log.d(TAG, "sendNotification: intent" + intent.getExtras().keySet());
        PendingIntent pendingIntent = PendingIntent.getActivity(this
                , PENDING_INTENT_REQUEST_CODE,
                intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        OreoNotification oreoNotification = new OreoNotification(this);
        Notification.Builder builder = oreoNotification.getOreoNotification(title, body,
                pendingIntent, defaultSound);

        oreoNotification.getManager().notify(j+1, builder.build());
    }

//    private void sendNotification(RemoteMessage remoteMessage){
//        String user = remoteMessage.getData().get("user");
////        String title = "new message";
//
//        String title = remoteMessage.getNotification().getTitle();
//        String body = remoteMessage.getNotification().getBody();
//
////        String body = remoteMessage.getNotification().getBody();
//
//        RemoteMessage.Notification notification = remoteMessage.getNotification();
//        int j = Integer.parseInt(user.replaceAll("[\\D]",""));
//        Intent intent = new Intent(this, MainActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("userId",user);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        PendingIntent pendingIntent = PendingIntent.getActivity(this,j,intent,PendingIntent.FLAG_ONE_SHOT);
//
//        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
//                .setSmallIcon(R.mipmap.ic_notification).setContentTitle(title)
//                .setContentText(body).setAutoCancel(true)
//                .setSound(defaultSound).setContentIntent(pendingIntent);
//        NotificationManager noti = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//
//        // bize unique channel id ler lazım olduğu için böyle bir yol izledik, her yeni bildirimde
//        // sıralı şekilde yeni bir channel id oluşturulmuş olacak
//        int i = 0;
//        if(j > 0){
//            i = j;
//        }
//        noti.notify(i, builder.build());
//    }

    @Override
    public void onNewToken(@NonNull String token) {
        updateToken(token);
        super.onNewToken(token);
    }

    // token ı app in yüklendiği cihazın id si gibi düşünebiliriz, app reinstall yapıldığında ya da
    // önbellek temizleme işlemi yapıldığında ya da farklı cihaz a geçildiğinde token değişir,
    // bizim de bu token değerini save etmemiz gerektiği için bunu Users ın altında yapıyorum
    private void updateToken(String token){
        String currUserId;
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            currUserId = FirebaseAuth.getInstance()
                    .getCurrentUser().getUid();
        } else {
            HelperMethods.showShortToast(this, "log in again");
            currUserId = "null";
        }
        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Users").child(currUserId);
        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        databaseReference.updateChildren(map);

    }

}
