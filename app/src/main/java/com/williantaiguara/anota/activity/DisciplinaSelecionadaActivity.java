package com.williantaiguara.anota.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.model.Disciplina;

public class DisciplinaSelecionadaActivity extends AppCompatActivity {

    private Disciplina disciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina_selecionada);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //recupera os dados da disciplina
        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");
        Log.i("dadossemestre", disciplina.getSemestreCurso());
        Log.i("dadosnomecurso", disciplina.getNomeCurso());
        Log.i("dadosprof", disciplina.getNomeProfessorDisciplina());
        Log.i("dadosnomedisc", disciplina.getNomeDisciplina());
        Log.i("dadosemailprof", disciplina.getEmailProfessorDisciplina());
        Log.i("dadoskey", disciplina.getKey());


        //todo: este botao vai servir para adicionar resumos, notas e faltas.
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

}
