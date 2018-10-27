package com.example.amritansh.socialclamp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.applications.GetTimeAgo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity {

    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";

    private DatabaseReference userReference;

    @BindView(R.id.toolbar_chat)
    android.support.v7.widget.Toolbar chatToolbar;
    @BindView(R.id.toolbar_username)
    TextView toolbarUsername;
    @BindView(R.id.toolbar_last_seen)
    TextView toolbarLastSeen;
    @BindView(R.id.toolbar_avtar)
    ImageView toolbarvtar;
    @BindView(R.id.chat_add)
    ImageView chatAddBtn;
    @BindView(R.id.chat_send)
    ImageView chatSendBtn;
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

        userReference = FirebaseDatabase.getInstance().getReference().child("Users").child
                (getIntent().getStringExtra(USER_ID));

        setSupportActionBar(chatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        displayToolbarDetails();
    }

    private void displayToolbarDetails() {
        String username = getIntent().getStringExtra(USERNAME);

        toolbarUsername.setText(username);

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
}
