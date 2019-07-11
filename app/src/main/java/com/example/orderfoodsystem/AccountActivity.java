package com.example.orderfoodsystem;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public class AccountActivity extends AppCompatActivity {

    private Button mLogoutBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        mLogoutBtn = (Button) findViewById(R.id.logoutBtn);

        //Status Bar Color
        getWindow().setStatusBarColor(Color.WHITE);

        mAuth = FirebaseAuth.getInstance();

        mLogoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               mAuth.signOut();
               LoginManager.getInstance().logOut();
                Intent accountIntent = new Intent(AccountActivity.this, SignIn.class);
                startActivity(accountIntent);
                Toast.makeText(AccountActivity.this, "You're logged out", Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }

//    @Override
//    public void onStart(){
//        super.onStart();
//        // Check if user is signed (non-null) and update UI accordingly.
//        FirebaseUser currentUser= mAuth.getCurrentUser();
//
//        if (currentUser != null){
//
//            updateUI();
//
//        }
//
//    }

    private void updateUI() {

        Toast.makeText(AccountActivity.this, "You're logged out", Toast.LENGTH_SHORT).show();

        Intent accountIntent = new Intent(AccountActivity.this, SignIn.class);
        startActivity(accountIntent);
        finish();
    }
}
