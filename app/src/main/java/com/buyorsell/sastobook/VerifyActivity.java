package com.buyorsell.sastobook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.security.identity.IdentityCredentialStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class VerifyActivity extends AppCompatActivity {

    TextView verifyMsg;
    Button verifyBtn;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);

        verifyMsg = findViewById(R.id.verifyText);
        verifyBtn = findViewById(R.id.verifyEmailBtn);
        auth = FirebaseAuth.getInstance();

        verifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //send verificatioin email
                auth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void v) {
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(VerifyActivity.this, "Verification Email is sent", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}