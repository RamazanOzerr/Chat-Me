package com.example.basicchatapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.basicchatapp.Notifications.Client;
import com.example.basicchatapp.Notifications.FCMResponse;
import com.example.basicchatapp.R;
import com.example.basicchatapp.Adapters.MessageAdapterr;
import com.example.basicchatapp.Services.APIService;
import com.example.basicchatapp.Utils.Friend;
import com.example.basicchatapp.Utils.MessageModel;
import com.example.basicchatapp.Utils.Profile;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PrivateChatActivity extends AppCompatActivity {

    private ImageView image;
    private EditText editTextMessage;
    private TextView textUserName;

    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    FirebaseUser firebaseUser;
    FirebaseAuth auth;
    private List<MessageModel> messageModelList;
    private List<String> keyList;

    androidx.appcompat.widget.Toolbar mtoolbarofspecificchat;
    private RecyclerView chatRecyView;
    private MessageAdapterr messageAdapter;
    APIService apiService;
    private Boolean notify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_chat);

        init();

    }

    private void init(){
        String url = "https://fcm.googleapis.com/";
        apiService = Client.getClient(url).create(APIService.class);
        notify = false;
        messageModelList = new ArrayList<>();
        keyList = new ArrayList<>();
        chatRecyView = findViewById(R.id.recyclerviewofspecific);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        chatRecyView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapterr(keyList, PrivateChatActivity.this,
                PrivateChatActivity.this, messageModelList);
        chatRecyView.setAdapter(messageAdapter);

        ScrollView scrollView = findViewById(R.id.scrollView);
        scrollView.setNestedScrollingEnabled(false);

        mtoolbarofspecificchat=findViewById(R.id.toolbarofspecificchat);

        image = findViewById(R.id.specificuserimageinimageview);
        ImageView backImage = findViewById(R.id.backImage);
        editTextMessage = findViewById(R.id.editTextMessage);
        textUserName = findViewById(R.id.textUserName);
        ImageButton btnSend = findViewById(R.id.btnSend);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference();
        Intent intent = getIntent();
        String userKey = intent.getStringExtra("UserKey");
        setUserInfo(userKey);

        setSupportActionBar(mtoolbarofspecificchat);
        mtoolbarofspecificchat.setOnClickListener(view -> {
            getUserInfo(userKey);
        });

        backImage.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), MainActivity.class)));

        btnSend.setOnClickListener(view -> {
            notify = true;
            String text = editTextMessage.getText().toString();
            if(text.equals("")){
                Toast.makeText(getApplicationContext(),"write a message first",Toast.LENGTH_LONG).show();
            }else{
                sendMessage(firebaseUser.getUid(),userKey,"text",getDate(),false, text);
                editTextMessage.setText("");

            }

        });
        loadMessage(userKey);
        swipeToRemove(userKey);
    }

    private String getDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm",Locale.getDefault());
        return sdf.format(new Date());
    }


    public void setUserInfo(String userKey){

        reference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Profile profile = snapshot.getValue(Profile.class);
                textUserName.setText(profile.getName());
                String photoPath = snapshot.child("photo").getValue().toString();
                Picasso.get().load(photoPath).into(image);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendMessage(String userID, String otherID, String textType,
                            String date, Boolean seen, String message){

        String messageID = reference.child("Messages").child(userID).child(otherID).push().getKey();

        Map<String, Object> messageMap = new HashMap<>();
        messageMap.put("type",textType);
        messageMap.put("seen",seen);
        messageMap.put("time",date);
        messageMap.put("text",message);
        messageMap.put("from",userID);

        if(messageID != null){
            reference.child("Messages").child(userID).child(otherID)
                    .child(messageID).setValue(messageMap)
                    .addOnCompleteListener(task -> reference.child("Messages")
                            .child(otherID).child(userID).child(messageID)
                            .setValue(messageMap).addOnCompleteListener(task1 -> {

                editTextMessage.requestFocus();
                chatRecyView.smoothScrollToPosition(messageModelList.size());

            }));
        }

        final String msg = message;
        final String receiver = otherID;
        reference.child("Users").child(firebaseUser.getUid())
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                if(notify){
                    sendNotification(receiver, username, msg);
                }
                notify = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void sendNotification(String receiver, String username, String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Users");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    // get token from db
                    String token;
                    try{
                        token = dataSnapshot.child("token").getValue().toString();
                    }catch (Exception e){
                        token = null;
                    }
                    if(token != null){
                        // create json data
                        JsonObject jsonData = new JsonObject();
                        JsonObject jsonBody = new JsonObject();
                        jsonData.addProperty("body",username+": "+message);
                        jsonData.addProperty("title","new message");
                        jsonBody.addProperty("to",token);
                        jsonBody.add("notification",jsonData);

                        // send post request
                        apiService.sendNotification(jsonBody).enqueue(new Callback<FCMResponse>() {
                            @Override
                            public void onResponse(Call<FCMResponse> call, Response<FCMResponse> response) {

                            }

                            @Override
                            public void onFailure(Call<FCMResponse> call, Throwable t) {
                                System.out.println("failed "+t);
                            }
                        });
                    }
                }

            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // userKey değeri bizim other user ımızın id si
    public void loadMessage(String userKey){
        reference.child("Messages").child(firebaseUser.getUid()).child(userKey).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                MessageModel messageModel = snapshot.getValue(MessageModel.class);
                messageModelList.add(messageModel);
                messageAdapter.notifyDataSetChanged();
                keyList.add(userKey);
                chatRecyView.smoothScrollToPosition(messageModelList.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    // get user info that will send to UserProfileActivity
    private void getUserInfo(String userKey){
        reference.child("Users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String photoPath = snapshot.child("photo").getValue().toString();
                String name = snapshot.child("name").getValue().toString();
                String bio = snapshot.child("bio").getValue().toString();
                Intent intent1 = new Intent(getApplicationContext(),UserProfileActivity.class);
                intent1.putExtra("photo",photoPath);
                intent1.putExtra("name",name);
                intent1.putExtra("bio",bio);
                intent1.putExtra("UserKey",userKey);
                startActivity(intent1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }


    private void swipeToRemove(String userKey){
        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView,
                                  @NonNull RecyclerView.ViewHolder viewHolder, @NonNull
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int pos = viewHolder.getAdapterPosition();
                MessageModel deletedMessage = messageModelList.get(pos);
                messageModelList.remove(pos);
                messageAdapter.notifyItemRemoved(pos);
                Snackbar snackbar = Snackbar.make(chatRecyView, deletedMessage.getText()
                                , Snackbar.LENGTH_LONG)
                        .setAction("undo", view -> {
                            messageModelList.add(pos, deletedMessage);
                            messageAdapter.notifyItemInserted(pos);

                        });
                snackbar.show();
                // remove chat from db as well if user didnt undo deletion
                snackbar.addCallback(new Snackbar.Callback() {

                    @Override
                    public void onDismissed(Snackbar snackbar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            // Snackbar closed on its own, means chat will be removed from the db as well
                            deleteMessage(userKey, deletedMessage.getText());
                        }
                    }

                    @Override
                    public void onShown(Snackbar snackbar) {

                    }
                });
            }
            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                    @NonNull RecyclerView.ViewHolder viewHolder, float dX,
                                    float dY, int actionState, boolean isCurrentlyActive) {

                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY,
                        actionState, isCurrentlyActive)
                        .addBackgroundColor(ContextCompat.getColor(getApplicationContext(), R.color.red))
                        .addActionIcon(R.mipmap.ic_delete)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState,
                        isCurrentlyActive);
            }

        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(chatRecyView);

    }

    private void deleteMessage(String otherUser, String targetText){

        reference.child("Messages").child(firebaseUser.getUid()).child(otherUser)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("text").getValue().toString().equals(targetText)){
                                String temp = dataSnapshot.getKey();
                                reference.child("Messages").child(firebaseUser.getUid())
                                        .child(otherUser).child(temp).removeValue((error, ref) -> {
                                        });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}