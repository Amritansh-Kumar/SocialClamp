package com.example.amritansh.socialclamp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.models.Messages;
import com.example.amritansh.socialclamp.views.FriendMessageViewHolder;
import com.example.amritansh.socialclamp.views.UserMessageViewHolder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int USER_MESSAGE = 0;
    private final int FRIEND_MESSAGE = 1;

    private List<Messages> messageList;

    @Override
    public int getItemViewType(int position) {

        String messageFrom = messageList.get(position).getFrom();

        if (messageFrom.equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
            return USER_MESSAGE;
        }else {
            return FRIEND_MESSAGE;
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        LayoutInflater inflater = (LayoutInflater) viewGroup.getContext().getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);

        View view;
        if (i == USER_MESSAGE) {
            view = inflater.inflate(R.layout.message_user_row, viewGroup, false);
            return new UserMessageViewHolder(view);
        } else {
            view = inflater.inflate(R.layout.message_friend_row, viewGroup, false);
            return new FriendMessageViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (getItemViewType(i) == USER_MESSAGE){
            UserMessageViewHolder holder = (UserMessageViewHolder) viewHolder;
            holder.setMessageText(messageList.get(i).getMessage());
        }else {
            FriendMessageViewHolder holder = (FriendMessageViewHolder) viewHolder;
            holder.setMessageText(messageList.get(i).getMessage());
        }
    }

    @Override
    public int getItemCount() {
        if (messageList != null){
            return messageList.size();
        }
        return 0;
    }

    public void updateMessageList(List<Messages> messageList){
        this.messageList = messageList;
        notifyDataSetChanged();
    }
}
