package com.example.trabalholam;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TimeTable extends AppCompatActivity {
    Intent i;
    String token;

    List<ArrayList<Schedule>> disc;
    TimetableView timeTable;
    RequestQueue queue;
    Db_handler db;
    ConstraintLayout cl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);
        inicializar();
        cl = findViewById(R.id.finishBtnTimeTable);
        cl.setOnClickListener(this::onClick);

        if (isConnected()) {
            getHorario();
        } else {
            getHorarioBd();
        }
    }
    public void onClick(View v)
    {
        finish();
    }

    private  void inicializar(){
        disc = new ArrayList<>();
        timeTable = findViewById(R.id.myTimeTable);
        i = getIntent();
        token = i.getStringExtra(MenuActivity.tokenM);
        db = new Db_handler(this.getApplicationContext());
    }

    public  void getHorario(){
        queue = Volley.newRequestQueue(TimeTable.this);
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=horario&token=" + token;
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, myUrl, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray myA = response.getJSONArray("horario");
                    Toast.makeText(TimeTable.this, "Horario", Toast.LENGTH_LONG).show();
                    for (int i = 0; i < myA.length(); i++) {
                        JSONObject object = myA.getJSONObject(i);
                        getDisc(object.getInt("codigoUC"), new ICallback() {

                            @Override
                            public void onSuccess(String uc) {
                                try {
                                    if (disc.size() == 0) {
                                        ArrayList<Schedule> s = new ArrayList<>();
                                        disc.add(s);
                                        disc.get(0).add(getScheduleWeb(uc, object.getString("tipoAula"), new Time(object.getInt("horaInicio"), 0), new Time(object.getInt("horaFim"), 0), object.getInt("diaSemana") - 2));
                                    } else {
                                        Boolean hasFound = false;
                                        for (int j = 0; j < disc.size(); j++) {
                                            if (uc.equals(disc.get(j).get(0).getClassTitle())) {
                                                disc.get(j).add(getScheduleWeb(uc, object.getString("tipoAula"), new Time(object.getInt("horaInicio"), 0), new Time(object.getInt("horaFim"), 0), object.getInt("diaSemana") - 2));
                                                hasFound = true;
                                            }
                                        }
                                        if (!hasFound) {
                                            ArrayList<Schedule> l = new ArrayList<>();
                                            disc.add(l);
                                            disc.get(disc.size() - 1).add(getScheduleWeb(uc, object.getString("tipoAula"), new Time(object.getInt("horaInicio"), 0), new Time(object.getInt("horaFim"), 0), object.getInt("diaSemana") - 2));
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                iterateDisc();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TimeTable.this, "Erro" , Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(objectRequest);
    }

    public void getDisc(int uc, final ICallback callback) {
        String myUrl = "https://alunos.upt.pt/~abilioc/dam.php?func=uc&codigo=" + uc;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, myUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(TimeTable.this, "Executado", Toast.LENGTH_SHORT).show();
                callback.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TimeTable.this, "Erro", Toast.LENGTH_SHORT).show();

            }
    });
        queue.add(stringRequest);
    }

    public void getHorarioBd() {
        token = token.replace("\n", "");
        ArrayList<Horario> horarios = db.getHorAl(token);
        for (Horario h : horarios) {
            Schedule s = getScheduleDb(h);
            if (disc.size() == 0) {
                ArrayList<Schedule> sc = new ArrayList<>();
                disc.add(sc);
                disc.get(0).add(s);
            } else {
                Boolean hasFound = false;
                for (int i = 0; i < disc.size(); i++) {
                    if (s.getClassTitle().equals(disc.get(i).get(0).getClassTitle())) {
                        disc.get(i).add(s);
                        hasFound = true;
                    }
                }
                if (!hasFound) {
                    ArrayList<Schedule> l = new ArrayList<>();
                    disc.add(l);
                    disc.get(disc.size() - 1).add(s);
                }
            }
        }
        iterateDisc();
    }

    public void iterateDisc() {
        for (int j = 0; j < disc.size(); j++) {
            timeTable.add(disc.get(j));
        }
    }
    public Schedule getScheduleDb(Horario h) {
        Schedule s = new Schedule();
        s.setClassTitle(db.getUc(h.getCodigoUC()).getUc());
        s.setProfessorName(h.getTipoAula());
        s.setStartTime(new Time(h.getHoraInicio(), 0));
        s.setEndTime(new Time(h.getHoraFim(), 0));
        s.setDay(h.getDiaSemana());
        return s;
    }

    public Schedule getScheduleWeb(String uc, String tipo, Time tStart, Time tEnd, int day) {
        Schedule s = new Schedule();
        s.setClassTitle(uc);
        s.setProfessorName(tipo);
        s.setStartTime(tStart);
        s.setEndTime(tEnd);
        s.setDay(day);
        return s;
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
