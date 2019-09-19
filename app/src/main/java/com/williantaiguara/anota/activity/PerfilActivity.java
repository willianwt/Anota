package com.williantaiguara.anota.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;

public class PerfilActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private FirebaseUser usuario = autenticacao.getInstance().getCurrentUser();
    private EditText nomeExibicao, email, novaSenha, confirmarNovaSenha;
    private Button btAtualizarDados;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Toolbar toolbar = findViewById(R.id.toolbarPerfil);
        toolbar.setTitle("Meu Perfil");
        setSupportActionBar(toolbar);


        nomeExibicao = findViewById(R.id.etNomeExibicaoPerfil);
        email = findViewById(R.id.etEmailPerfil);
        novaSenha = findViewById(R.id.etNovaSenhaPerfil);
        confirmarNovaSenha = findViewById(R.id.etConfirmarNovaSenhaPerfil);
        btAtualizarDados = findViewById(R.id.btAtualizarDadosPerfil);

        usuario.reload();
        String nome = usuario.getDisplayName();
        Log.i("nome", nome);

        nomeExibicao.setText(nome);
        email.setText(usuario.getEmail());


    }
/*
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
                verificarCampos((View) item);
                //TODO confirmar se quer alterar a senha, caso esteja preenchido
                item.setEnabled(true);

            }catch (Exception e){ //TODO: TRATAR ERROS
                e.printStackTrace();
                item.setEnabled(true);

            }
        }
        return super.onOptionsItemSelected(item);
    }
*/
    public void verificarCampos(View view){
        if (!nomeExibicao.getText().toString().isEmpty()){
            if (!email.getText().toString().isEmpty() || email.getText().toString() != ""){
                if (!novaSenha.getText().toString().isEmpty() && confirmarNovaSenha.getText().toString().equals(novaSenha.getText().toString())){
                    atualizarDados();
                    atualizarSenha();
                    finish();
                }else{
                    atualizarDados();
                    finish();
                }
            }else{
                Toast.makeText(PerfilActivity.this,
                        "O email não pode estar vazio!",
                        Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(PerfilActivity.this,
                    "Insira um nome!",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void atualizarDados(){
        usuario.updateProfile(new UserProfileChangeRequest.Builder()
            .setDisplayName(nomeExibicao.getText().toString())
            .build());
        if (!email.getText().toString().isEmpty() && email.getText().toString() != "") {
            usuario.updateEmail(email.getText().toString());
        }
        usuario.reload();
        Toast.makeText(PerfilActivity.this,
                "Dados atualizados com sucesso!",
                Toast.LENGTH_SHORT).show();
    }

    public void atualizarSenha(){
        if (!novaSenha.getText().toString().isEmpty() && novaSenha.getText().toString() != "") {
            usuario.updatePassword(novaSenha.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(PerfilActivity.this,
                            "Senha atualizada!",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
