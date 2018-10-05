package com.example.amritansh.socialclamp.activities;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.fragments.LoginFragment;
import com.example.amritansh.socialclamp.fragments.RegisterFragment;
import com.example.amritansh.socialclamp.models.interfaces.AuthenticationListner;

public class AuthenticationActivity extends BaseActivity implements AuthenticationListner {

    @Override
    protected boolean showActionBar() {
        return false;
    }

    @Override
    protected String setActionBarTitle() {
        return "";
    }

    @Override
    protected boolean showBackButton() {
        return false;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_authentication;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fragment loginFrag = LoginFragment.getInstance(this);
        super.replaceFragment(R.id.container, loginFrag, false);
    }

    @Override
    public void login() {
        Fragment loginFrag = LoginFragment.getInstance(this);
        super.replaceFragment(R.id.container, loginFrag, true);
    }

    @Override
    public void register() {
        Fragment registerFrag = RegisterFragment.getInstance(this);
        super.replaceFragment(R.id.container, registerFrag, true);
    }
}
