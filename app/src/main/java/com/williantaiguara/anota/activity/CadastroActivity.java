package com.williantaiguara.anota.activity;

import android.content.Context;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.ProgressBarCustom;
import com.williantaiguara.anota.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome, campoEmail, campoSenha, campoConfirmarSenha;
    private FirebaseAuth autenticacao;
    private ProgressBar progressBar;
    private Button enviarCadastro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        Toolbar toolbar = findViewById(R.id.toolbarAdicionarCurso);
        setSupportActionBar(toolbar);

        campoNome = findViewById(R.id.editCadastroNome);
        campoEmail = findViewById(R.id.editCadastroEmail);
        campoSenha = findViewById(R.id.editCadastroSenha);
        campoConfirmarSenha = findViewById(R.id.editCadastroConfirmarSenha);
        enviarCadastro = findViewById(R.id.buttonEnviarCadastro);


        progressBar = findViewById(R.id.progressBarCadastro);
        ProgressBarCustom.closeProgressBar(progressBar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
    public  void salvarUsuario(final Usuario usuario){
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(CadastroActivity.this,
                            "Usuário cadastrado com Sucesso!",
                            Toast.LENGTH_SHORT).show();
                    try{
                        String identificadorUsuario = Base64Custom.CodificarBase64(usuario.getEmail());
                        usuario.setUid(identificadorUsuario);
                        usuario.salvar();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    finish();
                }else{
                    String excecao = "";

                    try{
                        throw task.getException();
                    }catch (FirebaseAuthWeakPasswordException e){
                        excecao = "Digite uma senha mais forte!";
                    }catch (FirebaseAuthInvalidCredentialsException e) {
                        excecao = "Digite um email válido!";
                    }catch (FirebaseAuthUserCollisionException e) {
                        excecao = "Já existe um usuário com este email!";
                    }catch (Exception e) {
                        excecao = "Erro ao cadastrar usuário!" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, excecao, Toast.LENGTH_SHORT).show();
                }
                ProgressBarCustom.closeProgressBar(progressBar);
                enviarCadastro.setVisibility(View.VISIBLE);
            }
        });
    }

    public void validarCadastro(View view){
        String textoNome = campoNome.getText().toString();
        String textoEmail = campoEmail.getText().toString();
        String textoSenha = campoSenha.getText().toString();
        String textoConfirmarSenha = campoConfirmarSenha.getText().toString();

                if (!textoNome.isEmpty()){
                    if (!textoEmail.isEmpty()){
                        if (!textoSenha.isEmpty()){
                            if (!textoConfirmarSenha.isEmpty() && textoConfirmarSenha.equals(textoSenha)){
                                Usuario usuario = new Usuario();
                                usuario.setNome(textoNome);
                                usuario.setEmail(textoEmail);
                                usuario.setSenha(textoSenha);
                                enviarCadastro.setVisibility(View.GONE);
                                ProgressBarCustom.openProgressBar(progressBar);
                                salvarUsuario(usuario);

                            }else{
                                Toast.makeText(CadastroActivity.this,
                                        "As senhas não conferem!",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(CadastroActivity.this,
                                    "Preencha a senha!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(CadastroActivity.this,
                                "Preencha o Email!",
                                Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CadastroActivity.this,
                            "Preencha o nome!",
                            Toast.LENGTH_SHORT).show();
                }


    }



    @Override
    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(this.getWindow().getDecorView().getRootView().getWindowToken(), 0);
        return true;
    }

}
