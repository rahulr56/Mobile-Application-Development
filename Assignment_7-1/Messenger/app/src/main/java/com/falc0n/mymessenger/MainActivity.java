package com.falc0n.mymessenger;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
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
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {
    final public int GOOGLE_SIGNIN = 1000;
    final public static  String LOG = "MyMessenger";
    final public int FACEBOOK_SIGNIN = 1001;
    GoogleApiClient googleApiClient;
    FirebaseAuth mAuth;
    AlertDialog alertDialog;
    CallbackManager mCallbackManager;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        if(null != mAuth.getCurrentUser())
        {
            Log.d(LOG,"User present");
            startIntent(UsersActivity.class);
        }
        else {
            Log.d(LOG,"User NOT present");
        }
        findViewById(R.id.buttonLILogIn).setOnClickListener(this);
        findViewById(R.id.buttonLIRegister).setOnClickListener(this);
        findViewById(R.id.imageButtonGoogleSignIn).setOnClickListener(this);
        findViewById(R.id.imageButtonLIFacebook).setOnClickListener(this);
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                Log.d(LOG,"In auth listner");
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null)
                {
                    Log.d(LOG,"User is "+user.toString());
                    startIntent(UsersActivity.class);
                }
                else {
                    Log.d(LOG,"User login failed!"+user);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onClick(View view) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Loading....")
                .setCancelable(false);
        alertDialog = builder.create();
        alertDialog.show();
        switch (view.getId())
        {
            case R.id.buttonLIRegister:
                startIntent(SignUp.class);
                break;
            case R.id.buttonLILogIn:
                Log.d(LOG,"LOGIN pressed");
                signIn();
                break;
            case R.id.imageButtonGoogleSignIn:
                Log.d(LOG,"Google pressed");
                signInWithGoogle();
                break;
            case R.id.imageButtonLIFacebook:
                Log.d(LOG,"Facebook pressed");
                FacebookSdk.sdkInitialize(getApplicationContext());
                AppEventsLogger.activateApp(this);
                signInWithFacebook();
                break;
        }
        alertDialog.dismiss();

    }

    private void signInWithGoogle() {
       /* Log.d(LOG,"Login with Google");
        //String serverClientId = getString(R.string.server_client_id);
        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        //
//        .requestIdToken(getResources().getString(R.string.default_web_client_id))

        Log.d(LOG,"googleSignInOptions"+googleSignInOptions.toString());
        googleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                .enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,googleSignInOptions)
                .build();
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        Log.d(LOG,"Starting google activity");
        startActivityForResult(intent, GOOGLE_SIGNIN);*/
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, GOOGLE_SIGNIN);
    }

    private void signInWithFacebook() {
        Log.d(LOG,"Login with FB");
        // Initialize Facebook Login button

        mCallbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(mCallbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        ImageButton loginButton = (ImageButton) findViewById(R.id.imageButtonLIFacebook);
       /* loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }*/

          /*  @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });*/
    }

    private void signIn() {
        String email =((EditText)findViewById(R.id.editTextLIEmail)).getText().toString().trim();
        String password =((EditText)findViewById(R.id.editTextLIPassword)).getText().toString().trim();
        if(email == null || "".matches(email))
        {
            Toast.makeText(this,"Invalid  E-mail address.",Toast.LENGTH_LONG).show();
            return;
        }
        if(password == null || "".matches(password))
        {
            Toast.makeText(this,"Invalid password!",Toast.LENGTH_LONG).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            Log.d(LOG,"Login successful");
                            startIntent(UsersActivity.class);
                        }
                        else {
                            Log.d(LOG,"Login Failed"+task.isSuccessful());
                            Log.d(LOG,"Login Failed"+task.isComplete());
                            Toast.makeText(MainActivity.this,""+task.getException().getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                    alertDialog.dismiss();
                    }
                });
        Log.d(LOG,"Login clicked");
    }

    private void startIntent(Class aClass) {
        Intent signUpIntent = new Intent(MainActivity.this,aClass);
        signUpIntent.setFlags(FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(signUpIntent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(LOG,"In activity for result");
        if(requestCode == GOOGLE_SIGNIN)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
        else if(FACEBOOK_SIGNIN==requestCode)
        {
            super.onActivityResult(requestCode, resultCode, data);
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }
    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            pushToFireBase();
            startActivity(new Intent(getApplicationContext(), UsersActivity.class));
        } else {
            Toast.makeText(MainActivity.this,"Signin failed",Toast.LENGTH_SHORT).show();
        }
     //   progressDialogue.dismiss();
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(LOG,"Connection failed");
    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthStateListener != null) {
            mAuth.removeAuthStateListener(mAuthStateListener);
        }
    }
    void pushToFireBase()
    {
        FirebaseAuth firebaseAuthG = FirebaseAuth.getInstance();
        Map<String, String> userMap = new HashMap<>();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference reference = firebaseDatabase.getReference("users");
        reference.push();

        userMap.put("firstName", firebaseAuthG.getCurrentUser().getDisplayName());
        userMap.put("userId", firebaseAuthG.getCurrentUser().getUid());
        userMap.put("lastName", firebaseAuthG.getCurrentUser().getEmail());
        userMap.put("avatarUrl", firebaseAuthG.getCurrentUser().getPhotoUrl().toString());
        reference.child(firebaseAuthG.getCurrentUser().getUid()).setValue(userMap);
        //userMap.put("gender", firebaseAuthG.getCurrentUser().get);


    }
}
