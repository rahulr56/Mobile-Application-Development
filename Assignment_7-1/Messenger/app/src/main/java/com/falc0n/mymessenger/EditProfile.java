package com.falc0n.mymessenger;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import static com.falc0n.mymessenger.ChatActivity.lastSeen;
import static com.falc0n.mymessenger.MainActivity.LOG;
import static com.falc0n.mymessenger.SignUp.RESULT_LOAD_IMAGE;

public class EditProfile extends AppCompatActivity {
    FirebaseAuth auth = FirebaseAuth.getInstance();
    User currentUser = new User();
    String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("users/" + auth.getCurrentUser().getUid());
        if (getIntent().getExtras() != null) {
            Log.d(LOG, " 1 URL is " + avatarUrl);
            findViewById(R.id.textViewESFirstName).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextESFirstName).setVisibility(View.VISIBLE);
            findViewById(R.id.editTextESLastName).setVisibility(View.VISIBLE);
            ImageView imageViewDP = ((ImageView) findViewById(R.id.imageViewESProfilePic));

            String type = (String) getIntent().getExtras().get("ACTIVITY_KEY");
            currentUser = (User) getIntent().getExtras().get("USER");
            avatarUrl = currentUser.getAvatarUrl();
            Log.d(LOG, " 1 URL is " + avatarUrl);
            Picasso.with(this).load(currentUser.getAvatarUrl()).into(imageViewDP);
            if (type.equals("EDIT")) {
                ((EditText) findViewById(R.id.editTextESFirstName)).setHint(currentUser.getFirstName());
                ((EditText) findViewById(R.id.editTextESLastName)).setHint(currentUser.getLastName());
                findViewById(R.id.textViewESFirstName).setVisibility(View.GONE);
                //User user = new User();
            } else if (type.equals("VIEW")) {
                ((TextView) findViewById(R.id.textViewESFirstName)).setText("Name : " + currentUser.getFirstName() + " " + currentUser.getLastName());
                findViewById(R.id.buttonESSave).setVisibility(View.GONE);
                findViewById(R.id.editTextESFirstName).setVisibility(View.GONE);
                findViewById(R.id.editTextESLastName).setVisibility(View.GONE);
                ((Button) findViewById(R.id.buttonESCancel)).setText("CLOSE");
                if (avatarUrl.equals("NO_IMAGE")) {
                    imageViewDP.setImageResource(R.drawable.user);
                }
            }
            findViewById(R.id.buttonESCancel).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                }
            });
            findViewById(R.id.buttonESSave).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    updateProfile();
                    finish();
                }
            });
            findViewById(R.id.imageViewESProfilePic).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT, Uri.parse("content://media/internal/images/media"));
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                }
            });

        }
    }

    private void updateProfile() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseDatabase firebaseDB = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDB.getReference("users/" + auth.getCurrentUser().getUid());

        String firstName = ((EditText) findViewById(R.id.editTextESFirstName)).getText().toString().trim();
        String lastName = ((EditText) findViewById(R.id.editTextESLastName)).getText().toString().trim();

        if (verifyUserName(firstName, lastName)) {
            Log.d(LOG, "db URL is " + avatarUrl);
            currentUser.setFirstName(firstName);
            currentUser.setLastName(lastName);
            currentUser.setAvatarUrl(avatarUrl);
            Map<String, Object> userMap = new HashMap<>();
            String currUrl = currentUser.getAvatarUrl();
            Log.d(LOG, "After map URL is " + avatarUrl);
            if (!avatarUrl.equals(currUrl)) {
                uploadFileToFirebase(Uri.parse(avatarUrl));
            }
            userMap.put("firstName", currentUser.getFirstName());
            userMap.put("lastName", currentUser.getLastName());
            userMap.put("gender", currentUser.getGender());
            userMap.put("avatarUrl", currentUser.getAvatarUrl());
            userMap.put("lastName", lastName);
            databaseReference.updateChildren(userMap);
        }
    }

    private boolean verifyUserName(String firstName, String lastName) {
        if (firstName.matches("") || firstName == null) {
            firstName = currentUser.getFirstName();
        } else {
            currentUser.setFirstName(firstName);
        }
        if (lastName.matches("") || lastName == null) {
            lastName = currentUser.getLastName();
        } else {
            currentUser.setLastName(lastName);
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK) {
            final Uri selectedImageUri = data.getData();
            avatarUrl = selectedImageUri.toString();
            Log.d(LOG, "The image uri is " + selectedImageUri.toString());
            Log.d(LOG, "The image uri is " + selectedImageUri);
            final ImageView imageView = (ImageView) findViewById(R.id.imageViewESProfilePic);
            final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setTitle("Loading...");
            progressDialog.create();
            Picasso.with(getApplicationContext())
                    .load(selectedImageUri)
                    .error(R.drawable.user)
                    .into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressDialog.dismiss();
                        }

                        @Override
                        public void onError() {
                            Log.d(LOG, "Failed to load the selected image. Try again later!");
                            Toast.makeText(getApplicationContext(), "Failed to load the selected image. Try again later!", Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void uploadFileToFirebase(final Uri selectedImageUri) {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid();
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("profilePics/" + currentUserId + ".jpeg");
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        avatarUrl = taskSnapshot.getDownloadUrl().toString();
                        currentUser.setAvatarUrl(taskSnapshot.getDownloadUrl().toString());
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload the selected image. Try again later!", Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    protected void onDestroy() {
        lastSeen();
        super.onDestroy();
    }
}
