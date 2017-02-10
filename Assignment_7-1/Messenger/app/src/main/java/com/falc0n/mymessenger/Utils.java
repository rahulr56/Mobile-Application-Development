package com.falc0n.mymessenger;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static com.falc0n.mymessenger.MainActivity.LOG;

/**
 * Created by fAlc0n on 11/19/16.
 */

public class Utils {
    public static String xor(String a, String b){
        StringBuilder sb = new StringBuilder();
        for(int k=0; k < a.length(); k++)
            sb.append((a.charAt(k) ^ b.charAt(k + (Math.abs(a.length() - b.length()))))) ;
        return sb.toString();
    }
    public static String messageDigest(String a , String b)
    {
        String digestedValue = null;
        byte[] digest ;
        try {
            String toDigest = xor(a,b);
            Log.d(LOG,"XORR is "+toDigest);

            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            digestedValue = messageDigest.digest(toDigest.getBytes()).toString();
            digest = messageDigest.digest(toDigest.getBytes());
            Log.d(LOG,"UTIL:MD5 is "+digestedValue);
            Log.d(LOG,"UTIL:MD5 is "+digest);

            messageDigest = MessageDigest.getInstance("SHA-1");
            digest = messageDigest.digest(toDigest.getBytes());
            digestedValue = messageDigest.digest(toDigest.getBytes()).toString();
            Log.d(LOG,"UTIL:SHA - 1 is "+digestedValue);
            Log.d(LOG,"UTIL:SHA - 1 is "+digest);


            messageDigest = MessageDigest.getInstance("SHA-256");
            digest = messageDigest.digest(toDigest.getBytes());
            digestedValue = messageDigest.digest(toDigest.getBytes()).toString();
            Log.d(LOG,"UTIL:SHA-256 is "+digestedValue);
            Log.d(LOG,"UTIL:SHA-256 is "+digest);

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digestedValue;
    }

    public static void postMessage(String otherUserId,String message, String imageUrl)
    {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String curUserId = auth.getCurrentUser().getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        String key = xor(curUserId,otherUserId);
        DatabaseReference curUserReference = firebaseDatabase.getReference("messages/"+curUserId+"/"+key);
        DatabaseReference otherUserReference = firebaseDatabase.getReference("messages/"+otherUserId+"/"+key);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setMessage(message);
        chatMessage.setUserId(curUserId);
        chatMessage.setImageUrl(imageUrl);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        chatMessage.setTime(format.format(new Date()));
        curUserReference.push().setValue(chatMessage);
        otherUserReference.push().setValue(chatMessage);
    }
    public static void getImageUri(Context context)
    {


    }
    public static void postImageToFireBase(final Context context, final Uri uri, final ImageView imageView)
    {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference();
        storageReference.putFile(uri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Picasso.with(context).load(uri).into(imageView);
            }
        });
    }
}
