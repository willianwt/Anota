package com.williantaiguara.anota.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;
import com.williantaiguara.anota.model.Nota;

import java.util.Calendar;

public class AdicionarNotaActivity extends AppCompatActivity {

    private TextInputEditText txNomeAtividade, txDataAtividade, txNotaAtividade;
    private Button btSalvarAtividade;
    private Disciplina disciplina;
    private Nota nota;
    private Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_nota);
        Toolbar toolbar = findViewById(R.id.toolbarAdcNota);
        toolbar.setTitle("Adicionar Nota");
        setSupportActionBar(toolbar);

        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        txNomeAtividade = findViewById(R.id.txNomeAtividadeNota);
        txDataAtividade = findViewById(R.id.txDataAtividadeNota);
        txNotaAtividade = findViewById(R.id.txNotaAtividade);
        btSalvarAtividade = findViewById(R.id.buttonSalvarNota);

        txDataAtividade.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (txDataAtividade.hasFocus()) {
                    abreCalendario();
                }
            }
        });

        btSalvarAtividade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCampos();
            }
        });
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

    public DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            txNotaAtividade.requestFocus();
        }

    };
    private void abreCalendario(){
        new DatePickerDialog(AdicionarNotaActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel() {
        txDataAtividade.setText(DateCustom.dataSelecionada(myCalendar.getTime()));
    }

    private void adicionarNota(){
        nota = new Nota();
        nota.setTitulo(txNomeAtividade.getText().toString());
        nota.setData(txDataAtividade.getText().toString());
        nota.setNota(Double.valueOf(txNotaAtividade.getText().toString()));
        nota.salvarNota(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));

        finish();
        Toast.makeText(this, "Nota adicionada com Sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void verificarCampos(){
        if (!txDataAtividade.getText().toString().isEmpty()){
            if (!txNotaAtividade.getText().toString().isEmpty()){
                adicionarNota();
            }else{
                Toast.makeText(AdicionarNotaActivity.this,
                        "O conteúdo não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdicionarNotaActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
