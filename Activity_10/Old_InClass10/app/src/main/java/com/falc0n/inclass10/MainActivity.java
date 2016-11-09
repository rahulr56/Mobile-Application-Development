package com.falc0n.inclass10;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
    public final static String LOG_TAG = "FireBaseDemo";
    public final static int RC_SIGN_IN = 100;
    private FirebaseAuth auth;
    static String currentUserId;

    GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        final GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        client = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
                .build();
        findViewById(R.id.buttonLogIn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = ((EditText)findViewById(R.id.editTextEmail)).getText().toString().trim();
                String password = ((EditText)findViewById(R.id.editTextPassword)).getText().toString().trim();
                signIn(email,password);
            }
        });
        findViewById(R.id.buttonNewAccount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });    }


    public  boolean validateForm(String email, String password) {
        boolean valid = true;

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(MainActivity.this,"Email cannot be empty",Toast.LENGTH_LONG).show();
            valid = false;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(MainActivity.this,"Password cannot be empty",Toast.LENGTH_LONG).show();
            valid = false;
        }
        return valid;
    }
    private void signIn(String email, String password) {
        Log.d(LOG_TAG, "signIn:" + email);
        if (!validateForm(email,password)) {
            return;
        }

        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(LOG_TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(LOG_TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this,"Login was not successful!",Toast.LENGTH_LONG).show();
                            }
                        else {
                            currentUserId = auth.getCurrentUser().getUid();
                            Intent intent = new Intent(MainActivity.this,AllExpensesActivity.class);
                            startActivity(intent);
                        }
                    }
                });
    }
/*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RC_SIGN_IN:
                GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                handleSignInResult(signInResult);
        }
    }

    private void handleSignInResult(GoogleSignInResult signInResult) {
        Log.d(LOG_TAG,"handleSignInResult : "+ signInResult.isSuccess());
        if(signInResult.isSuccess())
        {
            GoogleSignInAccount account =  signInResult.getSignInAccount();
            Log.d(LOG_TAG,"Hello, "+account.getDisplayName());
        }
        else {
            Toast.makeText(MainActivity.this,"Login was not successful!",Toast.LENGTH_LONG).show();
        }
    }
*/

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG_TAG,"onConnectionFailed : "+connectionResult);
    }
}
