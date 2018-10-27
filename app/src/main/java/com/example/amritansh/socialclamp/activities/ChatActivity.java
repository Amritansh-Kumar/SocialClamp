package com.example.amritansh.socialclamp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.applications.GetTimeAgo;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChatActivity extends BaseActivity {

    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";

    private String friendUser;
    private String USER_CHAT_REFERNCE;
    private String FRIEND_CHAT_REFERNCE;
    private String USER_MESSAGE_REFERNCE;
    private String FRIEND_MESSAGE_REFERNCE;

    private FirebaseUser currentUser;
    private DatabaseReference userReference;
    private DatabaseReference rootReference;

    @BindView(R.id.toolbar_chat)
    android.support.v7.widget.Toolbar chatToolbar;
    @BindView(R.id.toolbar_username)
    TextView toolbarUsername;
    @BindView(R.id.toolbar_last_seen)
    TextView toolbarLastSeen;
    @BindView(R.id.toolbar_avtar)
    ImageView toolbarvtar;
    @BindView(R.id.chat_message)
    EditText chatMessage;

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected String setActionBarTitle() {
        return null;
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        friendUser = getIntent().getStringExtra(USER_ID);

        USER_CHAT_REFERNCE = "Chats/" + currentUser.getUid() + "/" + friendUser;
        FRIEND_CHAT_REFERNCE = "Chats/" + friendUser + "/" + currentUser.getUid();

        USER_MESSAGE_REFERNCE = "Messages/" + currentUser.getUid() + "/" + friendUser;
        FRIEND_MESSAGE_REFERNCE = "Messages/" + friendUser + "/" + currentUser.getUid();

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child
                (getIntent().getStringExtra(USER_ID));
        rootReference = FirebaseDatabase.getInstance().getReference();

        setSupportActionBar(chatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayToolbarDetails();
        setupChatDatabase();
    }

    private void displayToolbarDetails() {
        toolbarUsername.setText(getIntent().getStringExtra(USERNAME));

        userReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String online = dataSnapshot.child("online").getValue().toString();
                final String image = dataSnapshot.child("thumb_image").getValue().toString();

                if (online.equals("true")){
                    toolbarLastSeen.setText("online");
                }else {
                    GetTimeAgo getTimeAgo = new GetTimeAgo();

                    long lastSeen = Long.parseLong(online);
                    String lastSeenTime = getTimeAgo.getTimeAgo(lastSeen, getApplicationContext());

                    toolbarLastSeen.setText(lastSeenTime);
                }

                if (!image.matches("default")){
                    Picasso.get()
                           .load(image)
                           .networkPolicy(NetworkPolicy.OFFLINE)
                           .resize(50, 50)
                           .centerCrop()
                           .placeholder(R.drawable.useravtar)
                           .into(toolbarvtar, new Callback() {
                               @Override
                               public void onSuccess() {

                               }

                               @Override
                               public void onError(Exception e) {
                                   Picasso.get()
                                          .load(image)
                                          .resize(50, 50)
                                          .centerCrop()
                                          .placeholder(R.drawable.useravtar)
                                          .into(toolbarvtar);
                               }
                           });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupChatDatabase() {

        rootReference.child("Chats").child(currentUser.getUid())
                     .addValueEventListener(new ValueEventListener() {
                         @Override
                         public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(friendUser)){
                                Map chatAddMap = new HashMap();
                                chatAddMap.put("seen", false);
                                chatAddMap.put("timestamp", ServerValue.TIMESTAMP);

                                Map userAddMap = new HashMap();
                                userAddMap.put(USER_CHAT_REFERNCE, chatAddMap);
                                userAddMap.put(FRIEND_CHAT_REFERNCE, chatAddMap);

                                rootReference.updateChildren(userAddMap, new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                                        if (databaseError != null){
                                            Log.d("CHAT_LOG", databaseError.getMessage());
                                        }
                                    }
                                });
                            }
                         }

                         @Override
                         public void onCancelled(@NonNull DatabaseError databaseError) {

                         }
                     });

    }

    @OnClick(R.id.chat_send)
    void sendMessage(){

        String messageBody = chatMessage.getText().toString();

        if (!TextUtils.isEmpty(messageBody)) {

            String messagePushId = rootReference.child("Messages")
                    .child(currentUser.getUid()).child(friendUser).push().getKey();

            Map messageMap = new HashMap();
            messageMap.put("message", false);
            messageMap.put("seen", false);
            messageMap.put("type", "text");
            messageMap.put("timestamp", ServerValue.TIMESTAMP);

            Map messageUserMap = new HashMap();
            messageUserMap.put(USER_MESSAGE_REFERNCE + "/" + messagePushId, messageMap);
            messageUserMap.put(FRIEND_MESSAGE_REFERNCE + "/" + messagePushId, messageMap);

            rootReference.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null){
                        Log.d("MESSAGE_LOG", databaseError.getMessage());
                    }else {
                        chatMessage.setText(null);
                    }
                }
            });
        }
    }
}
