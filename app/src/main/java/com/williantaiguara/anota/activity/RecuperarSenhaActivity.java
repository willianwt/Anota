package com.williantaiguara.anota.activity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;

public class RecuperarSenhaActivity extends AppCompatActivity {
    private EditText emailCadastrado;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private Button btRecuperarSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        emailCadastrado = findViewById(R.id.etEmailCadastrado);
        btRecuperarSenha = findViewById(R.id.btRecuperarSenha);

        btRecuperarSenha.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        solicitarRecuperacao();
                    }
                }
        );
    }

    public void solicitarRecuperacao(){
     if (!emailCadastrado.getText().toString().isEmpty() && emailCadastrado.getText().toString() != ""){
         String email = emailCadastrado.getText().toString();
         autenticacao.sendPasswordResetEmail(email).addOnCompleteListener(
                 new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if (task.isSuccessful()){
                             finish();
                             Toast.makeText(RecuperarSenhaActivity.this,
                                     "Email enviado. Verifique sua caixa de mensagens!",
                                     Toast.LENGTH_SHORT).show();
                         }
                     }
                 }
         );
     }
    }

}
