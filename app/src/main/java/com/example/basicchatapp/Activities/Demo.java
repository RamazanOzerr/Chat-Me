package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.basicchatapp.R;

import java.util.Calendar;
import java.util.Date;

public class Demo extends AppCompatActivity {

    TextView timehehe;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo2);

        timehehe = findViewById(R.id.timehehe);
        Date today = Calendar.getInstance().getTime();
        String time = today.toString();
        timehehe.setText(time);

        timehehe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "have no clue what it is")
                        .setSmallIcon(R.drawable.iconnotif)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);

                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(1, builder.build()); // notificationID ye 1 verdik

                mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.notif);
                mediaPlayer.start();


            }
        });


    }
}