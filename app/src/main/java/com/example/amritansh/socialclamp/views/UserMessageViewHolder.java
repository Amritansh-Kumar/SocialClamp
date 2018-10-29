package com.example.amritansh.socialclamp.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMessageViewHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.message_text)
    TextView messageText;

    public UserMessageViewHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    public void setMessageText(String messageText){
        this.messageText.setText(messageText);
    }


}
