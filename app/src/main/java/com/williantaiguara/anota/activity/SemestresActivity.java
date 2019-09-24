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

public class SemestresActivity extends AppCompatActivity {

    private String cursoEscolhido;
    private TextView txCursoEscolhido;
    private RecyclerView recyclerViewListaSemestres;
    private AdapterCursos adapterSemestres;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference listaCursosRef;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Disciplina semestre;
    private List<Disciplina> semestres = new ArrayList<>();
    private ValueEventListener valueEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semestres);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Semestre");
        setSupportActionBar(toolbar);

        txCursoEscolhido = findViewById(R.id.txCursoEscolhido);

        recyclerViewListaSemestres = findViewById(R.id.recyclerViewListaSemestre);

        adapterSemestres = new AdapterCursos(semestres, this);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaSemestres.setLayoutManager(layoutManager);
        recyclerViewListaSemestres.setHasFixedSize(true);
        recyclerViewListaSemestres.setAdapter(adapterSemestres);


        //recuperar dados da activity adicionar curso
        Bundle dados = getIntent().getExtras();
        cursoEscolhido = dados.getString("cursoEscolhido");

        txCursoEscolhido.setText("Qual semestre deseja visualizar para \n"+cursoEscolhido+"?");


        //evento de clique
        recyclerViewListaSemestres.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewListaSemestres,
                        new RecyclerItemClickListener.OnItemClickListener(){

                            @Override
                            public void onItemClick(View view, int position) {
                                Disciplina semestre = semestres.get(position);
                                Intent intent = new Intent(SemestresActivity.this, DisciplinasActivity.class);
                                intent.putExtra("semestreEscolhido", semestre.getKey());
                                intent.putExtra("cursoEscolhido", cursoEscolhido);
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

        //todo: alterar para adicionar um novo semestre caso o usuario queira
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SemestresActivity.this, AdicionarCursoActivity.class);
                intent.putExtra("nomeCurso", cursoEscolhido);
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
        if (id == R.id.salvar){
            try{
                item.setEnabled(false);
                try {
                    Intent intent = new Intent(SemestresActivity.this, MainActivity.class);
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

    public void recuperarSemestres(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
        Query query = firebaseRef.child("usuarios").child(idUsuario)
                                                    .child("cursos")
                                                    .child(Base64Custom.CodificarBase64(cursoEscolhido));
        valueEventListener = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                semestres.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Disciplina semestre = dados.getValue(Disciplina.class);
                    semestre.setKey(Base64Custom.DecodificarBase64(dados.getKey()));
                    semestres.add(semestre);
                }
                adapterSemestres.notifyDataSetChanged();


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

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaSemestres);

    }

    public void excluirCurso(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir CURSO:");
        alertDialog.setMessage("Tem certeza que deseja excluir esse SEMESTRE? Isso não pode ser desfeito.");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                semestre = semestres.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                listaCursosRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos")
                        .child(Base64Custom.CodificarBase64(cursoEscolhido));
                Log.i("curso", semestre.getKey());

                listaCursosRef.child(Base64Custom.CodificarBase64(semestre.getKey())).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterSemestres.notifyItemRemoved(position);
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
                adapterSemestres.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarSemestres();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListener);
    }
}
