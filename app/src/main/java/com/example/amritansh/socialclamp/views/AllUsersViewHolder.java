package com.example.amritansh.socialclamp.views;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

        Log.d("console log", "imageurlprofile" + model.getThumb_image());

            Picasso.get()
                   .load(model.getImage())
                   .placeholder(R.drawable.useravtar)
                   .into(userAvtar);

            username.setText(model.getUsername());
            userStatus.setText(model.getStatus());
    }
}
