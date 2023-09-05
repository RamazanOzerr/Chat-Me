package com.example.basicchatapp.Activities.MainActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.basicchatapp.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

// class which we use to create bottom sheet
public class ModalBottomSheet extends BottomSheetDialogFragment {

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
        ) {
            return inflater.inflate(R.layout.bottom_sheet_layout, container, false);
        }

        public static final String TAG = "ModalBottomSheet";


}
