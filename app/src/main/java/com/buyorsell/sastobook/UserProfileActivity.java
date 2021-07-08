package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.buyorsell.sastobook.model.UserHelperClass;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

public class UserProfileActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    TextView topUsername, topEmail, username, email, address;

    TextView editUserProfile, editUserProduct;

    EditText Uname, Uaddress;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

       /* topUsername = findViewById(R.id.labelUsername);
        topEmail = findViewById(R.id.labelEmailaddress);*/
        username = findViewById(R.id.profileUsername);
        email = findViewById(R.id.profileEmail);
        address = findViewById(R.id.profileAddress);
        editUserProfile = findViewById(R.id.gotoUpdateProfile);
        editUserProduct = findViewById(R.id.gotoMyBook);

        Uname = findViewById(R.id.Uname);
        Uaddress = findViewById(R.id.Uaddress);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);


        setSupportActionBar(toolbar);



        editUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), UpdateProfileActivity.class));

                /*BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(UserProfileActivity.this, R.style.BottomSheetDialogTheme);
                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet_profile,(LinearLayout)findViewById(R.id.bottomSheet));

                databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                        assert userHelperClass != null;
                *//*topUsername.setText(userHelperClass.getUsername());
                topEmail.setText(userHelperClass.getEmail());*//*
                        Uname.setText(userHelperClass.getUsername());
                        Uaddress.setText(userHelperClass.getAddress());
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(UserProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();*/

            }
        });

        editUserProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MyBookActivity.class));
            }
        });

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_profile);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserHelperClass userHelperClass = snapshot.getValue(UserHelperClass.class);
                assert userHelperClass != null;
                /*topUsername.setText(userHelperClass.getUsername());
                topEmail.setText(userHelperClass.getEmail());*/
                username.setText(userHelperClass.getUsername());
                email.setText(userHelperClass.getEmail());
                address.setText(userHelperClass.getAddress());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                Intent intent2 = new Intent(UserProfileActivity.this, MainActivity.class);
                startActivity(intent2);
                break;

           case R.id.nav_addBook:
                Intent intent1 = new Intent(UserProfileActivity.this, AddBookActivity.class);
                startActivity(intent1);
                break;

           case R.id.nav_mybook:
                Intent intent5 = new Intent(UserProfileActivity.this, MyBookActivity.class);
                startActivity(intent5);
                break;

            case R.id.nav_message:
                Intent intent6 = new Intent(UserProfileActivity.this, MessageActivity.class);
                startActivity(intent6);
                break;


            case R.id.nav_profile:
                break;

            case R.id.nav_reset:
                Intent intent8 = new Intent(UserProfileActivity.this, ResetPasswordActivity.class);
                startActivity(intent8);
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
                Intent intent7 = new Intent(UserProfileActivity.this, PolicyActivity.class);
                startActivity(intent7);
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;

    }
}
