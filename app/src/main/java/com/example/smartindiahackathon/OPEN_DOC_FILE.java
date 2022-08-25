package com.example.smartindiahackathon;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
//import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;

public class OPEN_DOC_FILE extends AppCompatActivity {

    View view;
    BottomSheetDialog sheetDialog1;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_doc_file);

        view = findViewById(R.id.plus_1);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView =findViewById(R.id.navigationView);
        toolbar =findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_open,R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view.setOnClickListener(v -> {
            sheetDialog1=new BottomSheetDialog(OPEN_DOC_FILE.this,R.style.BottomSheetStyle);
            View view = LayoutInflater.from(OPEN_DOC_FILE.this).inflate(R.layout.bottomsheet_dialog_doc,
                    (LinearLayout)findViewById(R.id.sheet));
            sheetDialog1.setContentView(view);

            sheetDialog1.show();
        });
    }
}