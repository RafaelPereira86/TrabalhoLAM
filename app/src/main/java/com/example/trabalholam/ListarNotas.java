package com.example.trabalholam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.trabalholam.Adapters.AdapterNotas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ListarNotas extends AppCompatActivity {
    Db_handler dbHandler;
    RequestQueue queue;
    Intent i;
    String token;
    ArrayList<NotasDisciplinas> ListaNotas;
    ArrayList<Nota> Notas;
    ArrayList<Disciplina> ListaDisciplinas;
    AdapterNotas myadapter;
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;
    ConstraintLayout cl;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainnotas);
        ListaNotas = new ArrayList<>();
        ListaDisciplinas = new ArrayList<>();

        i = getIntent();
        token = i.getStringExtra(MenuActivity.tokenM);
        dbHandler = new Db_handler(this);

        cl = findViewById(R.id.finishBtnListarNotas);
        cl.setOnClickListener(this::onClick);

        if (isConnected()){
            getNotas();

        }
        else{
            getNotasBd();

        }

        myadapter = new AdapterNotas(ListaNotas);
        recyclerView = findViewById(R.id.recyclerViewNotas);
        recyclerView.setAdapter(myadapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }
    public void onClick(View view)
    {

    }

    public void getNotas(){
        queue = Volley.newRequestQueue(ListarNotas.this);
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=classificacao&token=" + token;
        JsonObjectRequest jObeject = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray ucs = response.getJSONArray("classificacao");
                    for (int i = 0; i < ucs.length(); i++) {
                        JSONObject ucObject = ucs.getJSONObject(i);
                        getUc(ucObject.getInt("uc"), new ICallback() {
                            @Override
                            public void onSuccess(String uc) {
                                try {
                                    int ucNota = ucObject.getInt("nota");
                                    ListaNotas.add(new NotasDisciplinas(uc, ucNota));
                                    myadapter.notifyDataSetChanged();
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
                Toast.makeText(ListarNotas.this, "Failed to get Response", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jObeject);
    }


    public void getNotasBd(){
        Notas = dbHandler.getNotas(token);
        for (int i = 0; i < Notas.size(); i++){
            int temp = Notas.get(i).getCodUc();
            ListaDisciplinas.add(dbHandler.getUc(temp));
        }
        for (int i = 0; i < Notas.size(); i++){
            ListaNotas.add(new NotasDisciplinas(ListaDisciplinas.get(i).getUc(), Notas.get(i).getNota()));
        }
    }

    public void getUc(int uc, final ICallback callback) {
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=uc&codigo=" + uc;
        Log.d("coisa", "getUc: " + myUrl);
        //Background work here
        StringRequest sr = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ListarNotas.this, "done", Toast.LENGTH_SHORT).show();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarNotas.this, "Erro" + error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(sr);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR");
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI");
                    return true;
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET");
                    return true;
                }
            }
        }
        return false;
    }
}
