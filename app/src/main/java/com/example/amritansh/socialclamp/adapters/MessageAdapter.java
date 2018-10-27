package com.example.amritansh.socialclamp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amritansh.socialclamp.R;
import com.example.amritansh.socialclamp.models.Messages;
import com.example.amritansh.socialclamp.views.MessageViewHolder;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageViewHolder> {

    private List<Messages> messageList;

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_row, viewGroup, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        messageViewHolder.setMessageText(messageList.get(i).getMessage());
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
