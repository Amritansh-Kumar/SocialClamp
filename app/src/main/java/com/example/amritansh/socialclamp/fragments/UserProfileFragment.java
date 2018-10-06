package com.example.amritansh.socialclamp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileFragment extends Fragment {

    private static final String USER_ID = "userId";
    private static final String SEND_FRIEND_REQUEST = "SEND FRIEND REQUEST";
    private static final String CANCEL_FRIEND_REQUEST = "CANCEL FRIEND REQUEST";
    private static final String ACCEPT_FRIEND_REQUEST = "ACCEPT FRIEND REQUEST";
    private static final String REQUEST_SENT = "request_sent";
    private static final String REQUEST_RECEIVED = "request_received";
    private static final String NOT_FRIENDS = "not_friends";

    private DatabaseReference userProfileRef;
    private DatabaseReference friendRequestRef;
    private FirebaseUser currentUser;

    private String friendId;
    private String currentStatus = NOT_FRIENDS;

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.user_status)
    TextView userStatus;
    @BindView(R.id.user_avtar)
    ImageView userImage;
    @BindView(R.id.request_btn)
    Button requestButton;

    public UserProfileFragment() {
    }

    public static UserProfileFragment getInstance(String userId) {
        Bundle bundle = new Bundle();
        bundle.putString(USER_ID, userId);
        UserProfileFragment fragment = new UserProfileFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        friendId = bundle.getString(USER_ID);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("friend_request");

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                         .child(friendId);
        fillUserData();
    }

    private void fillUserData() {
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                username.setText(name);
                userStatus.setText(status);
                Picasso.get()
                       .load(thumbImage)
                       .placeholder(R.drawable.useravtar)
                       .into(userImage);

                // ____________________ REQUEST STATE ______________
                friendRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(currentUser.getUid())) {

                            String requestType = dataSnapshot.child(currentUser.getUid()).child(friendId)
                                                             .child("request_type").getValue().toString();

                            switch (requestType) {

                                case "send": {
                                    currentStatus = REQUEST_SENT;
                                    requestButton.setText(CANCEL_FRIEND_REQUEST);
                                    break;
                                }
                                case "received": {
                                    currentStatus = REQUEST_RECEIVED;
                                    requestButton.setText(ACCEPT_FRIEND_REQUEST);
                                    break;
                                }
                                default: {
                                    break;
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    @OnClick(R.id.request_btn)
    void acceptOrSendRequest() {

        requestButton.setEnabled(false);

        // _____________________ SEND FRIEND REQUEST _______________________
        if (currentStatus.equals(NOT_FRIENDS)) {
            sendFriendRequest();
        }

        // ________________________ CANCEL FRIEND REQEUST ___________________________
        else if (currentStatus.equals(REQUEST_SENT)) {
            cancelFriendRequest();
        }

        else if(currentStatus.equals(REQUEST_RECEIVED)) {
            acceptFriendRequest();
        }



    }


    private void sendFriendRequest() {
        friendRequestRef.child(currentUser.getUid()).child(friendId)
            .child("request_type").setValue("send")
            .addOnCompleteListener(new OnCompleteListener<Void>() {
                 @Override
                 public void onComplete(@NonNull Task<Void> task) {
                     if (task.isSuccessful()) {
                         friendRequestRef.child(friendId).child(currentUser.getUid())
                             .child("request_type").setValue("received")
                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        requestButton.setEnabled(true);
                                        currentStatus = REQUEST_SENT;
                                        requestButton.setText(CANCEL_FRIEND_REQUEST);
                                    }
                                 });
                     }
                 }
            });
    }

    private void cancelFriendRequest() {

        friendRequestRef.child(currentUser.getUid()).child(friendId).removeValue()
              .addOnSuccessListener(new OnSuccessListener<Void>() {
                   @Override
                   public void onSuccess(Void aVoid) {
                       friendRequestRef.child(friendId).child(currentUser.getUid()).removeValue()
                           .addOnSuccessListener(new OnSuccessListener<Void>() {
                               @Override
                               public void onSuccess(Void aVoid) {
                                   requestButton.setEnabled(true);
                                   currentStatus = NOT_FRIENDS;
                                   requestButton.setText(SEND_FRIEND_REQUEST);
                               }
                           });
                   }
              });
    }

    private void acceptFriendRequest() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View userView = getActivity().findViewById(R.id.all_users_rv);
        View container = getActivity().findViewById(R.id.user_container);
        container.setVisibility(View.GONE);
        userView.setVisibility(View.VISIBLE);
    }
}
