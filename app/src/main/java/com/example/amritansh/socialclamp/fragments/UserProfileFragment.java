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
import android.widget.Toast;

import com.example.amritansh.socialclamp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserProfileFragment extends Fragment {

    private static final String USER_ID = "userId";
    private static final String SEND_FRIEND_REQUEST = "SEND FRIEND REQUEST";
    private static final String CANCEL_FRIEND_REQUEST = "CANCEL FRIEND REQUEST";
    private static final String ACCEPT_FRIEND_REQUEST = "ACCEPT FRIEND REQUEST";
    private static final String UNFRIEND = "UNFRIEND";
    private static final String REQUEST_SENT = "request_sent";
    private static final String REQUEST_RECEIVED = "request_received";
    private static final String NOT_FRIENDS = "not_friends";
    private static final String FRIENDS = "friends";

    private DatabaseReference userProfileRef;
    private DatabaseReference friendRequestRef;
    private DatabaseReference friendDatabaseRef;
    private DatabaseReference mNotificationRef;
    private DatabaseReference mRootRef;
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
    @BindView(R.id.decline_btn)
    Button declineButton;

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

        mRootRef = FirebaseDatabase.getInstance().getReference();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        friendRequestRef = FirebaseDatabase.getInstance().getReference().child("friend_request");

        friendDatabaseRef = FirebaseDatabase.getInstance().getReference().child("friends");

        mNotificationRef = FirebaseDatabase.getInstance().getReference().child("notifications");

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                         .child(friendId);

        declineButton.setVisibility(View.INVISIBLE);
        fillUserData();
    }

    // getting user profile and displaying it
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


                getRequestState();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // checking the request status of friend request
    private void getRequestState(){
        // ________________ REQUEST STATE ______________

        friendRequestRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(currentUser.getUid())) {

                    friendRequestRef.child(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(friendId)){

                                String requestType = dataSnapshot.child(friendId)
                                                                 .child("request_type").getValue().toString();

                                switch (requestType) {

                                    case "send": {
                                        currentStatus = REQUEST_SENT;
                                        requestButton.setText(CANCEL_FRIEND_REQUEST);
                                        break;
                                    }
                                    case "received": {
                                        declineButton.setVisibility(View.VISIBLE);
                                        currentStatus = REQUEST_RECEIVED;
                                        requestButton.setText(ACCEPT_FRIEND_REQUEST);
                                        break;
                                    }
                                    default: {
                                        break;
                                    }
                                }
                            }else {
                                checkIsFriendStatus();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // checking if already friend
    private void checkIsFriendStatus(){

        friendRequestRef.child(currentUser.getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(friendId)){
                                    currentStatus = FRIENDS;
                                    requestButton.setText(UNFRIEND);
                                }
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
        else if (currentStatus.equals(FRIENDS)){
            unfriendUser();
        }

    }


    private void sendFriendRequest() {

        String notificationId = mNotificationRef.push().getKey();

//        HashMap<String, String> notificationData = new HashMap<>();
//        notificationData.put("from", currentUser.getUid());
//        notificationData.put("type", "request");

        Map requestMap = new HashMap();
        requestMap.put("friend_request/" + currentUser.getUid() + "/" + friendId + "/request_type",
                "sent");
        requestMap.put("friend_request/" + friendId + "/" + currentUser.getUid() + "/request_type",
                "received");
        requestMap.put("notifications/" + friendId + "/" + notificationId,
                genereteNotificationData(currentUser.getUid(), "request"));


        mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                if (databaseError != null){
                    Toast.makeText(getActivity(), "There was some error in sending request",
                            Toast.LENGTH_SHORT).show();
                }else {
                    currentStatus = REQUEST_SENT;
                    requestButton.setText(CANCEL_FRIEND_REQUEST);
                }
                requestButton.setEnabled(true);

            }
        });


//        friendRequestRef.child(currentUser.getUid()).child(friendId)
//            .child("request_type").setValue("send")
//            .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//                @Override
//                 public void onComplete(@NonNull Task<Void> task) {
//                     if (task.isSuccessful()) {
//                         friendRequestRef.child(friendId).child(currentUser.getUid())
//                             .child("request_type").setValue("received")
//                                 .addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//
//                                        HashMap<String, String> notificatinData = genereteNotificationData(currentUser.getUid(),
//                                                        "request");
//                                        mNotificationRef.child(friendId).push().setValue
//                                                (notificatinData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                requestButton.setEnabled(true);
//                                                currentStatus = REQUEST_SENT;
//                                                requestButton.setText(CANCEL_FRIEND_REQUEST);
//                                            }
//                                        });
//                                    }
//
//                                 });
//                     }
//                 }
//
//            });

    }

    private void cancelFriendRequest() {

        Map cancelRequestMap = new HashMap();
        cancelRequestMap.put("friend_request/" + currentUser.getUid() + "/" + friendId,
                null);
        cancelRequestMap.put("friend_request/" + friendId + "/" + currentUser.getUid(),
                null);

        mRootRef.updateChildren(cancelRequestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null){
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();

                }else {
                    currentStatus = NOT_FRIENDS;
                    requestButton.setText(SEND_FRIEND_REQUEST);
                }
                requestButton.setEnabled(true);
            }
        });

