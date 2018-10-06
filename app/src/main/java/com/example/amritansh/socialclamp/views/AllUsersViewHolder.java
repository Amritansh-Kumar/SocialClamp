package com.example.amritansh.socialclamp.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.models.User;
import com.example.amritansh.socialclamp.models.interfaces.UserRowClickListner;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersViewHolder extends RecyclerView.ViewHolder {

    private View mView;
    private String userId;
    private UserRowClickListner rowClickListner;

    @BindView(R.id.useravtar)
    CircleImageView userAvtar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userstatus)
    TextView userStatus;

    public AllUsersViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, mView);
    }

    public void setDisplayData(User model, String userId, Context context) {

        this.userId = userId;
        rowClickListner = (UserRowClickListner) context;

            Picasso.get()
                   .load(model.getImage())
                   .placeholder(R.drawable.useravtar)
                   .into(userAvtar);

            username.setText(model.getUsername());
            userStatus.setText(model.getStatus());

    }

    @OnClick(R.id.user_row)
    public void viewUser(){
        rowClickListner.onRowClickedListner(userId);
    }
}
