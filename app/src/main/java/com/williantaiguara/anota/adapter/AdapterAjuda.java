package com.williantaiguara.anota.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.model.Ajuda;

import java.util.List;

public class AdapterAjuda extends RecyclerView.Adapter<AdapterAjuda.MyViewHolder> {

    private List<Ajuda> ajudaList;

    public AdapterAjuda(List<Ajuda> list){
        this.ajudaList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemAjuda = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_ajuda, parent, false);
        return new MyViewHolder(itemAjuda);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Ajuda item = ajudaList.get(position);

        holder.bind(item);

        holder.itemView.setOnClickListener(v -> {
            boolean expandido = item.isExpandido();
            item.setExpandido(!expandido);
            notifyItemChanged(position);
        });


    }

    @Override
    public int getItemCount() {
        return ajudaList == null ? 0 : ajudaList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView pergunta;
        private TextView resposta;
        private View subItem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pergunta = itemView.findViewById(R.id.pergunta);
            resposta = itemView.findViewById(R.id.resposta);
            subItem = itemView.findViewById(R.id.subitem);
        }

        public void bind(Ajuda item){
            boolean expanded = item.isExpandido();

            subItem.setVisibility(expanded ? View.VISIBLE : View.GONE);

            pergunta.setText(item.getPergunta());
            resposta.setText(item.getResposta());
        }
    }
}
