package com.williantaiguara.anota.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Lembrete;

import java.util.Calendar;

public class AtualizarLembreteDisciplinaActivity extends AppCompatActivity {

    private TextInputEditText dataLembrete;
    private TextInputEditText tituloLembrete;
    private TextInputEditText conteudoLembrete;
    private Calendar myCalendar = Calendar.getInstance();
    private Lembrete lembrete;
    private String keyLembrete;
    private Disciplina disciplina;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar_lembrete);
        Toolbar toolbar = findViewById(R.id.toolbarAtualizarLembrete);
        toolbar.setTitle("Atualizar Lembrete");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        dataLembrete = findViewById(R.id.etDataLembrete);
        tituloLembrete = findViewById(R.id.etTituloLembrete);
        conteudoLembrete = findViewById(R.id.etConteudoLembrete);

        //recuperar dados do lembrete
        Bundle dados = getIntent().getExtras();
        Lembrete lembreteRecuperado = (Lembrete) dados.getSerializable("objeto");
        tituloLembrete.setText(lembreteRecuperado.getTitulo());
        dataLembrete.setText(lembreteRecuperado.getData());
        conteudoLembrete.setText(lembreteRecuperado.getConteudo());
        keyLembrete = lembreteRecuperado.getKey();
        disciplina = (Disciplina) dados.getSerializable("disciplina");


        dataLembrete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(dataLembrete.hasFocus()) {
                    AbreCalendario();
                }
            }
        });
        dataLembrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AbreCalendario();
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
                AtualizarLembrete();
                finish();
                Toast.makeText(this, "Lembrete ATUALIZADO com Sucesso!", Toast.LENGTH_SHORT).show();

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
            conteudoLembrete.requestFocus();
        }

    };

    private void AbreCalendario(){
        new DatePickerDialog(AtualizarLembreteDisciplinaActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel() {
        dataLembrete.setText(DateCustom.dataSelecionada(myCalendar.getTime()));
    }


    private void AtualizarLembrete(){
        lembrete = new Lembrete();
        lembrete.setTitulo(tituloLembrete.getText().toString());
        lembrete.setData(dataLembrete.getText().toString());
        lembrete.setConteudo(conteudoLembrete.getText().toString());
        lembrete.setKey(keyLembrete);
        Log.i("disciplina", disciplina.toString());
        Log.i("lembrete", lembrete.toString());
        lembrete.atualizarLembreteDisciplina(disciplina);
    }
}
