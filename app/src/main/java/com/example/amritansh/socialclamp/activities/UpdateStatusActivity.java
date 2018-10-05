package com.example.amritansh.socialclamp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UpdateStatusActivity extends BaseActivity {

    @BindView(R.id.txt_status)
    TextView statusText;

    @Override
    protected boolean showActionBar() {
        return true;
    }

    @Override
    protected String setActionBarTitle() {
        return "Update Status";
    }

    @Override
    protected boolean showBackButton() {
        return true;
    }

    @Override
    protected int setLayoutResourceId() {
        return R.layout.activity_update_status;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.update_btn)
    public void updateStatus(){
        String status = statusText.getText().toString();

        if (status.matches("")){
            status = " ";
        }

        Intent intent = new Intent();
        intent.putExtra(AccountSettingActivity.STATUS, status);
        setResult(AccountSettingActivity.REQUEST_CODE, intent);
        finish();
    }

}
