package com.williantaiguara.anota.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.model.Lembrete;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AdicionarLembreteActivity extends AppCompatActivity {

    private TextInputEditText dataLembrete;
    private TextInputEditText tituloLembrete;
    private TextInputEditText conteudoLembrete;
    private Calendar myCalendar = Calendar.getInstance();
    private Lembrete lembrete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_lembrete);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarLembrete);
        toolbar.setTitle("Adiconar Lembrete");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dataLembrete = findViewById(R.id.etDataLembrete);
        tituloLembrete = findViewById(R.id.etTituloLembrete);
        conteudoLembrete = findViewById(R.id.etConteudoLembrete);



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
            conteudoLembrete.requestFocus();
        }

    };

    private void AbreCalendario(){
        new DatePickerDialog(AdicionarLembreteActivity.this, date,
                myCalendar.get(Calendar.YEAR),
                myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    private void updateLabel() {

        String myFormat = "yyyy-MM-dd"; //formato da data
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));

        dataLembrete.setText(sdf.format(myCalendar.getTime()));
    }

    private void SalvarLembrete(){
        lembrete = new Lembrete();
        lembrete.setTitulo(tituloLembrete.getText().toString());
        lembrete.setData(dataLembrete.getText().toString());
        lembrete.setConteudo(conteudoLembrete.getText().toString());

        lembrete.salvarLembrete();

        finish();
        Toast.makeText(this, "Lembrete adicionado com Sucesso!", Toast.LENGTH_SHORT).show();
    }
    public void VerificarCampos(){
        if (!tituloLembrete.getText().toString().isEmpty()){
            if (!dataLembrete.getText().toString().isEmpty()){
                SalvarLembrete();
            }else{
                Toast.makeText(AdicionarLembreteActivity.this,
                        "Escolha uma data!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdicionarLembreteActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
