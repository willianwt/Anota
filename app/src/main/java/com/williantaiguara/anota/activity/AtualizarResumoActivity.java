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
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Lembrete;

public class AtualizarResumoActivity extends AppCompatActivity {
    private Lembrete resumoRecuperado;
    private TextInputEditText tituloResumo, conteudoResumo;
    private String keyResumo;
    private Lembrete resumo;
    private Disciplina disciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_resumo);
        Toolbar toolbar = findViewById(R.id.toolbarAtualizarResumo);
        toolbar.setTitle("Atualizar Resumo");
        setSupportActionBar(toolbar);

        //inicializa os campos de texto
        tituloResumo = findViewById(R.id.etAtualizarTituloResumo);
        conteudoResumo = findViewById(R.id.etAtualizarConteudoResumo);

        //recupera os dados da disciplina
        Bundle dados = getIntent().getExtras();
        resumoRecuperado = (Lembrete) dados.getSerializable("resumo");
        disciplina = (Disciplina) dados.getSerializable("disciplina");
        tituloResumo.setText(resumoRecuperado.getTitulo());
        conteudoResumo.setText(resumoRecuperado.getConteudo());
        keyResumo = resumoRecuperado.getKey();
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
                verificarCampos();
                item.setEnabled(true);

            }catch (Exception e){ //TODO: TRATAR ERROS
                e.printStackTrace();
                item.setEnabled(true);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void atualizarResumo(){
        resumo = new Lembrete();
        resumo.setTitulo(tituloResumo.getText().toString());
        resumo.setConteudo(conteudoResumo.getText().toString());
        resumo.setData(resumoRecuperado.getData());
        resumo.setKey(keyResumo);
        Log.i("data", resumo.getData());

        resumo.atualizarResumo(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                                Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                                Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));

        finish();
        Toast.makeText(this, "Resumo atualizado com Sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void verificarCampos(){
        if (!tituloResumo.getText().toString().isEmpty()){
            if (!conteudoResumo.getText().toString().isEmpty()){
                atualizarResumo();
            }else{
                Toast.makeText(AtualizarResumoActivity.this,
                        "O conteúdo não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AtualizarResumoActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
