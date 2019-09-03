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

public class AdapterLembretes extends RecyclerView.Adapter<AdapterLembretes.MyViewHolder> {

    private List<Lembrete> lembreteList;
    private Context context;

    public AdapterLembretes(List<Lembrete> lembretes, Context c){
        this.lembreteList = lembretes;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_lembretes_lista, parent, false);
        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Lembrete lembrete = lembreteList.get(position);

        holder.titulo.setText(lembrete.getTitulo());
        List<String> dataSeparada = null;
        if (dataSeparada != null){
            dataSeparada = Arrays.asList(lembrete.getData().split("-"));
            Collections.reverse(dataSeparada);
            String dataFormatada = TextUtils.join("/", dataSeparada);
            holder.data.setText(dataFormatada);
        }else{
            holder.data.setText("");
        }

        if(lembrete.getConteudo() != null && lembrete.getConteudo().length() > 50){
            String resumo = lembrete.getConteudo();
            resumo = resumo.substring(0, 50) + "...";
            holder.resumo.setText(resumo);
        }else {
            holder.resumo.setText(lembrete.getConteudo());
        }
    }

    @Override
    public int getItemCount() {
        return lembreteList.size();
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
