package com.example.trabalholam;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


public class AdapterDisciplinas extends RecyclerView.Adapter<View_HolderDisciplinas>{
    ArrayList<String> disciplinas;
    View_HolderDisciplinas myViewHolderDisciplina;

    public AdapterDisciplinas(ArrayList<String> ListaDisciplinas) {
        disciplinas = ListaDisciplinas;
    }

    @NonNull
    @Override
    public View_HolderDisciplinas onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_view_disciplinas, parent, false);

        myViewHolderDisciplina = new View_HolderDisciplinas(view);
        return myViewHolderDisciplina;
    }

    @Override
    public void onBindViewHolder(@NonNull View_HolderDisciplinas holder, int position) {
        myViewHolderDisciplina.disciplinaTextView.setText(disciplinas.get(position));
        myViewHolderDisciplina.disciplinaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
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
