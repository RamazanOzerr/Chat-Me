package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.RandomString;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

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
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());

                Map map = new HashMap();
                map.put("name",name.getText().toString());
                map.put("username",user_name.getText().toString());
                map.put("bio",about_me.getText().toString());

                databaseReference.updateChildren(map);
            }
        });


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                Profile user1 = snapshot.getValue(Profile.class);
                String bio = snapshot.child(firebaseAuth.getUid()).child("bio").getValue().toString();
                String namee = snapshot.child(firebaseAuth.getUid()).child("name").getValue().toString();
                String photoPath = snapshot.child(firebaseAuth.getUid()).child("photo").getValue().toString();
                String username = snapshot.child(firebaseAuth.getUid()).child("username").getValue().toString();


                user_name.setText(username);
                name.setText(namee);
//                profile_image.setImageResource(photoID);
                about_me.setText(bio);

                if(!photoPath.equals("null")){
                    Picasso.get().load(photoPath).into(profile_image);
                }
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

        name = findViewById(R.id.name);
        user_name = findViewById(R.id.user_name);
        about_me = findViewById(R.id.about_me);
        final StorageReference ref = storageReference.child("Pictures").child(RandomString.getSaltString() + ".jpg");
        UploadTask uploadTask = ref.putFile(selectedImage);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    firebaseDatabase = FirebaseDatabase.getInstance();
                    databaseReference = firebaseDatabase.getReference().child("Users").child(firebaseAuth.getUid());
                    Map map = new HashMap();
                    map.put("name",name.getText().toString());
                    map.put("username",user_name.getText().toString());
                    map.put("photo",downloadUri.toString());
//                    map.put("photo",selectedImage);
                    map.put("bio",about_me.getText().toString());

                    databaseReference.setValue(map);

                    Toast.makeText(getApplicationContext(),
                            "picture has been successfully updated",Toast.LENGTH_LONG).show();
//
//
                } else {
                    // Handle failures
                    // ...
                }
            }
        });



        // onDataChange methodu yüzünden buranın çalışma süresi uzun olmuyor.
//        profile_image.setImageURI(selectedImage);
//        Picasso.get().load(reference.getDownloadUrl().toString()).into(profile_image);
        }
    }

}