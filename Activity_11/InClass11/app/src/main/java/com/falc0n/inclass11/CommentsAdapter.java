package com.falc0n.inclass11;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fAlc0n on 11/14/16.
 */
public class CommentsAdapter extends ArrayAdapter<ChatMessage>{
    ArrayList<ChatMessage> messagesList;
    int resource;
    Context mContext;
    public CommentsAdapter(Context context, int resource, ArrayList<ChatMessage> objects) {
        super(context, resource, objects);
        this.messagesList = objects;
        this.resource = resource;
        this.mContext = context;
    }
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            layoutInflater.inflate(resource,parent,false);
        }
        ChatMessage chatMessage = messagesList.get(position);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.imageViewCommentImage);
        TextView textView = (TextView) convertView.findViewById(R.id.imageViewCommentMessage);
        TextView textViewUser = (TextView) convertView.findViewById(R.id.textViewSenderName);
        textViewUser.setText(chatMessage.getUser());
        TextView textViewTime = (TextView) convertView.findViewById(R.id.textViewMessageTime);
        textViewTime.setText(chatMessage.getTime());
        if(chatMessage.getText().matches(""))
        {

        }else if(chatMessage.getUrl().matches(""))
        {
            textView.setText(chatMessage.getText());
        }
        if(chatMessage.getCommentList().size() > 0)
        {
            ArrayList<ChatMessage> commentsList = chatMessage.getCommentList();
            CommentsAdapter chatMessageAdapter = new CommentsAdapter(mContext,R.layout.comment_item,commentsList);
        }
        return convertView;
    }

}
