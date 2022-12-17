package com.example.trabalholam.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trabalholam.R;
import com.example.trabalholam.View_HolderDisciplinas;

import java.util.ArrayList;


public class AdapterDisciplinas extends RecyclerView.Adapter<View_HolderDisciplinas>{

    ArrayList<String> disciplinas;
    View_HolderDisciplinas myVDis;

    public AdapterDisciplinas(ArrayList<String> listarDisciplinas) {
        disciplinas = listarDisciplinas;
    }

    @NonNull
    @Override
    public View_HolderDisciplinas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_disciplinas,parent,false);

        myVDis = new View_HolderDisciplinas(v);
        return myVDis;
    }

    @Override
    public void onBindViewHolder(@NonNull View_HolderDisciplinas holder, int position) {
        myVDis.discTextv.setText(disciplinas.get(position));
    }

    @Override
    public int getItemCount() {
        return disciplinas.size();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
}