//        friendRequestRef.child(currentUser.getUid()).child(friendId).removeValue()
//              .addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                  @Override
//                   public void onSuccess(Void aVoid) {
//                       friendRequestRef.child(friendId).child(currentUser.getUid()).removeValue()
//                           .addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                               @Override
//                               public void onSuccess(Void aVoid) {
//                                   requestButton.setEnabled(true);
//                                   currentStatus = NOT_FRIENDS;
//                                   requestButton.setText(SEND_FRIEND_REQUEST);
//                               }
//
//                           }).addOnFailureListener(new OnFailureListener() {
//                           @Override
//                           public void onFailure(@NonNull Exception e) {
//                               requestButton.setEnabled(true);
//                           }
//                       });
//                   }
//
//              });

    }

    private void acceptFriendRequest() {
        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());

        Map acceptRequestMap = new HashMap();
        acceptRequestMap.put("friends/" + currentUser.getUid() + "/" + friendId + "/date",
                currentDate);
        acceptRequestMap.put("friends/" + friendId + "/" + currentUser.getUid() + "/date",
                currentDate);

        acceptRequestMap.put("friend_request/" + currentUser.getUid() + "/" + friendId,
                null);
        acceptRequestMap.put("friend_request/" + friendId + "/" + currentUser.getUid(),
                null);

        mRootRef.updateChildren(acceptRequestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                if (databaseError != null){
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                }else {
                    currentStatus = FRIENDS;
                    requestButton.setText(UNFRIEND);
                }
                requestButton.setEnabled(true);
            }
        });

//        friendDatabaseRef.child(currentUser.getUid()).child(friendId).setValue(currentDate)
//            .addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                @Override
//                public void onSuccess(Void aVoid) {
//                    friendDatabaseRef.child(friendId).child(currentUser.getUid())
//                        .setValue(currentDate)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                            @Override
//                            public void onSuccess(Void aVoid) {
//                                friendRequestRef.child(currentUser.getUid()).child(friendId)
//                                     .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                                    @Override
//                                    public void onSuccess(Void aVoid) {
//                                        friendRequestRef.child(friendId).child(currentUser.getUid())
//                                           .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//
//                                            @Override
//                                            public void onSuccess(Void aVoid) {
//                                                requestButton.setEnabled(true);
//                                                currentStatus = FRIENDS;
//                                                requestButton.setText(UNFRIEND);
//                                            }
//
//                                        }).addOnFailureListener(new OnFailureListener() {
//                                            @Override
//                                            public void onFailure(@NonNull Exception e) {
//                                                requestButton.setEnabled(true);
//                                            }
//                                        });
//                                    }
//
//                                });
//                            }
//
//                        });
//                }
//
//            });

    }


    private void unfriendUser(){

        Map unfriendMap = new HashMap();
        unfriendMap.put("friends/" + currentUser.getUid() + "/" + friendId, null);
        unfriendMap.put("friends/" + friendId + "/" + currentUser.getUid(), null);

        mRootRef.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                if (databaseError != null){
                    Toast.makeText(getActivity(), "something went wrong",
                            Toast.LENGTH_SHORT).show();
                }else {
                    currentStatus = NOT_FRIENDS;
                    requestButton.setText(SEND_FRIEND_REQUEST);


                }
                requestButton.setEnabled(true);
            }
        });

//        friendDatabaseRef.child(currentUser.getUid()).child(friendId)
//                         .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                friendDatabaseRef.child(friendId).child(currentUser.getUid())
//                                 .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        requestButton.setEnabled(true);
//                        currentStatus = NOT_FRIENDS;
//                        requestButton.setText(SEND_FRIEND_REQUEST);
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        requestButton.setEnabled(true);
//                    }
//                });
//            }
//        });
    }


    @OnClick(R.id.decline_btn)
    void declineRequest(){

        Map requestMap = new HashMap();
        requestMap.put("friend_request/" + currentUser.getUid() + "/" + friendId, null);
        requestMap.put("friend_request/" + friendId + "/" + currentUser.getUid(), null);

        mRootRef.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                if (databaseError != null){
                    Toast.makeText(getActivity(), "Something went wrong",
                            Toast.LENGTH_SHORT).show();
                }else {
                    currentStatus = NOT_FRIENDS;
                    requestButton.setText(SEND_FRIEND_REQUEST);
                }
                requestButton.setEnabled(true);
                declineButton.setVisibility(View.INVISIBLE);

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        View userView = getActivity().findViewById(R.id.all_users_rv);
        View container = getActivity().findViewById(R.id.user_container);
        container.setVisibility(View.GONE);
        userView.setVisibility(View.VISIBLE);
    }


    private HashMap<String, String> genereteNotificationData(String senderId, String requestType){
        HashMap<String, String> notificationMap = new HashMap<>();
        notificationMap.put("from", senderId);
        notificationMap.put("type", requestType);

        return  notificationMap;
    }
}
