package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;


public class MenuActivity extends AppCompatActivity {

    public static final String tokenM = "token";
    String token;
    CardView c1, c2, c3, c4, c5, c6;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        i = getIntent();
        token = i.getStringExtra(MainActivity.tokenA);

        c1 = findViewById(R.id.c1);
        c2 = findViewById(R.id.c2);
        c3 = findViewById(R.id.c3);
        c4 = findViewById(R.id.c4);
        c5 = findViewById(R.id.c5);
        c6 = findViewById(R.id.c6);

        c1.setOnClickListener(this :: disciplinas);
        c2.setOnClickListener(this :: notas);
        c3.setOnClickListener(this :: timeTable);
        c4.setOnClickListener(this :: siupt);
        c5.setOnClickListener(this :: webMail);
        c6.setOnClickListener(this :: voltar);
    }

    private void disciplinas(View view){
        Intent i = new Intent(this,ListarDisciplinas.class);
        i.putExtra(tokenM, token);
        startActivity(i);
    }

    private void notas(View view){
        Intent i = new Intent(this,ListarNotas.class);
        i.putExtra(tokenM, token);
        startActivity(i);
    }

    private void timeTable(View view){
        Intent i = new Intent(this,TimeTable.class);
        i.putExtra(tokenM, token);
        startActivity(i);
    }

    private void webMail(View view) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://mail.alunos.upt.pt/"));
            startActivity(i);

    }
    private void siupt(View view){
        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://siupt.upt.pt/"));
        startActivity(i);
    }
    private void voltar(View view){
        finish();
    }
}