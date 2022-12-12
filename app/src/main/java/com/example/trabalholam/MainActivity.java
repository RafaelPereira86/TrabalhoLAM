package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
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
    EditText number;
    EditText password;
    Button btn;
    TextView textView;

    RequestQueue queue;
    String token,myUC;
    Db_handler db;

    ConstraintLayout c;

    public static final String tokenA = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        number = findViewById(R.id.editTextemail);
        password = findViewById(R.id.editTextpass);
        textView = findViewById(R.id.textView2);
        btn = findViewById(R.id.btnlogin);

        btn.setOnClickListener(this::login);
    }

    public void login(View view) {
        if (isConnected()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://alunos.upt.pt/~abilioc/dam.php?func=auth&login=" + number.getText().toString() + "&password=" + password.getText().toString();
            queue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            token = response;
                            String check = db.authCheck(numero,password);
                            if(check.equalsIgnoreCase("")){
                                addToDataBase(Interger.parseInt(numero),password,token);
                            }
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            intent.putExtra(tokenA,check);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"Numero ou palavra-passe incorreta",Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(sr);
        }else{
            String check = db.authCheck(numero,password);
            if(check != null){
                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                intent.putExtra(tokenA,check);
                startActivity(intent);
            }
        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if(capabilities != null){
                if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)){
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                    return true;
                }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT__ETHERNET");
                    return true;
                }else if(capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                    return true;

                }
            }
        }
        return false;
    }

    public void addToDataBase(int number, String password, String token){

    }
}