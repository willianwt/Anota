package com.williantaiguara.anota.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Lembrete;

public class AdicionarResumoActivity extends AppCompatActivity {

    private Disciplina disciplina;
    private Lembrete resumo;
    private TextInputEditText tituloResumo, conteudoResumo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_adicionar_resumo);
        Toolbar toolbar = findViewById(R.id.toolbarAdcResumo);
        toolbar.setTitle("Novo Resumo");
        setSupportActionBar(toolbar);

        //recupera os dados da disciplina
        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");


        //inicializa os campos de texto
        tituloResumo = findViewById(R.id.etTituloResumo);
        conteudoResumo = findViewById(R.id.etConteudoResumo);


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

    private void SalvarResumo(){
        resumo = new Lembrete();
        resumo.setTitulo(tituloResumo.getText().toString());
        resumo.setConteudo(conteudoResumo.getText().toString());
        resumo.setData(DateCustom.dataAtual());
        resumo.setTipo("texto");
        Log.i("data", resumo.getData());

        resumo.salvarResumo(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                            Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                            Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));

        finish();
        Toast.makeText(this, "Resumo adicionado com Sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void VerificarCampos(){
        if (!tituloResumo.getText().toString().isEmpty()){
            if (!conteudoResumo.getText().toString().isEmpty()){
                SalvarResumo();
            }else{
                Toast.makeText(AdicionarResumoActivity.this,
                        "O conteúdo não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdicionarResumoActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }


}
