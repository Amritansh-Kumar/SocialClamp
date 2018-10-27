package com.example.amritansh.socialclamp.activities;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.adapters.PagerAdapter;
import com.example.amritansh.socialclamp.fragments.ChatFragment;
import com.example.amritansh.socialclamp.fragments.FriendsFragment;
import com.example.amritansh.socialclamp.fragments.RequestsFragment;
import com.example.amritansh.socialclamp.models.ViewPagerItem;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;

    private PagerAdapter pagerAdapter;
    private List<ViewPagerItem> pagerItemList = new ArrayList<>();

    private FirebaseAuth mAuth;
    private DatabaseReference userDatabase;

    public static Intent newInstance(Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected String setActionBarTitle() {
        return "SocialClamp";
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        userDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child
                (currentUser.getUid());

        if (currentUser == null){
            Intent intent = new Intent(this, AuthenticationActivity.class);
            startActivity(intent);
            finish();
        }else {
            setUpViewPager();
        }
    }

    private void setUpViewPager() {
        pagerAdapter = new PagerAdapter(getSupportFragmentManager());

        pagerItemList.add(new ViewPagerItem(RequestsFragment.createFragment(), "Requests"));
        pagerItemList.add(new ViewPagerItem(ChatFragment.createFragment(), "Chats"));
        pagerItemList.add(new ViewPagerItem(FriendsFragment.createFragment(), "Friends"));

        viewPager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        pagerAdapter.updateList(pagerItemList);
        tabLayout.setHorizontalScrollBarEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) {
            userDatabase.child("online").setValue(true);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth.getCurrentUser() != null) {
            userDatabase.child("online").setValue(ServerValue.TIMESTAMP);
        }
    }
}
