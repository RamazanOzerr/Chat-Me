package com.example.basicchatapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.basicchatapp.R;
import com.example.basicchatapp.Utils.RandomString;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUpSetProfile extends AppCompatActivity {

    private CircleImageView sign_up_profile_image;
    private EditText sign_up_name, sign_up_user_name, sign_up_about_me;
    private Button sign_up_setBtn;
    DatabaseReference reference;
    FirebaseAuth auth;
    StorageReference storageReference;
    private Boolean hasPhotoSet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_set_profile);

        init();
        sign_up_setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sign_up_user();
            }
        });

        sign_up_profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,3);
            }
        });


    }

    private void init(){
        sign_up_about_me = findViewById(R.id.sign_up_about_me);
        sign_up_profile_image = findViewById(R.id.sign_up_profile_image);
        sign_up_name = findViewById(R.id.sign_up_name);
        sign_up_user_name = findViewById(R.id.sign_up_user_name);
        sign_up_setBtn = findViewById(R.id.sign_up_setBtn);
        reference = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        auth = FirebaseAuth.getInstance();
        hasPhotoSet = false;
    }

    private void sign_up_user(){
        if(auth.getUid() == null){
            Toast.makeText(getApplicationContext(),"USER NOT FOUND",Toast.LENGTH_SHORT).show();
        } else if(!hasPhotoSet){
            Toast.makeText(getApplicationContext(),"you gotta set a photo first",Toast.LENGTH_SHORT).show();
        } else if (sign_up_name.getText().toString() == null
                || sign_up_user_name.getText().toString() == null
                || sign_up_about_me.getText().toString() == null) {
            Toast.makeText(getApplicationContext(),"you gotta fill all the blanks",Toast.LENGTH_SHORT).show();
        } else{
            Map<String, Object> map = new HashMap();
            map.put("name",sign_up_name.getText().toString());
            map.put("username",sign_up_user_name.getText().toString());
            map.put("bio",sign_up_about_me.getText().toString());
            reference.child("Users").child(auth.getUid()).updateChildren(map);
            getToken();
            startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        }
    }

    private void getToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Toast.makeText(SignUpSetProfile.this,
                                "Fetching FCM registration token failed"+ task.getException(),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Map<String, Object> hm = new HashMap<>();
                    hm.put("token",token);
                    reference.child("Users").child(auth.getUid()).updateChildren(hm);
                });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode, data);
        if(resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();

            sign_up_name = findViewById(R.id.sign_up_name);
            sign_up_user_name = findViewById(R.id.sign_up_user_name);
            sign_up_about_me = findViewById(R.id.sign_up_about_me);
            sign_up_profile_image = findViewById(R.id.sign_up_profile_image);
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
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", sign_up_name.getText().toString());
                    map.put("username", sign_up_user_name.getText().toString());
                    map.put("photo", downloadUri.toString());
                    map.put("bio", sign_up_about_me.getText().toString());

                    reference.child("Users").child(auth.getUid()).setValue(map);
                    sign_up_profile_image.setImageURI(selectedImage);
                    hasPhotoSet = true;
                    Toast.makeText(getApplicationContext(),
                            "picture has been successfully added", Toast.LENGTH_LONG).show();
                }
            });
        }
    }
}