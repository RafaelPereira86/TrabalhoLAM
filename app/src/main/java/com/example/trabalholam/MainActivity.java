package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Context;
import android.content.Intent;

import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.Button;

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
    TextView number;
    TextView password;
    Button btn;
    TextView textView;

    RequestQueue queue;
    String token,myUC;
    Db_handler db;
    String num, pass;


    public static final String tokenA = "token";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_main);

        db = new Db_handler(this);
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
            String url = "https://alunos.upt.pt/~abilioc/dam.php?func=auth&login=" + num + "&password=" + pass;
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

            String check = db.checkUser(num,pass);
            if(!check.equals("")){
                Intent i = new Intent(this,MenuActivity.class);
                i.putExtra(tokenA,check);
                startActivity(i);
            }
        }
    }

    public boolean isConnected() {
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
        addAluno(new Aluno(number,password,token));
        addUcs();
        addHorario(number);
        adicNotas();
        addDisciplinas();
    }

    public void addAluno(Aluno a) {
        db.addAluno(a);
        Toast.makeText(MainActivity.this,"Aluno Adicionado",Toast.LENGTH_SHORT).show();
    }

    public void  addHorario(int numAl){
        queue = Volley.newRequestQueue(MainActivity.this);
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=horario&token=" + token;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array = response.getJSONArray("horario");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject object = array.getJSONObject(i);
                        getUc(object.getInt("codigoUC"), new ICallback() {
                            @Override
                            public void onSuccess(String uc) {
                                try {
                                    Horario horario = new Horario();
                                    db.addUc(new Disciplina(object.getInt("codigoUC"), uc));
                                    horario.setCodigoUC(object.getInt("codigoUC"));
                                    horario.setTipoAula(object.getString("tipoAula"));
                                    horario.setNumAluno(numAl);
                                    horario.setHoraInicio(object.getInt("horaInicio"));
                                    horario.setHoraFim(object.getInt("horaFim"));
                                    horario.setDiaSemana(object.getInt("diaSemana") - 2);
                                    db.addHorario(horario);

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
        queue.add(objectRequest);
    }


    public void addDisciplinas(){
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

    public void addUcs(){
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
                                    db.addUc(d);
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

    public void adicNotas(){
        queue = Volley.newRequestQueue(MainActivity.this);
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=classificacao&token=" + token;

        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("classificacao");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        getUc(object.getInt("uc"), new ICallback() {
                            @Override
                            public void onSuccess(String uc) {
                                try {
                                    db.addUc(new Disciplina(object.getInt("uc"), uc));
                                    Nota n = new Nota();
                                    n.setCodUc(object.getInt("uc"));
                                    n.setNota(object.getInt("nota"));
                                    n.setnAluno(db.checkToken(token));
                                    db.addNota(n);
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
        queue.add(objectRequest);
    }


    public void getUc(int uc, ICallback callback) {
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