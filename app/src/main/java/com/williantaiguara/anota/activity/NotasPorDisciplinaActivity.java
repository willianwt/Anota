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

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterNotas;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Nota;

import java.util.ArrayList;
import java.util.List;

public class NotasPorDisciplinaActivity extends AppCompatActivity {

    private AdapterNotas adapterNotas;
    private List<Nota> notas = new ArrayList<>();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private DatabaseReference notasRef;
    private Disciplina disciplina;
    private RecyclerView recyclerViewListaNotas;
    private ValueEventListener valueEventListenerNotas;
    private Nota nota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notas_por_disciplina);
        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(disciplina.getNomeDisciplina() + " - Notas:");
        setSupportActionBar(toolbar);

        recyclerViewListaNotas = findViewById(R.id.recyclerViewListaNotas);

        adapterNotas = new AdapterNotas(notas, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaNotas.setLayoutManager(layoutManager);
        recyclerViewListaNotas.setHasFixedSize(true);
        recyclerViewListaNotas.setAdapter(adapterNotas);
        recyclerViewListaNotas.scrollToPosition(notas.size() -1);

        //evento de clique do recyclerview
        recyclerViewListaNotas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewListaNotas,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                nota = notas.get(position);
                                Intent intent = new Intent(NotasPorDisciplinaActivity.this, AtualizarNotaActivity.class);
                                intent.putExtra("nota", nota);
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

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NotasPorDisciplinaActivity.this, AdicionarNotaActivity.class);
                intent.putExtra("disciplina", disciplina);
                startActivity(intent);
            }
        });

        swipe();
    }

    public void recuperarNotas(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);

        Query query = firebaseRef.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("notas").orderByChild("data");
        valueEventListenerNotas = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notas.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    nota = dados.getValue(Nota.class);
                    nota.setKey(dados.getKey());
                    notas.add(nota);

                }
                if (recyclerViewListaNotas.getAdapter().getItemCount() != 0) {
                    recyclerViewListaNotas.smoothScrollToPosition(recyclerViewListaNotas.getAdapter().getItemCount() - 1);
                }
                adapterNotas.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void excluirNota(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Nota:");
        alertDialog.setMessage("Tem certeza que deseja excluir essa nota?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                nota = notas.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                notasRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos")
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                        .child("notas");
                notasRef.child(nota.getKey()).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterNotas.notifyItemRemoved(position);

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(NotasPorDisciplinaActivity.this, "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapterNotas.notifyDataSetChanged();

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
                excluirNota(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaNotas);
    }
    @Override
    protected void onStart() {
        super.onStart();
        recuperarNotas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListenerNotas);
    }
}
