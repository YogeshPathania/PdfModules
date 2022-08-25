package com.example.smartindiahackathon;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
//import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
//import android.widget.Toast;
//import android.widget.TextClock;
//import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.smartindiahackathon.login.emailLogin.LoginWithEmailActivity;
import com.example.smartindiahackathon.login.emailLogin.RegisterActivity;
import com.example.smartindiahackathon.ui.epubReader.EpubReaderMainActivity;
import com.google.android.material.navigation.NavigationView;

public class Homepage extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    View view;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homepage);


        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
        view = findViewById(R.id.access_1);
        setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        View view1 = findViewById(R.id.rectangle_1);
        view1.setOnClickListener(view -> {
            Intent intent = new Intent(Homepage.this, Convertor_Layout.class);
            startActivity(intent);
        });
        View view2 = findViewById(R.id.rectangle_2);
        view2.setOnClickListener(view -> {
            Intent intent = new Intent(Homepage.this, OPEN_PDF_FILE.class);
            startActivity(intent);
        });
        View view3 = findViewById(R.id.rectangle_4);
        view3.setOnClickListener(view -> {
            Intent intent = new Intent(Homepage.this, OPEN_DOC_FILE.class);
            startActivity(intent);
        });
        View view4 = findViewById(R.id.rectangle_5);
        view4.setOnClickListener(view -> {
            Intent intent = new Intent(Homepage.this, EpubReaderMainActivity.class);
            startActivity(intent);
        });

    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected( MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.Sign_Up: {
                Intent intent = new Intent(Homepage.this, RegisterActivity.class);
                startActivity(intent);
                break;
            }
            case R.id.Login: {
                Intent intent = new Intent(Homepage.this, LoginWithEmailActivity.class);
                startActivity(intent);
                break;
            }
        }
        //close navigation drawer
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}