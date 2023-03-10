package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.RandomString;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
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
    private ImageView profile_image;
    private EditText user_name, name, about_me;
    StorageReference storageReference;
    FirebaseStorage firebaseStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_design);

        Button logOutBtn = findViewById(R.id.logOutBtn);
        profile_image = findViewById(R.id.profile_image);
        user_name = findViewById(R.id.user_name);
        name = findViewById(R.id.name);
        about_me = findViewById(R.id.about_me);
        firebaseAuth = FirebaseAuth.getInstance();
        Button setBtn = findViewById(R.id.setBtn);
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        MaterialToolbar toolbar = findViewById(R.id.toolbar_user_profile_design);
        setSupportActionBar(toolbar);
        toolbar.setTitle("PROFILE");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name.setEnabled(true);
        user_name.setEnabled(true);
        about_me.setEnabled(true);

        logOutBtn.setOnClickListener(view -> {
            firebaseAuth.signOut();
            startActivity(new Intent(UserProfileDesign.this, LoginActivity.class));
        });

        // it opens gallery to pick a pic
        profile_image.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,3);
        });

        setBtn.setOnClickListener(view -> {
            firebaseDatabase = FirebaseDatabase.getInstance();
            databaseReference = firebaseDatabase.getReference().child("Users")
                    .child(firebaseAuth.getUid());

            Map<String, Object> map = new HashMap<>();
            map.put("name",name.getText().toString());
            map.put("username",user_name.getText().toString());
            map.put("bio",about_me.getText().toString());
            databaseReference.updateChildren(map);
        });

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

//                Profile user1 = snapshot.getValue(Profile.class);
                String bio = snapshot.child(firebaseAuth.getUid()).child("bio")
                        .getValue().toString();
                String namee = snapshot.child(firebaseAuth.getUid()).child("name")
                        .getValue().toString();
                String photoPath = snapshot.child(firebaseAuth.getUid()).child("photo")
                        .getValue().toString();
                String username = snapshot.child(firebaseAuth.getUid()).child("username")
                        .getValue().toString();

                user_name.setText(username);
                name.setText(namee);
                about_me.setText(bio);

                // upload photo
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
        final StorageReference ref = storageReference.child("Pictures")
                .child(RandomString.getSaltString() + ".jpg");
        UploadTask uploadTask = ref.putFile(selectedImage);

        Task<Uri> urlTask = uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw task.getException();
            }

            // Continue with the task to get the download URL
            return ref.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                firebaseDatabase = FirebaseDatabase.getInstance();
                databaseReference = firebaseDatabase.getReference().child("Users")
                        .child(firebaseAuth.getUid());
                Map<String, Object> map = new HashMap<>();
                map.put("name",name.getText().toString());
                map.put("username",user_name.getText().toString());
                map.put("photo",downloadUri.toString());
                map.put("bio",about_me.getText().toString());

                databaseReference.setValue(map);

                Toast.makeText(getApplicationContext(),
                        "picture has been successfully updated",Toast.LENGTH_LONG).show();
            }
        });
        }
    }
}