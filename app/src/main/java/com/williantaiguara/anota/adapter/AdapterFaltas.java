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
import com.williantaiguara.anota.model.Falta;

import java.util.List;

public class AdapterFaltas extends RecyclerView.Adapter<AdapterFaltas.MyViewHolder> {

    private List<Falta> faltaList;
    private Context context;

    public AdapterFaltas(List<Falta> faltas, Context c){
        this.faltaList = faltas;
        this.context = c;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemFalta = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_faltas, parent, false);
        return new MyViewHolder(itemFalta);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Falta falta = faltaList.get(position);
        holder.qtdFalta.setText(falta.getQtd());

        holder.dataFalta.setText(DateCustom.formataData(falta.getData()));
    }

    @Override
    public int getItemCount() {
        return faltaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView dataFalta, qtdFalta;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            dataFalta = itemView.findViewById(R.id.txDataFalta);
            qtdFalta = itemView.findViewById(R.id.txQtdFalta);

        }
    }
}
