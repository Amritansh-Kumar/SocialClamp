package com.example.amritansh.socialclamp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amritansh.socialclamp.R;

import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity {

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
    }
}
