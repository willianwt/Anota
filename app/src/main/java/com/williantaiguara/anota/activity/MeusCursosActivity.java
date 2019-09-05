package com.williantaiguara.anota.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.williantaiguara.anota.adapter.AdapterCursos;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.FormatadorDeCaracteresIniciais;
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Disciplina;


import java.util.ArrayList;
import java.util.List;

public class MeusCursosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListaCursos;
    private AdapterCursos adapterCursos;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference listaCursosRef;
    private Disciplina curso;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private List<Disciplina> cursos = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_cursos);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Meus Cursos");
        setSupportActionBar(toolbar);

        recyclerViewListaCursos = findViewById(R.id.recyclerViewListaCursos);

        adapterCursos = new AdapterCursos(cursos, this);


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaCursos.setLayoutManager(layoutManager);
        recyclerViewListaCursos.setHasFixedSize(true);
        recyclerViewListaCursos.setAdapter(adapterCursos);

        //evento de clique
        recyclerViewListaCursos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                getApplicationContext(),
                recyclerViewListaCursos,
                new RecyclerItemClickListener.OnItemClickListener(){

                    @Override
                    public void onItemClick(View view, int position) {
                        Disciplina curso = cursos.get(position);
                        Intent intent = new Intent(MeusCursosActivity.this, SemestresActivity.class);
                        intent.putExtra("cursoEscolhido", curso.getKey());
                        startActivity(intent);

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void recuperarCursos(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
        Query query = firebaseRef.child("usuarios").child(idUsuario).child("cursos");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                cursos.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Disciplina curso = dados.getValue(Disciplina.class);
                    curso.setKey(Base64Custom.DecodificarBase64(FormatadorDeCaracteresIniciais.remove2char(dados.getKey())));
                    cursos.add(curso);
                }
                adapterCursos.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarCursos();

    }

    public void swipe() {

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
                excluirCurso(viewHolder);

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaCursos);

    }

    public void excluirCurso(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir CURSO:");
        alertDialog.setMessage("Tem certeza que deseja excluir esse CURSO? Isso não pode ser desfeito.");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                curso = cursos.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                listaCursosRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos");
                Log.i("curso", curso.getKey());

                listaCursosRef.child(FormatadorDeCaracteresIniciais.adicionaCharParaCurso(curso.getKey())).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterCursos.notifyItemRemoved(position);
                finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);


            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapterCursos.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }
}
