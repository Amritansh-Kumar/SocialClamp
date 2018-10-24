package com.example.amritansh.socialclamp.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private String userId;

    @BindView(R.id.useravtar)
    CircleImageView userAvtar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userstatus)
    TextView userStatus;

    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, mView);
    }

    public void setDisplayData(String thumbUrl, String username, String status, String userId) {
        this.userId = userId;

        Picasso.get()
               .load(thumbUrl)
               .placeholder(R.drawable.useravtar)
               .into(userAvtar);

        this.username.setText(username);
        userStatus.setText(status);

    }
}
