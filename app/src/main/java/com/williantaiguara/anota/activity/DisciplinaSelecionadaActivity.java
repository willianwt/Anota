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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

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
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;
import com.williantaiguara.anota.model.Lembrete;

import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

public class DisciplinaSelecionadaActivity extends AppCompatActivity {

    private Disciplina disciplina;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference resumosRef;
    private Lembrete resumo;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private ValueEventListener valueEventListenerResumos;
    private AdapterListaResumos adapterListaResumos;
    private RecyclerView recyclerViewListaResumos;
    private List<Lembrete> resumos = new ArrayList<>();
    private String emailUsuario = autenticacao.getCurrentUser().getEmail();
    private String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
    private Button btTotalFaltas, btTotalNotas;

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

        btTotalFaltas = findViewById(R.id.btListaFaltas);
        btTotalNotas = findViewById(R.id.btListaNota);


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

        //evento de clique
        recyclerViewListaResumos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewListaResumos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Lembrete resumo = resumos.get(position);
                                Intent intent = new Intent(DisciplinaSelecionadaActivity.this, AtualizarResumoActivity.class);
                                intent.putExtra("resumo", resumo);
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

        swipe();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    public void adicionarResumo(View view){
        Intent intent = new Intent(this, AdicionarResumoActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void adicionarFalta(View view){
        Intent intent = new Intent(this, AdicionarFaltasActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void adicionarNota(View view){
        Intent intent = new Intent(this, AdicionarNotaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void abrirNotas(){

    }

    public void abrirFaltas(View view){
        Intent intent = new Intent(this, FaltasPorDisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void contarQtdFaltas(){
        Query query = firebaseRef.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("faltas").orderByChild("data");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total = 0;
                for (DataSnapshot qtd: dataSnapshot.getChildren()){
                    Falta qtdFalta = qtd.getValue(Falta.class);
                    total += Integer.valueOf(qtdFalta.getQtd());
                    btTotalFaltas.setText("Faltas: " + total);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumos(){

        Query query = firebaseRef.child("usuarios")
                                .child(idUsuario)
                                .child("cursos")
                                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                                .child("resumos").orderByChild("data");
        valueEventListenerResumos = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                resumos.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Lembrete resumo = dados.getValue(Lembrete.class);
                    resumo.setKey(dados.getKey());
                    resumos.add(resumo);
                }
                if (recyclerViewListaResumos.getAdapter().getItemCount() != 0) {
                    recyclerViewListaResumos.smoothScrollToPosition(recyclerViewListaResumos.getAdapter().getItemCount() - 1);
                }
                adapterListaResumos.notifyDataSetChanged();
                //ProgressBarCustom.closeProgressBar(progressBar);
                //nenhumLembreteEncontrado();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void excluirResumo(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Resumo:");
        alertDialog.setMessage("Tem certeza que deseja excluir esse resumo?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                resumo = resumos.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                resumosRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos")
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                        .child("resumos");
                resumosRef.child(resumo.getKey()).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterListaResumos.notifyItemRemoved(position);

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DisciplinaSelecionadaActivity.this, "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapterListaResumos.notifyDataSetChanged();

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

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaResumos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumos();
        contarQtdFaltas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListenerResumos);
    }
}
