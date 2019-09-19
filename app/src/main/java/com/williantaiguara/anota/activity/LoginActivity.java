package com.williantaiguara.anota.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoLoginEmail, campoLoginSenha;
    private FirebaseAuth autenticacao;
    private ProgressBar progressBar;
    private Button buttonEntrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        campoLoginEmail = findViewById(R.id.editLoginEmail);
        campoLoginSenha = findViewById(R.id.editLoginSenha);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        progressBar = findViewById(R.id.progressBarLogin);
        buttonEntrar = findViewById(R.id.buttonLoginEntrar);

        closeProgressBar();
    }

    public void logarUsuario(Usuario usuario){
        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()
        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                openProgressBar();
                if (task.isSuccessful()){
                    closeProgressBar();
                    abrirTelaPrincipal();
                }else{
                    String excecao = "";
                    try{
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e) {
                        excecao = "Email não cadastrado!";
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        excecao = "Email e/ou senha incorretos!";
                    } catch (Exception e) {
                        excecao = "Ocorreu um erro! Verifique se está conectado a internet.." + e.getMessage();
                        e.printStackTrace();
                    }
                    closeProgressBar();
                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_SHORT).show();
                    buttonEntrar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void abrirTelaPrincipal() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void validarAutenticacaoUsuario(View view){
        String textoEmail = campoLoginEmail.getText().toString();
        String textoSenha = campoLoginSenha.getText().toString();

        if (!textoEmail.isEmpty()){
            if (!textoSenha.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setEmail(textoEmail);
                usuario.setSenha(textoSenha);
                openProgressBar();
                buttonEntrar.setVisibility(View.GONE);
                logarUsuario(usuario);
            }else{
                Toast.makeText(LoginActivity.this,
                        "Preencha a senha!",
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(LoginActivity.this,
                    "Preencha o Email!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void abrirRecuperarSenha(View view){
        Intent intent = new Intent(LoginActivity.this, RecuperarSenhaActivity.class);
        startActivity(intent);
    }

    protected void openProgressBar(){
        progressBar.setVisibility( View.VISIBLE );
    }

    protected void closeProgressBar(){
        progressBar.setVisibility( View.GONE );
    }
}
