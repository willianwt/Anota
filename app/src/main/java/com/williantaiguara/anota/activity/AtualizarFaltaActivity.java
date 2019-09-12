package com.williantaiguara.anota.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;
import com.williantaiguara.anota.model.Lembrete;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AtualizarFaltaActivity extends AppCompatActivity {

    private Falta faltaRecuperada;
    private TextInputEditText txAtualizarDataFalta, txAtualizarQtdFalta;
    private String keyFalta;
    private Falta falta;
    private Disciplina disciplina;
    private Button btAtualizar;
    private Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_falta);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Atualizar Falta");
        setSupportActionBar(toolbar);

        txAtualizarDataFalta = findViewById(R.id.txAtualizarDiaFalta);
        txAtualizarQtdFalta = findViewById(R.id.txAtualizarQtdFaltas);
        btAtualizar = findViewById(R.id.btAtualizarFalta);

        Bundle dados = getIntent().getExtras();
        faltaRecuperada = (Falta) dados.getSerializable("falta");
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        txAtualizarQtdFalta.setText(faltaRecuperada.getQtd());
        txAtualizarDataFalta.setText(faltaRecuperada.getData());
        keyFalta = faltaRecuperada.getKey();

        btAtualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                VerificarCampos();
            }
        });

        txAtualizarDataFalta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (txAtualizarDataFalta.hasFocus()) {
                    AbreCalendario();
                }
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
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
    public DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            txAtualizarQtdFalta.requestFocus();
        }

    };
    private void AbreCalendario(){
        new DatePickerDialog(AtualizarFaltaActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel() {
        txAtualizarDataFalta.setText(DateCustom.dataSelecionada(myCalendar.getTime()));
    }

    private void AtualizarFalta(){
        falta = new Falta();
        falta.setQtd(txAtualizarQtdFalta.getText().toString());
        falta.setData(txAtualizarDataFalta.getText().toString());
        falta.setKey(keyFalta);

        falta.atualizarFalta(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));

        finish();
        Toast.makeText(this, "Falta atualizada com Sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void VerificarCampos(){
        if (!txAtualizarDataFalta.getText().toString().isEmpty()){
            if (!txAtualizarQtdFalta.getText().toString().isEmpty()){
                AtualizarFalta();
            }else{
                Toast.makeText(AtualizarFaltaActivity.this,
                        "O conteúdo não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AtualizarFaltaActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
