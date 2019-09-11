package com.williantaiguara.anota.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;

import com.williantaiguara.anota.R;

public class AdicionarFaltasActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_faltas);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarFalta);
        toolbar.setTitle("Novo Resumo");
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adicionar, menu);
        return true;
    }
}
