package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterProfileDesign extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    CircleImageView profile_image1;
    EditText name1, user_name1, about_me1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_profile_design);

//        defineItems();
//
//
//        if(firebaseAuth.getUid() != null){
//            databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
//            Map map = new HashMap();
//            map.put("name",name1.getText().toString());
//            map.put("username",user_name1.getText().toString());
//            map.put("photo","null");
//            map.put("bio",about_me1.getText().toString());
//
//            databaseReference.setValue(map);
//
//            Toast.makeText(getApplicationContext(), "Account has been created successfully",Toast.LENGTH_SHORT).show();
//        }
    }

//    public void defineItems(){
//        profile_image1 = findViewById(R.id.profile_image1);
//        name1 = findViewById(R.id.name1);
//        user_name1 = findViewById(R.id.user_name1);
//        about_me1 = findViewById(R.id.about_me1);
//        firebaseAuth = FirebaseAuth.getInstance();
//        firebaseDatabase = FirebaseDatabase.getInstance();
//    }
//

}