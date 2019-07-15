package com.example.orderfoodsystem;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfoodsystem.Common.Common;
import com.example.orderfoodsystem.Model.Users;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import java.util.Arrays;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignIn extends AppCompatActivity {

    static final int GOOGLE_SIGN = 123;

    // Initialize Facebook Login button
    private CallbackManager mCallbackManager;

    private FirebaseAuth mAuth;

    private static final String TAG = "FACELOG";


    private ImageButton mFacebookBtn, mGoogleBtn, btn_sign;

    GoogleSignInClient mGoogleSignInClient;

    private TextView link_regist;

    FirebaseDatabase database;
    DatabaseReference category;

    EditText edtName, edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        mFacebookBtn =  findViewById(R.id.facebookBtn);

        mGoogleBtn = findViewById(R.id.googleBtn);

        btn_sign =  findViewById(R.id.btn_sign);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtName = (EditText) findViewById(R.id.edtName);
//        edtPhone = findViewById(R.id.edtPhone);

        link_regist =(TextView) findViewById(R.id.link_regist);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");


        link_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        DatabaseReference table_category = database.getReference("Category");



        mCallbackManager = CallbackManager.Factory.create();

        GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions
                .Builder()
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        mGoogleBtn.setOnClickListener(v -> SignInGoogle());


        mFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mFacebookBtn.setEnabled(false);

                LoginManager.getInstance().logInWithReadPermissions(SignIn.this, Arrays.asList("email", "public_profile"));
                LoginManager.getInstance().registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d(TAG, "Facebook:onSuccess:" + loginResult);
                        handleFacebookAccessToken(loginResult.getAccessToken());

                    }

                    @Override
                    public void onCancel() {
                        Log.d(TAG, "facebook:onCancel: ");
                        // App code

                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d(TAG, "facebook:nError", error);
                        // App code
                    }
                });

            }
        });

        btn_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user not exist in database
                        if (dataSnapshot.child(edtName.getText().toString()).exists()) {

                            // Get user information
                            mDialog.dismiss();
                            Users users = dataSnapshot.child(edtName.getText().toString()).getValue(Users.class);
                            users.setName(edtName.getText().toString());
//                            users.setPhone(edtPhone.getText().toString());
                            if (users.getPassword().equals(edtPassword.getText().toString()))
                            {
                                Toast.makeText(SignIn.this, "Sign in successfully !", Toast.LENGTH_SHORT).show();
                                Intent signIn = new Intent(SignIn.this, Home.class);
                                Common.currentUser = users;
                                startActivity(signIn);

                            } else {
                                Toast.makeText(SignIn.this, "Sign in failed!", Toast.LENGTH_SHORT).show();
                            }
                        }else
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }


    void SignInGoogle() {
        Intent signIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signIntent, GOOGLE_SIGN);
    }

    @Override
    public void onStart(){
        super.onStart();
        // Check if user is signed (non-null) and update UI accordingly.
        FirebaseUser currentUser= mAuth.getCurrentUser();

        if (currentUser != null){

            updateUI();

        }

    }

    private void updateUI(){

        Toast.makeText(this, "You're logged in", Toast.LENGTH_SHORT).show();

        Intent accountIntent = new Intent(SignIn.this, Home.class);
        startActivity(accountIntent);
        finish();



    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);


        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN) {
            Task<GoogleSignInAccount> task = GoogleSignIn
                    .getSignedInAccountFromIntent(data);

            try {

                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) firebaseAuthWithGoogle(account);

            } catch (ApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle: " + account.getId());

        AuthCredential credential = GoogleAuthProvider
                .getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithCredential:success");

                        FirebaseUser User = mAuth.getCurrentUser();
                        updateUI();

                        mGoogleBtn.setEnabled(true);

                    } else
                    {
                        Log.w(TAG, "signin failure", task.getException());
                        Toast.makeText(SignIn.this, "Authentication failed. ",
                                Toast.LENGTH_SHORT).show();

                        mGoogleBtn.setEnabled(true);
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token){
        Log.d(TAG, "handleFacebookAccessToken:" + token );

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete( Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            mFacebookBtn.setEnabled(true);

                            updateUI();
                        }else {
                            // If sign in fails, display a message to the user
                            Log.w(TAG,"signInWithCredential:failure", task.getException());
                            Toast.makeText(SignIn.this, "Authentication failed. ",
                                    Toast.LENGTH_SHORT).show();

                            mFacebookBtn.setEnabled(true);
                        }
                    }
                });
    }


}
