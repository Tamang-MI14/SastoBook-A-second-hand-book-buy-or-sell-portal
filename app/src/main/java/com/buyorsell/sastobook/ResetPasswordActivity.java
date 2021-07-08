package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    EditText userPassword, userConfPassword;
    Button resetBtn;
    FirebaseUser user;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_reset);

        userPassword = findViewById(R.id.reset_password);
        userConfPassword = findViewById(R.id.reset_confirm_password);

        user = FirebaseAuth.getInstance().getCurrentUser();

        resetBtn = findViewById(R.id.reset_btn);


        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userPassword.getText().toString().isEmpty()) {
                    userPassword.setError("Required Field");
                    return;
                }

                if (userConfPassword.getText().toString().isEmpty()) {
                    userConfPassword.setError("Required Field");
                    return;
                }

                if (!userPassword.getText().toString().equals(userConfPassword.getText().toString())){
                    userConfPassword.setError("password do not match");
                    return;
                }
                user.updatePassword(userPassword.getText().toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ResetPasswordActivity.this,"Password Updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ResetPasswordActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                Intent intent9 = new Intent(ResetPasswordActivity.this, MainActivity.class);
                startActivity(intent9);
                break;

            case R.id.nav_addBook:
                Intent intent2 = new Intent(ResetPasswordActivity.this, AddBookActivity.class);
                startActivity(intent2);
                break;

            case R.id.nav_mybook:
                Intent intent5 = new Intent(ResetPasswordActivity.this, MyBookActivity.class);
                startActivity(intent5);
                break;

            case R.id.nav_message:
                Intent intent6 = new Intent(ResetPasswordActivity.this, MessageActivity.class);
                startActivity(intent6);
                break;


            case R.id.nav_profile:
                Intent intent3 = new Intent(ResetPasswordActivity.this, UserProfileActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_reset:
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, LoginActivity.class));
                break;


            case R.id.nav_share:
                Toast.makeText(this, "Shared Successfully!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_policy:
                Intent intent7 = new Intent(ResetPasswordActivity.this, PolicyActivity.class);
                startActivity(intent7);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}