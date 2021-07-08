package com.buyorsell.sastobook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class PolicyActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);

        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_draw_open, R.string.navigation_draw_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.nav_policy);
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
                Intent intent2 = new Intent(PolicyActivity.this, MainActivity.class);
                startActivity(intent2);
                break;

           /* case R.id.nav_addBook:
                Intent intent2 = new Intent(MainActivity.this, AddBookActivity.class);
                startActivity(intent2);
                break;*/

           /* case R.id.nav_mybook:
                Intent intent5 = new Intent(MainActivity.this, MyBookActivity.class);
                startActivity(intent5);
                break;*/

          /*  case R.id.nav_message:
                Intent intent6 = new Intent(MainActivity.this, MessageActivity.class);
                startActivity(intent6);
                break;*/


            case R.id.nav_profile:
                Intent intent3 = new Intent(PolicyActivity.this, UserProfileActivity.class);
                startActivity(intent3);
                break;

            case R.id.nav_reset:
                Intent intent8 = new Intent(PolicyActivity.this, ResetPasswordActivity.class);
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
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}