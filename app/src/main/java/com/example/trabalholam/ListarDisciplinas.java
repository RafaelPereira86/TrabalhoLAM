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
import com.example.trabalholam.Adapters.AdapterDisciplinas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class ListarDisciplinas extends AppCompatActivity {

    Db_handler dbHandler;
    RequestQueue queue;
    Intent i;
    String token;
    AdapterDisciplinas myadapter;
    RecyclerView recyclerViewDisciplinas;
    ArrayList<String> listarDisciplinas;
    LinearLayoutManager layoutManager;
    ConstraintLayout cl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindisciplinas);
        listarDisciplinas = new ArrayList<>();

        i = getIntent();
        token = i.getStringExtra(MenuActivity.tokenM);
        dbHandler = new Db_handler(this);

        if (isConnected()) {
            getDisciplinas();
        } else {
            getDisciplinasBD();
        }

        myadapter = new AdapterDisciplinas(listarDisciplinas);
        recyclerViewDisciplinas = findViewById(R.id.recyclerViewDisciplinas);
        recyclerViewDisciplinas.setAdapter(myadapter);

        layoutManager = new LinearLayoutManager(this);
        recyclerViewDisciplinas.setLayoutManager(layoutManager);

        cl = findViewById(R.id.finishBtnListarDisciplinas);
        cl.setOnClickListener(this::onClick);
    }

    public void onClick(View v)
    {

    }

    public void getDisciplinas() {
        queue = Volley.newRequestQueue(ListarDisciplinas.this);
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=horario&token=" + token;
        JsonObjectRequest jObeject = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray disciplinas = response.getJSONArray("horario");
                    for (int i = 0; i < disciplinas.length(); i++) {
                        JSONObject ucObject = disciplinas.getJSONObject(i);
                        getUc(ucObject.getInt("codigoUC"), new ICallback() {
                            @Override
                            public void onSuccess(String uc) {
                                if(listarDisciplinas.contains(uc)) {
                                } else {
                                    listarDisciplinas.add(uc);
                                }
                                myadapter.notifyDataSetChanged();
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
                Toast.makeText(ListarDisciplinas.this, "Failed to get Response", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jObeject);
    }

    public void getDisciplinasBD() {
        listarDisciplinas = (dbHandler.getInscr(token));
    }

    public void getUc(int uc, final ICallback callback) {
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=uc&codigo=" + uc;
        Log.d("ver", "getUc: " + myUrl);
        //Background work here
        StringRequest sr = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(ListarDisciplinas.this, "done", Toast.LENGTH_SHORT).show();
                callback.onSuccess(response.replace("\n", ""));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(ListarDisciplinas.this, "Erro" + error, Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(sr);
    }

    public boolean isConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkCapabilities capabilities = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
            }
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
