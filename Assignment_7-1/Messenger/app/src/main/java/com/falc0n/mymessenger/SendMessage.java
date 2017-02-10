package com.falc0n.mymessenger;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.storage.StorageReference;

import static com.falc0n.mymessenger.MainActivity.LOG;

/**
 * Created by fAlc0n on 11/22/16.
 */

public class SendMessage extends AsyncTask<StorageReference,Void,Boolean> {
    ChatActivity activity;

    public SendMessage(ChatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Boolean doInBackground(StorageReference... storageReferences) {
        while(storageReferences[0].getActiveUploadTasks().size()>0)
        {
            Log.d(LOG,"Number of upload tasks = "+storageReferences[0].getActiveUploadTasks().size());
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        activity.setData(aBoolean);
    }
}
/*    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Uri doInBackground(Uri... chatMessages) {
        DateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmssSSS");

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("messageUploads/" + auth.getCurrentUser().getUid()+dateFormat.format(new Date())+ ".jpeg");
        final Uri[] url = new Uri[1];

        storageReference.putFile(chatMessages[0])
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        url[0] = taskSnapshot.getDownloadUrl();
                        Log.d(LOG, "Data from firebase is " + url[0]);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //Toast.makeText(getApplicationContext(), "Failed to upload the selected image. Try again later!", Toast.LENGTH_LONG).show();
                url[0]=null;
            }
        });

        while (storageReference.getActiveUploadTasks().size()>0);
    }

    @Override
    protected void onPostExecute(Uri chatMessage) {

    }
    private void sendMessageFromAlert(String message, Uri uri) {
        Log.d(LOG, "sendMessageFromAlert Message" + message + " URI " + uri);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        final ChatMessage chatMessage = new ChatMessage(message, "NO_IMAGE", dateFormat.format(new Date()), auth.getCurrentUser().getUid());
        final boolean[] flag = {true};
        if (uri != null) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
            StorageReference storageReference = firebaseStorage.getReference("messageUploads/" + auth.getCurrentUser().getUid() + ".jpeg");//+firebaseAuth.getCurrentUser().getUid()+".jpeg");
            final Uri[] url = new Uri[1];
            storageReference.putFile(uri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            url[0] = taskSnapshot.getDownloadUrl();
                            Log.d(LOG, "Data from firebase is " + url[0]);
                            UpdateChatMessageImage(chatMessage, url[0]);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed to upload the selected image. Try again later!", Toast.LENGTH_LONG).show();
                }
            });
            Log.d(LOG, "entering while loop");
            // while (flag[0]) ;
            //Uri imageUri = uploadFileToFirebase(uri,auth.getCurrentUser().getUid());
            UpdateChatMessageImage(chatMessage, selectedImageUri);
        }*/
