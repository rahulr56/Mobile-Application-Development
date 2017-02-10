package com.chatroom.inclass09;

import android.content.Context;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;
import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Raghavan on 31-Oct-16.
 */

public class MessageAdapter extends ArrayAdapter<Message.MessagesBean> {

    Context context;
    int resource;
    List<Message.MessagesBean> msgList ;
    public MessageAdapter(Context context, int resource, List<Message.MessagesBean> msgList) {
        super(context, resource, msgList);
        this.context = context;
        this.resource = resource;
        this.msgList = msgList;
        Log.d("consxss",msgList.size()+"");
    }

    @Override
    public int getCount() {
        Log.d("messages count",""+msgList.size());
        return msgList.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(resource,parent,false);
        }

        Message.MessagesBean msg = msgList.get(position);
        //Log.d("outsersre",""+(String)msg.getFileThumbnailId());
        ImageView img = (ImageView)convertView.findViewById(R.id.imageViewMsg);
        TextView txt = (TextView)convertView.findViewById(R.id.textViewMessageChat);


        try {
            ((TextView) convertView.findViewById(R.id.textViewTimeChat))
                    .setText(new PrettyTime().format(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .parse(msgList.get(position).getCreatedAt())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat inputformatter = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        String date1 = msg.getCreatedAt().toString();
        try {
           Date d = inputformatter.parse(date1);
            PrettyTime prettyTime = new PrettyTime();
            String date2 = prettyTime.format(d);
            ((TextView) convertView.findViewById(R.id.textViewTimeChat)).setText(date2.toString());
           /* SimpleDateFormat dateFormat1 = new SimpleDateFormat("MM/dd");
            String dateText=dateFormat1.format(d);*/

        } catch (ParseException e) {
            e.printStackTrace();
        }



        ((TextView)convertView.findViewById(R.id.textViewNameChat)).setText(msg.getUserFname()+" "+msg.getUserLname());
        if(msgList.get(position).getType().equals("TEXT")){
            txt.setVisibility(View.VISIBLE);
            img.setVisibility(View.INVISIBLE);
            txt.setText(msg.getComment());
        }else{
            img.setVisibility(View.VISIBLE);
            txt.setVisibility(View.INVISIBLE);
            Picasso.with(context).load(MainActivity.URL+"/"+msg.getFileThumbnailId()).into(img);
        }

        return convertView;
    }
}
