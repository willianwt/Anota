package com.williantaiguara.anota.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterAdicionarDisciplinas;
import com.williantaiguara.anota.adapter.AdapterCursos;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.model.Curso;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MeusCursosActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListaCursos;
    private AdapterCursos adapterCursos;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference listaCursosRef;
    private Curso curso;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private List<Curso> cursos = new ArrayList<>();

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
                    Curso curso = dados.getValue(Curso.class);
                    curso.setKey(dados.getKey());
                    //TODO: na proxima activity, pegar os semestres do curso escolhido.
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
}
