package com.williantaiguara.anota.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterAdicionarDisciplinas;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.model.Disciplina;

import java.util.ArrayList;
import java.util.List;

public class AdicionarDisciplinasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewListaDisciplinasAdicionadas;
    private List<Disciplina> disciplinaList = new ArrayList<>();
    private EditText nomeDisciplina;
    private EditText nomeProfessorDisciplina;
    private EditText emailProfessorDisciplina;
    private Button botaoAdicionarDisciplina;
    private AdapterAdicionarDisciplinas adapter = new AdapterAdicionarDisciplinas(disciplinaList);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_disciplinas);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarDisciplinas);
        toolbar.setTitle("Disciplina do Semestre");
        setSupportActionBar(toolbar);
        recyclerViewListaDisciplinasAdicionadas = findViewById(R.id.recyclerViewAdicionarDisciplinas);
        nomeDisciplina = findViewById(R.id.etNomeDisciplina);
        nomeProfessorDisciplina = findViewById(R.id.etNomeProfessor);
        emailProfessorDisciplina = findViewById(R.id.etEmailProfessor);
        botaoAdicionarDisciplina = findViewById(R.id.btAdicionarDisciplina);

        botaoAdicionarDisciplina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try  {
                    InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    verificarCampos();
                } catch (Exception e) {

                }
            }
        });

        swipe();


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewListaDisciplinasAdicionadas.setLayoutManager(layoutManager);
        recyclerViewListaDisciplinasAdicionadas.setHasFixedSize(true);
        recyclerViewListaDisciplinasAdicionadas.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        recyclerViewListaDisciplinasAdicionadas.setAdapter(adapter);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_adicionar_disciplinas, menu);
        return true;
    }

    public void verificarCampos(){
        if (!nomeDisciplina.getText().toString().isEmpty()){
            if (!nomeProfessorDisciplina.getText().toString().isEmpty()){
                    adicionarDisciplinaNaLista();
            }else{
                Toast.makeText(AdicionarDisciplinasActivity.this,
                        "Informe o NOME do Professor!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdicionarDisciplinasActivity.this,
                    "Informe o NOME da DISCIPLINA!", Toast.LENGTH_SHORT).show();
        }
    }

    public void adicionarDisciplinaNaLista(){
        Disciplina disciplina = new Disciplina(nomeDisciplina.getText().toString(),
                                                nomeProfessorDisciplina.getText().toString(),
                                                emailProfessorDisciplina.getText().toString());
        disciplinaList.add(disciplina);
        nomeDisciplina.getText().clear();
        nomeProfessorDisciplina.getText().clear();
        emailProfessorDisciplina.getText().clear();
        adapter.notifyDataSetChanged();
        recyclerViewListaDisciplinasAdicionadas.scrollToPosition(disciplinaList.size() -1);
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
                excluirDisciplinaAdicionada(viewHolder);

            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaDisciplinasAdicionadas);
    }

    public void excluirDisciplinaAdicionada(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Disciplina:");
        alertDialog.setMessage("Tem certeza que deseja excluir essa disciplina?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                disciplinaList.remove(position);

                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapter.notifyItemRemoved(position);
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getApplicationContext(), "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }
}
