package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {
    EditText email;
    EditText password;
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        email = findViewById(R.id.editTextemail);
        password = findViewById(R.id.editTextpass);
        textView = findViewById(R.id.textView2);
        btn = findViewById(R.id.btnlogin);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this,"Email e Password em falta!",Toast.LENGTH_LONG).show();
                }else{
                    
                }
            }
        });
    }

    public void login(View view) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://alunos.upt.pt/~abilioc/dam.php?func=auth&login=" + email.getText().toString() +"&password=" + password.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        textView.setText("Response is: " + response);

                        Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                        startActivity(intent);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });
    }
}