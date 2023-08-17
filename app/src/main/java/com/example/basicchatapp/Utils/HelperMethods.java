package com.example.basicchatapp.Utils;

import android.content.Context;
import android.widget.Toast;

public class HelperMethods {

    public void showShortToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public void showLongToast(Context context, String text){
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
