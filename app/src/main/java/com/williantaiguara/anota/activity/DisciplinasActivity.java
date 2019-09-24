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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
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
    private ValueEventListener valueEventListener;

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



        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisciplinasActivity.this, AdicionarDisciplinasActivity.class);
                intent.putExtra("nomeCurso", cursoEscolhido);
                intent.putExtra("semestre", semestreEscolhido);

                startActivity(intent);
            }
        });
        swipe();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_com_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home){
            try{
                item.setEnabled(false);
                try {
                    Intent intent = new Intent(DisciplinasActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setEnabled(true);

            }catch (Exception e){
                e.printStackTrace();
                item.setEnabled(true);

            }
        }
        return super.onOptionsItemSelected(item);
    }


    public void recuperarDisciplinas(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
        Query query = firebaseRef.child("usuarios").child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(cursoEscolhido))
                .child(Base64Custom.CodificarBase64(semestreEscolhido));
        valueEventListener = query.addValueEventListener(new ValueEventListener() {
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

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaDisciplinas);

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
                disciplina = disciplinas.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                listaCursosRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos")
                        .child(Base64Custom.CodificarBase64(cursoEscolhido))
                        .child(Base64Custom.CodificarBase64(semestreEscolhido));

                listaCursosRef.child(Base64Custom.CodificarBase64(disciplina.getKey())).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterDisciplinas.notifyItemRemoved(position);
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
                adapterDisciplinas.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }
    @Override
    protected void onStart() {
        super.onStart();
        recuperarDisciplinas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListener);
    }
}
