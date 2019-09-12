package com.williantaiguara.anota.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;

public class AdicionarFaltasActivity extends AppCompatActivity {

    private TextView dataFalta, qtdFaltas;
    private Falta falta;
    private Disciplina disciplina;
    private Button salvarFalta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_faltas);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarFalta);
        toolbar.setTitle("Nova Falta");
        setSupportActionBar(toolbar);

        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        dataFalta = findViewById(R.id.txDiaFalta);
        qtdFaltas = findViewById(R.id.txQtdFaltas);
        salvarFalta = findViewById(R.id.btSalvarFalta);

        salvarFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarCampos();
            }
        });
    }

    public void AdicionarFaltas(){
        falta = new Falta();
        falta.setData(dataFalta.getText().toString());
        falta.setQtd(qtdFaltas.getText().toString());

        falta.salvarFalta(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                            Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                            Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));
        finish();
        Toast.makeText(this, "Falta adicionada com Sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void VerificarCampos(){
        if (!dataFalta.getText().toString().isEmpty()){
            if (!qtdFaltas.getText().toString().isEmpty()){
                AdicionarFaltas();
            }else{
                Toast.makeText(AdicionarFaltasActivity.this,
                        "O campo data não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdicionarFaltasActivity.this,
                    "Informe uma quantidade!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adicionar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.salvar){
            try{
                item.setEnabled(false);
                VerificarCampos();
                item.setEnabled(true);

            }catch (Exception e){ //TODO: TRATAR ERROS
                e.printStackTrace();
                item.setEnabled(true);

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
