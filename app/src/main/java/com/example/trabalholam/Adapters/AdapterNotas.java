package com.example.trabalholam.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalholam.NotasDisciplinas;
import com.example.trabalholam.R;
import com.example.trabalholam.View_HolderNotas;

import java.util.ArrayList;



public class AdapterNotas extends RecyclerView.Adapter<View_HolderNotas>{

    ArrayList<NotasDisciplinas> notas;
    View_HolderNotas myViewHolder;


    public AdapterNotas(ArrayList<NotasDisciplinas> listaNotas) {
        notas = listaNotas;
    }

    @NonNull
    @Override
    public View_HolderNotas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycle_view_notas, parent, false);

        myViewHolder = new View_HolderNotas(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull View_HolderNotas holder, int position) {
        Log.d("Testar Notas", "onBindViewHolder: "+ myViewHolder.textViewDisciplina);
        Log.d("notas1", "onBindViewHolder: " + holder.textViewDisciplina);
        Log.d("notas2", "onBindViewHolder: "+ notas.get(position).getUC());
        myViewHolder.textViewDisciplina.setText(notas.get(position).getUC());
        Log.d("notas3", "onBindViewHolder: "+ myViewHolder.textViewNota);
        Log.d("notas4", "onBindViewHolder: " + holder.textViewNota);
        Log.d("notas5", "onBindViewHolder: "+ notas.get(position).getNota());
        myViewHolder.textViewNota.setText(String.valueOf( notas.get(position).getNota()));

    }




    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return notas.size();
    }


}