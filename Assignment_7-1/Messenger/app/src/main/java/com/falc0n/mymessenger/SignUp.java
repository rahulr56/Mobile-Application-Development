package com.falc0n.mymessenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
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

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static com.falc0n.mymessenger.ChatActivity.lastSeen;
import static com.falc0n.mymessenger.MainActivity.LOG;

public class SignUp extends AppCompatActivity implements View.OnClickListener {
    public final static String SIGNUP_KEY = "SIGN_UP";
    public final static int RESULT_LOAD_IMAGE = 1002;
    Spinner spinner;
    String avatarUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        spinner = ((Spinner) findViewById(R.id.spinnerESGender));
        Log.d(LOG, "Spinner is " + spinner);
        String[] genderOptions = {"Male", "Female"};
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.genderOptions, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        findViewById(R.id.buttonESCancel).setOnClickListener(this);
        findViewById(R.id.buttonSUSignUp).setOnClickListener(this);
        findViewById(R.id.imageViewESProfilePic).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonESCancel:
                finish();
                break;
            case R.id.buttonSUSignUp:
                final AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                builder.setTitle("Loading....")
                        .setCancelable(false);
                final AlertDialog alertDialog = builder.create();
                alertDialog.show();
                signUp();
                alertDialog.dismiss();
                break;
            case R.id.imageViewESProfilePic:
                selectPicture();
                break;
        }
    }

    private void selectPicture() {
        Log.d(LOG, "Pic is to be selected");
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, Uri.parse("content://media/internal/images/media"));
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void signUp() {
        final String firstName = ((EditText) findViewById(R.id.editTextESFirstName)).getText().toString().trim();
        final String lastName = ((EditText) findViewById(R.id.editTextSULastName)).getText().toString().trim();
        String email = ((EditText) findViewById(R.id.editTextESEmail)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.editTextESPassword)).getText().toString().trim();
        String repeatedPassword = ((EditText) findViewById(R.id.editTextSURepPassword)).getText().toString().trim();
        //avatarUrl = "NO_IMAGE";
        if (verifyUserName(firstName, lastName) && verifyEmail(email) && verifyPassword(password, repeatedPassword)) {
            final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //Start next screen to choose gender and profile pic
                                Intent intent = new Intent(SignUp.this, UsersActivity.class);
                                Map<String, String> userMap = new HashMap<>();
                                int gender = spinner.getSelectedItemPosition();

                                if (gender == 0) {
                                    userMap.put("gender", "Male");
                                } else {
                                    userMap.put("gender", "Female");
                                }
                                userMap.put("firstName", firstName);
                                userMap.put("userId", firebaseAuth.getCurrentUser().getUid());
                                userMap.put("lastName", lastName);


                                FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
                                DatabaseReference reference = firebaseDatabase.getReference("users");
                                reference.push();

                                if (avatarUrl == null || avatarUrl.matches("NO_IMAGE")) {
                                    userMap.put("avatarUrl", "NO_IMAGE");
                                } else {
                                    uploadFileToFirebase(Uri.parse(avatarUrl),firebaseAuth.getCurrentUser().getUid());
                                    userMap.put("avatarUrl", avatarUrl);
                                }
                                reference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userMap);

                                intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "" + task.getException().getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                Log.d(LOG, "Result is " + task.getException().getCause());
                                Log.d(LOG, "Result is " + task.getException().getLocalizedMessage());
                                Log.d(LOG, "Result is " + task.getException().getMessage());
                            }
                        }
                    });
        }

    }

    private boolean verifyPassword(String password, String repeatedPassword) {
        if (password.matches("") || repeatedPassword.matches("")) {
            ((EditText) findViewById(R.id.editTextESPassword)).setError("Invalid Password!");
            Toast.makeText(getApplicationContext(), "Invalid Password!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (password.length() != repeatedPassword.length()) {
            ((EditText) findViewById(R.id.editTextESPassword)).setError("Passwords do not match!");
            Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean verifyEmail(String email) {
        if (email.matches("") || email == null || !email.contains("@")) {
            ((EditText) findViewById(R.id.editTextESEmail)).setError("Invalid Email address!");
            Toast.makeText(getApplicationContext(), "Invalid Email address!", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean verifyUserName(String firstName, String lastName) {
        if (firstName.matches("") || firstName == null) {
            ((EditText) findViewById(R.id.editTextESEmail)).setError("Invalid First name!");
            Toast.makeText(getApplicationContext(), "Invalid First name!", Toast.LENGTH_LONG).show();
            return false;
        }
        if (lastName.matches("") || lastName == null) {
            ((EditText) findViewById(R.id.editTextESEmail)).setError("Invalid Last Name!");
            Toast.makeText(getApplicationContext(), "Invalid Last name!", Toast.LENGTH_LONG).show();
            return false;
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

    private void uploadFileToFirebase(final Uri selectedImageUri , String currentUserId) {
        FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
        StorageReference storageReference = firebaseStorage.getReference("profilePics/"+currentUserId+".jpeg");//+firebaseAuth.getCurrentUser().getUid()+".jpeg");
        storageReference.putFile(selectedImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        avatarUrl = taskSnapshot.getDownloadUrl().toString();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed to upload the selected image. Try again later!", Toast.LENGTH_LONG).show();
            }
        });
        /*storageReference.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    Picasso.with(getApplicationContext())
                            .load(selectedImageUri)
                            .error(R.drawable.user)
                            .into(imageView, new Callback() {
                                @Override
                                public void onSuccess() {
Toast.makeText()
                                }

                                @Override
                                public void onError() {
                                    Log.d(LOG, "Failed to load the selected image. Try again later!");
                                    Toast.makeText(getApplicationContext(), "Failed to load the selected image. Try again later!", Toast.LENGTH_LONG).show();
                                }
                            });

                    progressDialog.dismiss();
                }
            });*/
    }
    @Override
    protected void onDestroy() {
        //lastSeen();
        super.onDestroy();
    }
}
