package com.example.trabalholam;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class View_HolderDisciplinas extends RecyclerView.ViewHolder{
    public TextView disciplinaTextView;

    public View_HolderDisciplinas (@NonNull View itemView){
        super(itemView);
        disciplinaTextView = itemView.findViewById(R.id.textViewDisciplinas);
    }

}
