package com.example.amritansh.socialclamp.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.models.User;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersViewHolder extends RecyclerView.ViewHolder {

    View mView;

//    @BindView(R.id.useravtar)
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

    public void setDisplayData(User model) {

        userAvtar = itemView.findViewById(R.id.useravtar);

            Picasso.get()
                   .load(model.getImageUrl())
                   .placeholder(R.drawable.useravtar)
                   .into(userAvtar);

            username.setText(model.getUsername());
            userStatus.setText(model.getStatus());
    }

//    public void setUsername(String username) {
//
//        Log.d("console log", "set up data" + username);
//        this.username = mView.findViewById(R.id.username);
//        this.username.setText(username);
//    }
}
