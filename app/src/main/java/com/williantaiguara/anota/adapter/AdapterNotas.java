package com.williantaiguara.anota.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Nota;

import java.util.List;

public class AdapterNotas extends RecyclerView.Adapter<AdapterNotas.MyViewHolder>  {

    private List<Nota> notaList;
    private Context context;

    public AdapterNotas(List<Nota> notas, Context c){
        this.notaList = notas;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemNota = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.adapter_notas, parent, false);

        return new MyViewHolder(itemNota);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Nota nota = notaList.get(position);
        holder.nomeAtividade.setText(nota.getTitulo());
        holder.dataAtividade.setText(DateCustom.formataData(nota.getData()));
        holder.notaAtividade.setText(String.valueOf(nota.getNota()));
    }

    @Override
    public int getItemCount() {
        return notaList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nomeAtividade, dataAtividade, notaAtividade;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nomeAtividade = itemView.findViewById(R.id.txAdapterNotaNome);
            dataAtividade = itemView.findViewById(R.id.txAdapterNotaData);
            notaAtividade = itemView.findViewById(R.id.txAdapterNota);
        }
    }
}
