package com.example.amritansh.socialclamp.activities;

import android.content.Context;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.fragments.UserProfileFragment;
import com.example.amritansh.socialclamp.models.User;
import com.example.amritansh.socialclamp.models.interfaces.UserRowClickListner;
import com.example.amritansh.socialclamp.views.AllUsersViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AllUsersActivity extends BaseActivity implements UserRowClickListner {

    private DatabaseReference databaseRef;
    private FirebaseRecyclerAdapter adapter;

    @BindView(R.id.all_users_rv)
    RecyclerView usersRecycler;
    @BindView(R.id.user_container)
    FrameLayout userContainer;

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected String setActionBarTitle() {
        return "All Users";
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_all_users;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        databaseRef = FirebaseDatabase.getInstance().getReference().child("Users");

        usersRecycler.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected void onStart() {
        super.onStart();
        fillData();
    }

    private void fillData(){
        Log.d("console log", "filldata called");

        FirebaseRecyclerOptions<User> options =
                new FirebaseRecyclerOptions.Builder<User>()
                        .setQuery(databaseRef, User.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<User, AllUsersViewHolder>
                (options) {
            Context context;
            @NonNull
            @Override
            public AllUsersViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                                          .inflate(R.layout.all_users_row, viewGroup, false);
                context = viewGroup.getContext();
                return new AllUsersViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull AllUsersViewHolder holder, int position, @NonNull User model) {
                holder.setDisplayData(model, getRef(position).getKey(), context);
            }
        };
        adapter.startListening();
        usersRecycler.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public void onRowClickedListner(String userId) {
        usersRecycler.setVisibility(View.GONE);
        userContainer.setVisibility(View.VISIBLE);

        UserProfileFragment fragment  = UserProfileFragment.getInstance(userId, "allUser");
        super.replaceFragment(R.id.user_container, fragment, true);
    }

}
