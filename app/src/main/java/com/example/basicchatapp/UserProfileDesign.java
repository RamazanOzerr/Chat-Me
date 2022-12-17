package com.example.basicchatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UserProfileDesign extends AppCompatActivity {

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;
    ImageView profile_image;
    EditText user_name, name, about_me;
    Button setBtn;
    int photoID;

    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_design);

        profile_image = findViewById(R.id.profile_image);
        user_name = findViewById(R.id.user_name);
        name = findViewById(R.id.name);
        about_me = findViewById(R.id.about_me);
        firebaseAuth = FirebaseAuth.getInstance();
        setBtn = findViewById(R.id.setBtn);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();


        name.setEnabled(true);
        user_name.setEnabled(true);
        about_me.setEnabled(true);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        user_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        about_me.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        // it opens gallery to pick a pic
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });



        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                photoID = profile_image.getId();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
                Map map = new HashMap();
                map.put("name",name.getText().toString());
                map.put("username",user_name.getText().toString());
//                map.put("photo","null");
                map.put("bio",about_me.getText().toString());

                databaseReference.setValue(map);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                Profile user1 = snapshot.getValue(Profile.class);
                String bio = snapshot.child(firebaseAuth.getUid()).child("bio").getValue().toString();
                String namee = snapshot.child(firebaseAuth.getUid()).child("name").getValue().toString();
                String photoPath = snapshot.child(firebaseAuth.getUid()).child("photo").getValue().toString();
                String username = snapshot.child(firebaseAuth.getUid()).child("username").getValue().toString();


                user_name.setText(username);
                name.setText(namee);
//                profile_image.setImageResource(photoID);
                about_me.setText(bio);

                StorageReference httpsReference = firebaseStorage.getReferenceFromUrl("gs://chatapp-c5610.appspot.com/53591942324.jpg");
                if(!photoPath.equals("null")){
                    Toast.makeText(getApplicationContext(),"if e girdi",Toast.LENGTH_LONG).show();
                    Picasso.get().load(user1.getImage()).into(profile_image);

//                    Picasso.get().load(String.valueOf(httpsReference)).into(profile_image);
//                    Glide.with(getApplicationContext()).load(httpsReference).into(profile_image);
                }

                //TODO storage den resmi çekip yükleme işlemini yap

//                StorageReference pathReference = storageReference.child("images/stars.jpg");
//
//                StorageReference gsReference = firebaseStorage.getReferenceFromUrl("gs://bucket/images/stars.jpg");



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
    if(resultCode == RESULT_OK && data != null){
        Uri selectedImage = data.getData();


        // this is where given the file name, if you don't set unique names for each file, it'll
        // overwrite on the prev one
        StorageReference reference = storageReference.child("Pictures").child(RandomString.getSaltString() + ".jpg");
        reference.putFile(selectedImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),
                            "picture has been successfully updated",Toast.LENGTH_LONG).show();

                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
                    Map map = new HashMap();
                    map.put("name",name.getText().toString());
                    map.put("username",user_name.getText().toString());
                    map.put("photo",task.getResult().getUploadSessionUri().toString());
//                    map.put("photo",selectedImage);
                    map.put("bio",about_me.getText().toString());

                    databaseReference.setValue(map);


                }else{
                    Toast.makeText(getApplicationContext(),
                            "picture has not been updated",Toast.LENGTH_LONG).show();
                }
            }
        });

        profile_image.setImageURI(selectedImage);
//        Picasso.get().load(selectedImage).into(profile_image);
        }
    }

}