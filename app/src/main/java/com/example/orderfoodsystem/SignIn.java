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


    private ImageButton btn_sign;

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
    }



}
