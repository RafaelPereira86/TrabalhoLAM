package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.media.browse.MediaBrowser;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    EditText number;
    EditText password;
    Button btn;
    TextView textView;

    RequestQueue queue;
    String token,myUC;
    Db_handler db;
    String num, pass;

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
        num = number.getText().toString();
        pass = password.getText().toString();
        if (isConnected()) {
            RequestQueue queue = Volley.newRequestQueue(this);
            String url = "https://alunos.upt.pt/~abilioc/dam.php?func=auth&login=" + num + "password=" + pass;
            queue = Volley.newRequestQueue(MainActivity.this);
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            token = response;
                            token = token.replace("\n","").trim();
                            if(token.equals("false")){
                                Toast.makeText(MainActivity.this,"Numero ou palavra-passe incorreta", Toast.LENGTH_SHORT).show();
                            }else{
                                String check = db.checkUser(num,pass);
                                check = check.replace("\n","");
                                if(check.equalsIgnoreCase(""))
                                {
                                    addToDataBase(Integer.parseInt(num),pass,token);
                                }
                                Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                                intent.putExtra(tokenA,token);
                                startActivity(intent);
                            }


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(MainActivity.this,"Numero ou palavra-passe incorreta",Toast.LENGTH_SHORT).show();
                }
            });
            queue.add(stringRequest);
        }else{
            Toast.makeText(MainActivity.this,"Sem ligação a internet",Toast.LENGTH_SHORT).show();

        }
    }

    private boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkCapabilities capabilities = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
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
        addAluno();
    }

    private void addAluno(Aluno a) {
        db.addAluno(a);
        Toast.makeText(MainActivity.this,"Aluno Adicionado",Toast.LENGTH_SHORT).show();
    }

    private void addInscricao(){
        String myUrl ="https://alunos.upt.pt/~abilioc/dam.php?func=uc_inscrito&token" + token;
        queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    JSONArray json = response.getJSONArray("inscrito");
                    for (int i = 0; i < json.length(); i++) {
                        JSONObject jSon = json.getJSONObject(i);
                        int myUc = jSon.getInt("uc");
                        db.addInscr(new InscricaoAl(myUc, Integer.parseInt(num)));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void addUcs(){
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=uc_inscrito&token" + token;
        queue = Volley.newRequestQueue(MainActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray uc = response.getJSONArray("inscrito");
                    for (int i = 0; i < uc.length(); i++) {
                        JSONObject jsonObjectUc = uc.getJSONObject(i);
                        getUc(jsonObjectUc.getInt("uc"), new ICallback() {

                            @Override
                            public void onSuccess(String uc) {
                                try {
                                    int codUc = jsonObjectUc.getInt("uc");
                                    Disciplina d = new Disciplina(codUc, uc);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonObjectRequest);
    }
    private  void adicHorario(){

    }

    private void adicNotas(){

    }

    private void getUc(int uc, ICallback callback) {
        String myUrl = "https://alunos.upt.pt/abilioc/dam.php?func=uc&codigo=" + uc;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                myUC = response;
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                myUC = null;

            }
        });
        queue.add(stringRequest);
    }
    public void finish(View view){
        finish();
    }
}