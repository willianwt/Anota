package com.williantaiguara.anota.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterLembretes;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Lembrete;
import com.williantaiguara.anota.helper.ProgressBarCustom;

import java.util.ArrayList;
import java.util.List;

public class LembretesActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListaLembretes;
    private AdapterLembretes adapterLembretes;
    private List<Lembrete> lembretes = new ArrayList<>();
    private Lembrete lembrete;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference lembretesRef;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private ProgressBar progressBar;

    private ValueEventListener valueEventListenerLembretes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lembretes);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarCurso);
        toolbar.setTitle("Lembretes");

        progressBar = findViewById(R.id.progressBarLembretes);
        setSupportActionBar(toolbar);

        //recycler view
        recyclerViewListaLembretes = findViewById(R.id.recyclerViewListaLembretes);
        ProgressBarCustom.openProgressBar(progressBar);

        //configurar adapter
        adapterLembretes = new AdapterLembretes(lembretes, this);
        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaLembretes.setLayoutManager(layoutManager);
        recyclerViewListaLembretes.setHasFixedSize(true);
        recyclerViewListaLembretes.setAdapter(adapterLembretes);
        recyclerViewListaLembretes.addItemDecoration(new DividerItemDecoration(recyclerViewListaLembretes.getContext(), DividerItemDecoration.VERTICAL));

        //evento de click recyclerview
            recyclerViewListaLembretes.addOnItemTouchListener(
                    new RecyclerItemClickListener(
                            getApplicationContext(),
                            recyclerViewListaLembretes,
                            new RecyclerItemClickListener.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    Lembrete lembrete = lembretes.get(position);
                                    Intent intent = new Intent(LembretesActivity.this, AtualizarLembreteActivity.class);
                                    intent.putExtra("objeto", lembrete);
                                    startActivity(intent);
                                }

                                @Override
                                public void onLongItemClick(View view, final int position) {

                                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(LembretesActivity.this);

                                    alertDialog.setTitle("Excluir Lembrete:");
                                    alertDialog.setMessage("Tem certeza que deseja excluir esse lembrete?");
                                    alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Lembrete lembrete = lembretes.get(position);

                                            String emailUsuario = autenticacao.getCurrentUser().getEmail();
                                            String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                                            lembretesRef = firebaseRef.child("usuarios")
                                                    .child(idUsuario).child("lembretes");

                                            lembretesRef.child(lembrete.getKey()).removeValue();
                                            Toast.makeText(LembretesActivity.this, "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                                            adapterLembretes.notifyItemRemoved(i);

                                        }
                                    });
                                    alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(LembretesActivity.this, "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    AlertDialog alert = alertDialog.create();
                                    alert.show();
                                }

                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                                }
                            }
                    ));

            swipe();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LembretesActivity.this, AdicionarLembreteActivity.class);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swypeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swypeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirLembrete(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaLembretes);
    }

    public void excluirLembrete(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Lembrete:");
        alertDialog.setMessage("Tem certeza que deseja excluir esse lembrete?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                lembrete = lembretes.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                lembretesRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("lembretes");

                lembretesRef.child(lembrete.getKey()).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterLembretes.notifyItemRemoved(position);

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(LembretesActivity.this, "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapterLembretes.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }




    public void recuperarLembretes(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);

        Query query = firebaseRef.child("usuarios").child(idUsuario).child("lembretes").orderByChild("data");
        valueEventListenerLembretes = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lembretes.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Lembrete lembrete = dados.getValue(Lembrete.class);
                    lembrete.setKey(dados.getKey());
                    lembretes.add(lembrete);
                }

                adapterLembretes.notifyDataSetChanged();
                ProgressBarCustom.closeProgressBar(progressBar);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void semLembretes(){
        if (lembretes.isEmpty()) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(LembretesActivity.this);

            alertDialog.setTitle("Está vazio!");
            alertDialog.setMessage("Nenhum lembrete adicionado. Clique no + para adicionar um novo lembrete!");
            alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = alertDialog.create();
            if (!isFinishing()){
                alert.show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarLembretes();
        semLembretes();
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListenerLembretes);
    }

}
