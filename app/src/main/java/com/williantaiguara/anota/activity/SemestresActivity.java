package com.williantaiguara.anota.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.TextView;

import com.williantaiguara.anota.R;

public class SemestresActivity extends AppCompatActivity {

    private String cursoEscolhido;
    private TextView txCursoEscolhido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semestres);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Semestre");
        setSupportActionBar(toolbar);

        txCursoEscolhido = findViewById(R.id.txCursoEscolhido);

        //recuperar dados da activity adicionar curso
        Bundle dados = getIntent().getExtras();
        cursoEscolhido = dados.getString("cursoEscolhido");

        txCursoEscolhido.setText("Qual semestre deseja visualizar para \n"+cursoEscolhido+"?");

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

}
