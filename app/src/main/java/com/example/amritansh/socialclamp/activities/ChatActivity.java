package com.example.amritansh.socialclamp.activities;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends BaseActivity {

    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";

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

        String username = getIntent().getStringExtra(USERNAME);

        setSupportActionBar(chatToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbarUsername.setText(username);
    }
}
