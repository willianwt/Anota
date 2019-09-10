package com.williantaiguara.anota.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
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
import com.williantaiguara.anota.adapter.AdapterLembretes;
import com.williantaiguara.anota.adapter.AdapterListaResumos;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.ProgressBarCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Lembrete;

import java.util.ArrayList;
import java.util.List;

public class DisciplinaSelecionadaActivity extends AppCompatActivity {

    private Disciplina disciplina;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference resumosRef;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private ValueEventListener valueEventListenerResumos;
    private AdapterListaResumos adapterListaResumos;
    private RecyclerView recyclerViewListaResumos;
    private List<Lembrete> resumos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina_selecionada);
        //recupera os dados da disciplina
        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");
        Log.i("dadossemestre", disciplina.getSemestreCurso());
        Log.i("dadosnomecurso", disciplina.getNomeCurso());
        Log.i("dadosprof", disciplina.getNomeProfessorDisciplina());
        Log.i("dadosnomedisc", disciplina.getNomeDisciplina());
        Log.i("dadosemailprof", disciplina.getEmailProfessorDisciplina());
        Log.i("dadoskey", disciplina.getKey());


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(disciplina.getNomeDisciplina());
        setSupportActionBar(toolbar);

        //recycler view
        recyclerViewListaResumos = findViewById(R.id.recyclerViewListaResumos);
        //ProgressBarCustom.openProgressBar(progressBar);

        //configurar adapter
        adapterListaResumos = new AdapterListaResumos(resumos, this);
        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaResumos.setLayoutManager(layoutManager);
        recyclerViewListaResumos.setHasFixedSize(true);
        recyclerViewListaResumos.setAdapter(adapterListaResumos);
        recyclerViewListaResumos.scrollToPosition(resumos.size() - 1);


        recyclerViewListaResumos.addItemDecoration(new DividerItemDecoration(recyclerViewListaResumos.getContext(), DividerItemDecoration.VERTICAL));
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void adicionarResumo(View view){
        Intent intent = new Intent(this, AdicionarResumoActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void recuperarResumos(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);

        Query query = firebaseRef.child("usuarios").child(idUsuario).child("resumos").orderByChild("data");
        valueEventListenerResumos = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                resumos.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Lembrete resumo = dados.getValue(Lembrete.class);
                    resumo.setKey(dados.getKey());
                    resumos.add(resumo);
                }
                recyclerViewListaResumos.smoothScrollToPosition(recyclerViewListaResumos.getAdapter().getItemCount() -1);
                adapterListaResumos.notifyDataSetChanged();
                //ProgressBarCustom.closeProgressBar(progressBar);
                //nenhumLembreteEncontrado();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumos();
    }
}
