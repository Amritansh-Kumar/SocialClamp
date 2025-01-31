package com.example.amritansh.socialclamp.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.fragments.UserProfileFragment;

import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity{
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
        return R.layout.activity_profile;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        String userId = getIntent().getExtras().getString("userId");

        replaceFragment(R.id.container, UserProfileFragment.getInstance(userId, "profile")
                , false);
    }
}
