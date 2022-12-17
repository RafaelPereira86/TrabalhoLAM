package com.example.trabalholam;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class View_HolderNotas extends RecyclerView.ViewHolder {
    public TextView textViewDisciplina;
    public TextView textViewNota;

    public View_HolderNotas(@NonNull View itemView) {
        super(itemView);
        textViewDisciplina = itemView.findViewById(R.id.textViewDisciplinas);
        textViewNota = itemView.findViewById(R.id.textViewNotas);
    }
}

