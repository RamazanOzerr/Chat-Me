package com.example.basicchatapp.BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.example.basicchatapp.Activities.MainActivity;

public class BroadcastReceiverNetwork extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        boolean isConnected;
        if(intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)){
            ConnectivityManager connectivityManager = (ConnectivityManager)
                    context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo != null){
                isConnected = networkInfo.isConnected();
            } else{ // disconnected
                isConnected = false;
            }

            // Notify MainActivity about network state change
            Intent networkIntent = new Intent(MainActivity.NETWORK_STATE_CHANGED);
            networkIntent.putExtra(MainActivity.EXTRA_NETWORK_STATE, isConnected);
            context.sendBroadcast(networkIntent);

        }
    }
}
