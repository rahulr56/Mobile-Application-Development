package com.falc0n.mymessenger;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.ocpsoft.pretty.time.PrettyTime;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static com.falc0n.mymessenger.MainActivity.LOG;

/**
 * Created by fAlc0n on 11/19/16.
 */

public class ChatMessageAdapter extends ArrayAdapter<ChatMessage> {
    Context mContext;
    int mResource;
    ArrayList<ChatMessage> chatMessageArrayList;

    public ChatMessageAdapter(Context context, int resource, ArrayList<ChatMessage> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mResource = resource;
        this.chatMessageArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource,parent,false);
        }
        convertView.findViewById(R.id.RRCMSentMessage).setVisibility(View.VISIBLE);
        convertView.findViewById(R.id.RRCMReceivedMessage).setVisibility(View.VISIBLE);
        ChatMessage chatMessage = chatMessageArrayList.get(position);
        ImageView imageView ;
        TextView textView, textViewMTime;
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        if(chatMessage.getUserId().matches(firebaseAuth.getCurrentUser().getUid()) ) {
            //Current user message
            convertView.findViewById(R.id.RRCMReceivedMessage).setVisibility(View.GONE);
            imageView = (ImageView) convertView.findViewById(R.id.imageViewCMSentSentImage);
            imageView.setVisibility(View.VISIBLE);
            textView = (TextView) convertView.findViewById(R.id.textViewCMSentSentMessage);
            textView.setVisibility(View.VISIBLE);
            textViewMTime = (TextView) convertView.findViewById(R.id.textViewCMSentMessageTime);
        }else
        {
            //Received message from other user
            convertView.findViewById(R.id.RRCMSentMessage).setVisibility(View.GONE);
            imageView = (ImageView) convertView.findViewById(R.id.imageViewCMRecvSentImage);
            imageView.setVisibility(View.VISIBLE);
            textView = (TextView) convertView.findViewById(R.id.textViewCMRecvSentMessage);
            textView.setVisibility(View.VISIBLE);
            textViewMTime = (TextView) convertView.findViewById(R.id.textViewCMRecvMessageTime);
        }
        if(chatMessage.getImageUrl().matches("NO_IMAGE"))
        {
            imageView.setVisibility(View.GONE);
        }else {
            Picasso.with(mContext).load(chatMessage.getImageUrl()).into(imageView);
        }
        if(chatMessage.getMessage().matches("") || chatMessage.getMessage() == null)
        {
            textView.setVisibility(View.GONE);
        }else {
            textView.setText(chatMessage.getMessage());
        }
        //Print pretty time
        Date date = null;
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        try {
            date=dateFormat.parse(chatMessage.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d(LOG,"Setting message to "+chatMessage.toString());
        PrettyTime prettyTime = new PrettyTime();
        textViewMTime.setText(prettyTime.format(date));
        return convertView;
    }
}
