package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


public class MenuActivity extends AppCompatActivity {

    String token;
    CardView c1, c2, c3, c4, c5, c6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);

        c1.setOnClickListener(this :: webMail);
    }

    private void webMail(View view) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.alunos.upt.pt/"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}