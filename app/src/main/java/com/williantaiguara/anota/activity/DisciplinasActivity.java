package com.williantaiguara.anota.activity;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.TextView;

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
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class DisciplinasActivity extends AppCompatActivity{
    private String semestreEscolhido;
    private String cursoEscolhido;
    private TextView txCursoEscolhido;
    private RecyclerView recyclerViewListaDisciplinas;
    private AdapterCursos adapterDisciplinas;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference listaCursosRef;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Disciplina disciplina;
    private List<Disciplina> disciplinas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplinas);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Disciplinas");
        setSupportActionBar(toolbar);

        txCursoEscolhido = findViewById(R.id.txSemestreEscolhido);

        recyclerViewListaDisciplinas = findViewById(R.id.recyclerViewListaDisciplinas);

        adapterDisciplinas = new AdapterCursos(disciplinas, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaDisciplinas.setLayoutManager(layoutManager);
        recyclerViewListaDisciplinas.setHasFixedSize(true);
        recyclerViewListaDisciplinas.setAdapter(adapterDisciplinas);


        //recuperar dados da activity adicionar curso
        Bundle dados = getIntent().getExtras();
        semestreEscolhido = dados.getString("semestreEscolhido");
        cursoEscolhido = dados.getString("cursoEscolhido");

        txCursoEscolhido.setText("Estas são as disciplinas do \n"+semestreEscolhido+"º semestre:");

        //evento de clique
        recyclerViewListaDisciplinas.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewListaDisciplinas,
                        new RecyclerItemClickListener.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Disciplina disciplina = disciplinas.get(position);
                                Intent intent = new Intent(DisciplinasActivity.this, DisciplinaSelecionadaActivity.class);
                                disciplina.setSemestreCurso(semestreEscolhido);
                                disciplina.setNomeCurso(cursoEscolhido);
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
                ));

        //todo: alterar para adicionar mais disciplinas neste curso e semestre

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void recuperarDisciplinas(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
        Query query = firebaseRef.child("usuarios").child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(cursoEscolhido))
                .child(Base64Custom.CodificarBase64(semestreEscolhido));
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                disciplinas.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Disciplina semestre = dados.getValue(Disciplina.class);
                    semestre.setKey(Base64Custom.DecodificarBase64(dados.getKey()));
                    disciplinas.add(semestre);
                }
                adapterDisciplinas.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarDisciplinas();
    }

}
