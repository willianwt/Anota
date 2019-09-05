package com.williantaiguara.anota.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.model.Disciplina;

import java.util.List;

public class AdapterCursos extends RecyclerView.Adapter<AdapterCursos.MyViewHolder> {

    private List<Disciplina> cursoList;
    private Context context;

    public AdapterCursos(List<Disciplina> cursos, Context c){
        this.cursoList = cursos;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemCurso = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_cursos, parent, false);

        return new MyViewHolder(itemCurso);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Disciplina curso = cursoList.get(position);

        holder.nomeCurso.setText(curso.getKey());

    }

    @Override
    public int getItemCount() {
        return cursoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeCurso;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            nomeCurso = itemView.findViewById(R.id.txNomeDoCurso);
        }
    }
}
