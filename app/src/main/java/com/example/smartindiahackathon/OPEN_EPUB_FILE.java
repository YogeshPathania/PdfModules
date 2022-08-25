package com.example.smartindiahackathon;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

public class OPEN_EPUB_FILE extends AppCompatActivity {
    View view;
    BottomSheetDialog sheetDialog2;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_epub_file);
        view = findViewById(R.id.plus_1);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView =findViewById(R.id.navigationView);
        toolbar =findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view.setOnClickListener(v -> {
            sheetDialog2=new BottomSheetDialog(OPEN_EPUB_FILE.this,R.style.BottomSheetStyle);
            View view = LayoutInflater.from(OPEN_EPUB_FILE.this).inflate(R.layout.bottomsheet_dialog_epub,
                    (LinearLayout)findViewById(R.id.sheet));
            sheetDialog2.setContentView(view);

            sheetDialog2.show();

        });
    }
//    @Override
//    public void onBackPressed() {
//        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
//            drawerLayout.closeDrawer(GravityCompat.START);
//        }else {
//            super.onBackPressed();
//        }
//    }
}