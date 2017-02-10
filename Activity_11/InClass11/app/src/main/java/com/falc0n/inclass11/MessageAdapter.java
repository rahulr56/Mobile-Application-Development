package com.falc0n.inclass11;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

import static com.falc0n.inclass11.LoginActivity.LOG;

/**
 * Created by fAlc0n on 11/12/16.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {
    ArrayList<ChatMessage> messagesList;
    int resource;
    Context mContext;
    ItemTouchHelper itemTouchHelper;
    public MessageAdapter(ArrayList<ChatMessage> msgist, int resource, Context mContext, ItemTouchHelper itemTouchHelper) {
        this.messagesList = msgist;
        this.resource = resource;
        this.mContext = mContext;
        this.itemTouchHelper = itemTouchHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resource,parent,false);
        return new MessageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage message = messagesList.get(position);
        holder.imageView.setVisibility(View.VISIBLE);
        holder.textViewUser.setText(message.getUser());
        String time =  message.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
        Date date = null;
        try {
            date = dateFormat.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        PrettyTime prettyTime = new PrettyTime();
        holder.textViewTime.setText(prettyTime.format(date));
        if(message.getUrl().matches("NO_IMAGE"))
        {
            Log.d(LOG,"Setting text to "+message.getText());
            holder.imageView.setVisibility(View.INVISIBLE);
            holder.textView.setText(message.getText());
        }else {
            Log.d(LOG,"Setting image "+message.getUrl());
            Picasso.with(mContext).load(message.getUrl()).into(holder.imageView);
        }
        if(message.getCommentList()!=null && message.getCommentList().size()>0)
        {
            ArrayList<ChatMessage> commnetMessageArrayList = message.getCommentList();
            CommentsAdapter commentsAdapter = new CommentsAdapter(mContext,R.layout.comment_item,commnetMessageArrayList);
            holder.commnetsListView.setAdapter(commentsAdapter);
            commentsAdapter.setNotifyOnChange(true);

        }
    }

    @Override
    public int getItemCount() {
//        Log.d(LOG,"Size is "+messagesList.size());
        return messagesList.size();
    }



    public  class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private TextView textViewUser, textViewTime,textView;
        private ImageView imageView, imageViewDelete,imageViewComment;
        private ListView commnetsListView;
        View.OnClickListener clickListener;
        public ViewHolder(View convertView) {
            super(convertView);
            //Log.d(LOG,"In view holder's constructor");
            //name = (TextView) itemView.findViewById(R.id.textViewRVName);
            imageView = (ImageView) convertView.findViewById(R.id.imageViewRVIcon);
            imageViewDelete = (ImageView) convertView.findViewById(R.id.imageButtonDelete);
            imageViewComment = (ImageView) convertView.findViewById(R.id.imageButtonChat);
            textView = (TextView) convertView.findViewById(R.id.textViewRVName);
            textViewUser = (TextView) convertView.findViewById(R.id.textViewSenderName);
            textViewTime = (TextView) convertView.findViewById(R.id.textViewMessageTime);
            commnetsListView= (ListView) convertView.findViewById(R.id.listViewComments);
            imageViewDelete.setOnClickListener(this);
            imageViewComment.setOnClickListener(this);
        }

        public void setClickListener(View.OnClickListener clickListener) {
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
//            Log.d(LOG,"Clicked item is "+getPosition()+"---");
            switch (view.getId()) {
                case R.id.imageButtonDelete:
                    Log.d(LOG,"Delete pressed");
                    itemTouchHelper.DeleteItem(getAdapterPosition());
                    //DeleteItem(getAdapterPosition());
                    break;
                case R.id.imageButtonChat:
                    Log.d(LOG,"Comment pressed");
                    itemTouchHelper.AddComment(getAdapterPosition());
                    break;
            }
        }
    }
    interface ItemTouchHelper {
        void DeleteItem(int position);
        void AddComment(int position);
    }
}
