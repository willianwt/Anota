package com.williantaiguara.anota.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Lembrete;

import java.util.List;

public class AdapterListaResumos extends RecyclerView.Adapter<AdapterListaResumos.MyViewHolder> {
    private List<Lembrete> resumoList;
    private Context context;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private String emailUsuario = autenticacao.getCurrentUser().getEmail();
    private String idUsuario = Base64Custom.CodificarBase64(emailUsuario);


    public AdapterListaResumos(List<Lembrete> lembretes, Context c) {
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
        try {
            holder.imagem.setImageURI(null);
            holder.titulo.setText(resumo.getTitulo());
            holder.data.setText(DateCustom.formataData(resumo.getData()));
            String msg = resumo.getConteudo();
            String imagem = null;
            if (resumo.getImagem() != null) {
                imagem = resumo.getImagem().trim();
            }

            if (imagem != null) {

                Glide.with(context)
                        .load(resumo.getImagem())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .override(900)
                        .centerCrop()
                        .into(holder.imagem);

//
                holder.resumo.setVisibility(View.GONE);

            } else {
                if (resumo.getConteudo() != null && resumo.getConteudo().length() > 50) {
                    String tamanhoResumo = resumo.getConteudo();
                    tamanhoResumo = tamanhoResumo.substring(0, 250) + "...";
                    holder.resumo.setText(tamanhoResumo);
                } else {
                    holder.resumo.setText(resumo.getConteudo());
                }
                holder.imagem.setVisibility(View.GONE);
            }
        }catch (Exception e){
            e.printStackTrace();
            Log.i("erro", e.getMessage());
        }
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return resumoList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView titulo, resumo, data;
        ImageView imagem;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            titulo = itemView.findViewById(R.id.adapterTituloListaLembretes);
            data = itemView.findViewById(R.id.adapterDataListaLembretes);
            resumo = itemView.findViewById(R.id.adapterResumoListaLembretes);
            imagem = itemView.findViewById(R.id.adapterImageResumo);
        }
    }
}
