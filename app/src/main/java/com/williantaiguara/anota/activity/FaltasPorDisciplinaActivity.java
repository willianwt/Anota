package com.williantaiguara.anota.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterFaltas;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;

import java.util.ArrayList;
import java.util.List;

public class FaltasPorDisciplinaActivity extends AppCompatActivity {

    private AdapterFaltas adapterFaltas;
    private List<Falta> faltas = new ArrayList<>();
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Disciplina disciplina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faltas_por_disciplina);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void recuperarFaltas(){
        String emailUsuario = autenticacao.getCurrentUser().getEmail();
        String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
    }
}
