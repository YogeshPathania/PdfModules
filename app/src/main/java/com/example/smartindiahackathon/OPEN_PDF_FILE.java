package com.example.smartindiahackathon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;

import java.io.IOException;

public class OPEN_PDF_FILE extends AppCompatActivity {

    View view;
    BottomSheetDialog sheetDialog;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    public TextToSpeech textToSpeech;
    public TextView outputTextView;
    public static final int READ_REQUEST_CODE = 42;
    public static final String PRIMARY = "primary";
    public static final String LOCAL_STORAGE = "/storage/self/primary/";
    public static final String EXT_STORAGE = "/storage/7764-A034/";
    public static final String COLON = ":";
    String epub_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.open_pdf_file);

        view = findViewById(R.id.plus_1);
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        toolbar = findViewById(R.id.toolbar);
         epub_location = this.getIntent().getExtras().getString("pdf_location");
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_open, R.string.navigation_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        view.setOnClickListener(v -> {
            sheetDialog = new BottomSheetDialog(OPEN_PDF_FILE.this, R.style.BottomSheetStyle);
            View view = LayoutInflater.from(OPEN_PDF_FILE.this).inflate(R.layout.bottomsheet_dialog,
                    (LinearLayout) findViewById(R.id.sheet));
            sheetDialog.setContentView(view);
            sheetDialog.findViewById(R.id.btnSpeech).setOnClickListener(view1 -> {
             ///   readPdfFile(Uri.parse(epub_location));

            });

            sheetDialog.show();

        });

    }
    public void readPdfFile(Uri uri) {
        String fullPath;
        //convert from uri to full path
        if(uri.getPath().contains(PRIMARY)) {
            fullPath = LOCAL_STORAGE + uri.getPath().split(COLON)[1];
        }
        else {
            fullPath = EXT_STORAGE + uri.getPath().split(COLON)[1];
        }
        Log.v("URI", uri.getPath()+" "+fullPath);
        String stringParser;
        try {
            PdfReader pdfReader = new PdfReader(fullPath);

            try {
                String parsedText="";
                PdfReader reader = new PdfReader(pdfReader);
                int n = reader.getNumberOfPages();
                for (int i = 0; i <n ; i++) {
                    parsedText   = parsedText+PdfTextExtractor.getTextFromPage(reader, i+1).trim()+"\n"; //Extracting the content from the different pages
                }
                outputTextView.setText(parsedText);
                // textToSpeech.speak(parsedText, TextToSpeech.QUEUE_FLUSH,null, null);
           //     speak(parsedText);
                reader.close();
            } catch (Exception e) {
                System.out.println(e);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    void speak(String s){
//        if (s.isEmpty()){
//            Toast.makeText(this,"Pdf data not found",Toast.LENGTH_SHORT).show();
//        }else {
//            Intent I = new Intent(this, PdfTextToSpeechActivity.class);
//            I.putExtra("epub_location",s);
//            startActivity(I);
//        }
//    }

}



