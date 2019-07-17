package com.example.orderfoodsystem;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
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

import com.firebase.ui.auth.data.model.User;
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

import io.paperdb.Paper;


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
    DatabaseReference table_user;

    EditText edtName, edtPassword;

    TextView txtForgotPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        btn_sign =  findViewById(R.id.btn_sign);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtName = (EditText) findViewById(R.id.edtName);
//        edtPhone = findViewById(R.id.edtPhone);
        txtForgotPwd = findViewById(R.id.txtForgotPwd);

        txtForgotPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showForgotPwdDialog();
            }
        });



        //Init Paper
        Paper.init(this);

        link_regist =(TextView) findViewById(R.id.link_regist);

        database = FirebaseDatabase.getInstance();
        category = database.getReference("Category");
        table_user = database.getReference("User");



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

                //Save user & password



                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("Please waiting...");
                mDialog.show();

                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        // Check if user not exist in database
                        if (dataSnapshot.child(edtName.getText().toString()).exists()) {

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

    private void showForgotPwdDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password");
        builder.setMessage("Enter you code");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout,null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        EditText edtName = forgot_view.findViewById(R.id.edtName);
        EditText edtSecureCode = forgot_view.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table_user.addValueEventListener(new ValueEventListener(){

                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        Users user = dataSnapshot.child(edtName.getText().toString())
                                .getValue(Users.class);

                        if (user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(SignIn.this, "You Password : " + user.getPassword(), Toast.LENGTH_SHORT).show();
                        else {
                            Toast.makeText(SignIn.this, "Wrong Code", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();
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
