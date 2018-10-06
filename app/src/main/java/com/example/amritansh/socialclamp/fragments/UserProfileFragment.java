package com.example.amritansh.socialclamp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
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

public class UserProfileFragment extends Fragment {

    private static final String USER_ID = "userId";

    private DatabaseReference currentRef;
    private DatabaseReference userRef;
    private DatabaseReference userProfileRef;
    private FirebaseUser currentUser;

    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.user_status)
    TextView userStatus;
    @BindView(R.id.user_avtar)
    ImageView userImage;

    public UserProfileFragment() {
    }

    public static UserProfileFragment getInstance(String userId){
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
        String friendId = bundle.getString(USER_ID);
        Log.d("consolelog", "consoleuserid" + friendId);

        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        currentRef = FirebaseDatabase.getInstance().getReference().child("friend_requests")
                                     .child(currentUser.getUid());
        Log.d("console", "consoleuserid" + currentUser.getUid());
        userRef = FirebaseDatabase.getInstance().getReference().child("friend_requests")
                                  .child(friendId);

        userProfileRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                  .child(friendId);

        fillUserData();
    }

    private void fillUserData() {
        userProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("username").getValue().toString();
                Log.d("console", "userimagese" + name);
                String status = dataSnapshot.child("status").getValue().toString();
                String thumbImage = dataSnapshot.child("thumb_image").getValue().toString();

                username.setText(name);
                userStatus.setText(status);
                Picasso.get()
                       .load(thumbImage)
                       .placeholder(R.drawable.useravtar)
                       .into(userImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
}
