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
import com.williantaiguara.anota.model.Nota;

import java.util.Calendar;

public class AtualizarNotaActivity extends AppCompatActivity {

    private Nota nota, notaRecuperada;
    private TextInputEditText txAtualizarDataNota, txAtualizarNomeNota, txAtualizarValorNota;
    private String keyNota;
    private Disciplina disciplina;
    private Button btAtualizarNota;
    private Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_nota);
        Toolbar toolbar = findViewById(R.id.toolbarAtualizarNota);
        toolbar.setTitle("Atualizar Nota");
        setSupportActionBar(toolbar);

        txAtualizarDataNota = findViewById(R.id.txAtualizarDataAtividadeNota);
        txAtualizarNomeNota = findViewById(R.id.txAtualizarNomeAtividadeNota);
        txAtualizarValorNota = findViewById(R.id.txAtualizarNotaAtividade);
        btAtualizarNota = findViewById(R.id.btAtualizarNota);

        Bundle dados = getIntent().getExtras();
        notaRecuperada = (Nota) dados.getSerializable("nota");
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        txAtualizarNomeNota.setText(notaRecuperada.getTitulo());
        txAtualizarDataNota.setText(notaRecuperada.getData());
        txAtualizarValorNota.setText(String.valueOf(notaRecuperada.getNota()));
        keyNota = notaRecuperada.getKey();

        btAtualizarNota.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCampos();
            }
        });
        txAtualizarDataNota.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (txAtualizarDataNota.hasFocus()) {
                    abreCalendario();
                }
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

            }catch (Exception e){
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
            txAtualizarValorNota.requestFocus();
        }

    };
    private void abreCalendario(){
        new DatePickerDialog(AtualizarNotaActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel() {
        txAtualizarDataNota.setText(DateCustom.dataSelecionada(myCalendar.getTime()));
    }

    private void atualizarNota(){
        nota = new Nota();
        nota.setTitulo(txAtualizarNomeNota.getText().toString());
        nota.setData(txAtualizarDataNota.getText().toString());
        nota.setNota(Double.valueOf(txAtualizarValorNota.getText().toString()));
        nota.setKey(keyNota);

        nota.atualizarNota(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));

        finish();
        Toast.makeText(this, "Nota atualizada com Sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void verificarCampos(){
        if (!txAtualizarNomeNota.getText().toString().isEmpty()){
            if (!txAtualizarValorNota.getText().toString().isEmpty()){
                atualizarNota();
            }else{
                Toast.makeText(AtualizarNotaActivity.this,
                        "A nota não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AtualizarNotaActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
