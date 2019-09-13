package com.williantaiguara.anota.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterFaltas;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;

import java.util.ArrayList;
import java.util.List;

public class FaltasPorDisciplinaActivity extends AppCompatActivity {

    private AdapterFaltas adapterFaltas;
    private List<Falta> faltas = new ArrayList<>();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference faltasRef;
    private Disciplina disciplina;
    private RecyclerView recyclerViewListaFaltas;
    private ValueEventListener valueEventListenerFaltas;
    private Falta falta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faltas_por_disciplina);
        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(disciplina.getNomeDisciplina() + " - Faltas:");
        setSupportActionBar(toolbar);


        //recycler view
        recyclerViewListaFaltas = findViewById(R.id.recyclerViewListaFaltas);
        //ProgressBarCustom.openProgressBar(progressBar);

        //configurar adapter
        adapterFaltas = new AdapterFaltas(faltas, this);
        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaFaltas.setLayoutManager(layoutManager);
        recyclerViewListaFaltas.setHasFixedSize(true);
        recyclerViewListaFaltas.setAdapter(adapterFaltas);
        recyclerViewListaFaltas.scrollToPosition(faltas.size() - 1);
        recyclerViewListaFaltas.addItemDecoration(new DividerItemDecoration(recyclerViewListaFaltas.getContext(), DividerItemDecoration.VERTICAL));


        //evento de clique do recyclerview
        recyclerViewListaFaltas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewListaFaltas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                falta = faltas.get(position);
                                Intent intent = new Intent(FaltasPorDisciplinaActivity.this, AtualizarFaltaActivity.class);
                                intent.putExtra("falta", falta);
                                intent.putExtra("disciplina", disciplina);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );
        FloatingActionButton fab = findViewById(R.id.fabFaltas);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FaltasPorDisciplinaActivity.this, AdicionarFaltasActivity.class);
                intent.putExtra("disciplina", disciplina);
                startActivity(intent);
            }
        });

        swipe();
    }

    public void recuperarFaltas(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);

        Query query = firebaseRef.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("faltas").orderByChild("data");
        valueEventListenerFaltas = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                faltas.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Falta falta = dados.getValue(Falta.class);
                    falta.setKey(dados.getKey());
                    Log.i("falta", falta.getKey());
                    faltas.add(falta);
                }
                if (recyclerViewListaFaltas.getAdapter().getItemCount() != 0) {
                    recyclerViewListaFaltas.smoothScrollToPosition(recyclerViewListaFaltas.getAdapter().getItemCount() - 1);
                }
                adapterFaltas.notifyDataSetChanged();
                //ProgressBarCustom.closeProgressBar(progressBar);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void excluirResumo(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Resumo:");
        alertDialog.setMessage("Tem certeza que deseja excluir essa falta?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                falta = faltas.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                faltasRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos")
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                        .child("faltas");
                faltasRef.child(falta.getKey()).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterFaltas.notifyItemRemoved(position);

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(FaltasPorDisciplinaActivity.this, "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapterFaltas.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

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
                excluirResumo(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaFaltas);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarFaltas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListenerFaltas);
    }
}
