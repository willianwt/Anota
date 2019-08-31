package com.williantaiguara.anota.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.model.Disciplina;

import java.util.List;

public class AdapterAdicionarDisciplinas extends RecyclerView.Adapter<AdapterAdicionarDisciplinas.MyViewHolder> {
    private List<Disciplina> listaDisciplinas;

    public AdapterAdicionarDisciplinas(List<Disciplina> lista){
        this.listaDisciplinas = lista;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemLista = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.adapeter_adicionar_curso_lista,
                parent, false
        );


        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Disciplina disciplina = listaDisciplinas.get(position);
        holder.nomeDisciplina.setText(disciplina.getNomeDisciplina());
        holder.nomeProfessorDisciplina.setText(disciplina.getNomeProfessorDisciplina());
        holder.emailProfessorDisciplina.setText(disciplina.getEmailProfessorDisciplina());

    }

    @Override
    public int getItemCount() {
        return listaDisciplinas.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nomeDisciplina;
        TextView nomeProfessorDisciplina;
        TextView emailProfessorDisciplina;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeDisciplina = itemView.findViewById(R.id.adapterNomeDisciplinaAdd);
            nomeProfessorDisciplina = itemView.findViewById(R.id.adapterNomeProfessorDisciplinaAdd4);
            emailProfessorDisciplina = itemView.findViewById(R.id.adapterEmailProfessorDisciplinaAdd4);
        }
    }



}
