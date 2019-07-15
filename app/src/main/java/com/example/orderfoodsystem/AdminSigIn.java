package com.example.orderfoodsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AdminSigIn extends AppCompatActivity {

    ImageButton btnSigInA;
    ImageView imageViewA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_sig_in);

        btnSigInA = findViewById(R.id.btn_signA);
        imageViewA = findViewById(R.id.imageViewA);
    }
}
