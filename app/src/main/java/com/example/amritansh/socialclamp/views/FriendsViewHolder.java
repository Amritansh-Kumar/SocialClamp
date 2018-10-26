package com.example.amritansh.socialclamp.views;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.activities.ChatActivity;
import com.example.amritansh.socialclamp.activities.ProfileActivity;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class FriendsViewHolder extends RecyclerView.ViewHolder {

    private static final String USER_ID = "user_id";
    private static final String USERNAME = "username";

    private View mView;
    private String userId;
    private String usernameTxt;

    @BindView(R.id.useravtar)
    CircleImageView userAvtar;
    @BindView(R.id.username)
    TextView username;
    @BindView(R.id.userstatus)
    TextView userStatus;
    @BindView(R.id.online_img)
    ImageView onlineImage;

    public FriendsViewHolder(@NonNull View itemView) {
        super(itemView);
        mView = itemView;
        ButterKnife.bind(this, mView);
    }

    public void setDisplayData(String thumbUrl, String username, String status, String userId,
                               String isOnline) {
        this.userId = userId;
        this.usernameTxt = username;

        Picasso.get()
               .load(thumbUrl)
               .placeholder(R.drawable.useravtar)
               .into(userAvtar);

        this.username.setText(username);
        userStatus.setText(status);

        if (isOnline.equals("true")){
            onlineImage.setVisibility(View.VISIBLE);
        }else {
            onlineImage.setVisibility(View.INVISIBLE);
        }
    }

    @OnClick(R.id.user_row)
    public void selectAction(){
        String[] optionsList = {"Open Profile", "Send Message"};

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(itemView.getContext());
        alertDialogBuilder.setTitle("Select Options")
                          .setItems(optionsList, new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialog, int which) {
                                  switch (which) {

                                      case 0:
                                          Intent profileIntent = new Intent(itemView.getContext()
                                                  , ProfileActivity.class);
                                          profileIntent.putExtra("userId", userId);
                                          itemView.getContext().startActivity(profileIntent);
                                          break;
                                      case 1:
                                          Intent chatIntent = new Intent(itemView.getContext(),
                                                  ChatActivity.class);
                                          chatIntent.putExtra(USER_ID, userId);
                                          chatIntent.putExtra(USERNAME, usernameTxt);
                                          itemView.getContext().startActivity(chatIntent);
                                          break;
                                      default:
                                          break;

                                  }

                              }
                          });

        alertDialogBuilder.show();
    }
}
