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
import android.widget.TextView;
import android.widget.Toast;

import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;

import java.util.Calendar;

public class AdicionarFaltasActivity extends AppCompatActivity {

    private TextView dataFalta, qtdFaltas;
    private Falta falta;
    private Disciplina disciplina;
    private Button salvarFalta;
    private Calendar myCalendar = Calendar.getInstance();

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

        dataFalta.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (dataFalta.hasFocus()) {
                    abreCalendario();
                }
            }
        });

        salvarFalta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCampos();
            }
        });
    }

    public void adicionarFaltas(){
        falta = new Falta();
        falta.setData(dataFalta.getText().toString());
        falta.setQtd(qtdFaltas.getText().toString());

        falta.salvarFalta(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                            Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                            Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));
        finish();
        Toast.makeText(this, "Falta adicionada com Sucesso!", Toast.LENGTH_SHORT).show();
    }

    public void verificarCampos(){
        if (!dataFalta.getText().toString().isEmpty()){
            if (!qtdFaltas.getText().toString().isEmpty()){
                adicionarFaltas();
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

    public DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
            qtdFaltas.requestFocus();
        }

    };
    private void abreCalendario(){
        new DatePickerDialog(AdicionarFaltasActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel() {
        dataFalta.setText(DateCustom.dataSelecionada(myCalendar.getTime()));
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
}
