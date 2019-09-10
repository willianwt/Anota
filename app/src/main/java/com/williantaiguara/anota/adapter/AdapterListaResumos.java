package com.williantaiguara.anota.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.model.Lembrete;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AdapterListaResumos extends RecyclerView.Adapter<AdapterListaResumos.MyViewHolder> {
    private List<Lembrete> resumoList;
    private Context context;

    public AdapterListaResumos(List<Lembrete> lembretes, Context c){
        this.resumoList = lembretes;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lembretes_lista, parent, false);
        return new AdapterListaResumos.MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Lembrete resumo = resumoList.get(position);


        holder.titulo.setText(resumo.getTitulo());
        holder.data.setText(resumo.getData());

        if(resumo.getConteudo() != null && resumo.getConteudo().length() > 50){
            String tamanhoResumo = resumo.getConteudo();
            tamanhoResumo = tamanhoResumo.substring(0, 50) + "...";
            holder.resumo.setText(tamanhoResumo);
        }else {
            holder.resumo.setText(resumo.getConteudo());
        }
    }


    @Override
    public int getItemCount() {
        return resumoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, resumo, data;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            titulo = itemView.findViewById(R.id.adapterTituloListaLembretes);
            data = itemView.findViewById(R.id.adapterDataListaLembretes);
            resumo = itemView.findViewById(R.id.adapterResumoListaLembretes);
        }
    }
}
