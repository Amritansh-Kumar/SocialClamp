package com.example.amritansh.socialclamp.activities;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amritansh.socialclamp.R;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity {

    android.support.v7.widget.Toolbar toolbar;
    TextView toolbarTitle;

    private FirebaseAuth mAuth;

    protected abstract boolean showActionBar();

    protected abstract String setActionBarTitle();

    protected abstract boolean showBackButton();

    protected abstract int setLayoutResourceId();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayoutResourceId());
        ButterKnife.bind(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mAuth = FirebaseAuth.getInstance();
        handleToolbar();

        // TODO : add permissino checks
        // TODO : handle no internet connection
    }

    private void handleToolbar() {
        if (showActionBar()) {
            toolbar = findViewById(R.id.toolbar_main);
            setSupportActionBar(toolbar);
            toolbar.setVisibility(View.VISIBLE);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            if (setActionBarTitle() != null) {
                toolbarTitle = findViewById(R.id.toolbar_title);
                toolbarTitle.setVisibility(View.VISIBLE);
                toolbarTitle.setText(setActionBarTitle());

                if (showBackButton()) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                    getSupportActionBar().setHomeButtonEnabled(true);
                }
            } else {
                if (toolbar != null) {
                    toolbar.setVisibility(View.GONE);
                }
            }
        }
    }

    public void replaceFragment(int containerId, Fragment fragment, boolean addToBackStack) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        if (fragment != null) {
            transaction.replace(containerId, fragment);
            if (addToBackStack) {
                transaction.addToBackStack(null);
            }
            transaction.commit();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.settings: {
                Intent settingIntent = new Intent(this, AccountSettingActivity.class);
                startActivity(settingIntent);
                finish();
                break;
            }

            case R.id.logout: {
                mAuth.signOut();
                Intent intent = new Intent(this, AuthenticationActivity.class);
                startActivity(intent);
                finish();
                break;
            }

            case R.id.all_users: {
                Intent allUsersIntent = new Intent(this, AllUsersActivity.class);
                startActivity(allUsersIntent);
                finish();
                break;
            }

            case android.R.id.home:
                onBackPressed();
        }

        return true;
    }
}
