package com.williantaiguara.anota.activity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;

import java.util.ArrayList;

public class AdicionarCursoActivity extends AppCompatActivity {

    private AutoCompleteTextView tvAdicionarCurso;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private EditText semestre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_curso);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarCurso);
        toolbar.setTitle("Adiconar Novo Curso");

        setSupportActionBar(toolbar);

        semestre = findViewById(R.id.etSemestre);
        tvAdicionarCurso = findViewById(R.id.autoCompleteTVAdicionarCurso);
        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        firebaseRef.child("cursos").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    for (DataSnapshot suggestionSnapshot : dataSnapshot.getChildren()) {
                        String suggestion = suggestionSnapshot.getValue(String.class);
                        autoComplete.add(suggestion);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        tvAdicionarCurso.setAdapter(autoComplete);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adicionar_curso, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.adicionar_curso){
            try{
                VerificarCampos();

            }catch (Exception e){ //TODO: TRATAR ERROS
                e.printStackTrace();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void VerificarCampos(){
        if (!tvAdicionarCurso.getText().toString().isEmpty()){
            if (!semestre.getText().toString().isEmpty()){
                Intent intent = new Intent(this, MeusCursosActivity.class);
                intent.putExtra("nomeCurso", tvAdicionarCurso.getText().toString());
                intent.putExtra("semestre", semestre.getText().toString());

                startActivity(intent);
            }else{
                Toast.makeText(AdicionarCursoActivity.this,
                        "Informe o SEMESTRE!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(AdicionarCursoActivity.this,
                    "Informe o NOME DO CURSO!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
