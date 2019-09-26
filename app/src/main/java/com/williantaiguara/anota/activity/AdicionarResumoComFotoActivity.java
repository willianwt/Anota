package com.williantaiguara.anota.activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.DateCustom;
import com.williantaiguara.anota.helper.ProgressBarCustom;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Lembrete;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class AdicionarResumoComFotoActivity extends AppCompatActivity {

    private Disciplina disciplina;
    private Lembrete resumoComFoto;
    private TextInputEditText tituloResumoComFoto;
    private ImageView imagemResumoComFoto;
    private Bitmap imagemRecebida;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
    private String emailUsuario = autenticacao.getCurrentUser().getEmail();
    private String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_resumo_com_foto);
        Toolbar toolbar = findViewById(R.id.toolbarAdcResumoComFoto);
        toolbar.setTitle("Nova Foto");
        setSupportActionBar(toolbar);


        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");

        tituloResumoComFoto = findViewById(R.id.etTituloResumoComFoto);
        imagemResumoComFoto = findViewById(R.id.imgAdicionarResumoComFoto);

        progressBar = findViewById(R.id.progressBarAdcResumoComFoto);
        ProgressBarCustom.closeProgressBar(progressBar);


        String filename = getIntent().getStringExtra("imagem");
        try {
            FileInputStream is = this.openFileInput(filename);
            imagemRecebida = BitmapFactory.decodeStream(is);
            Glide.with(this).load(imagemRecebida).into(imagemResumoComFoto);

            is.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

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
                item.setVisible(false);
                verificarCampos();

            }catch (Exception e){ //TODO: TRATAR ERROS
                e.printStackTrace();
                item.setVisible(true);

            }
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void salvarResumo(){

        ProgressBarCustom.openProgressBar(progressBar);
        //Recuperar dados da imagem para o firebase
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagemRecebida.compress(Bitmap.CompressFormat.JPEG, 70, baos );
        byte[] dadosImagem = baos.toByteArray();

        //nome da imagem unico
        String nomeImagem = UUID.randomUUID().toString();

        //configurar a referencia do storage
        StorageReference imagemRef = storageReference.child("imagens")
                .child("fotos")
                .child(idUsuario)
                .child(nomeImagem);

        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
        if (!isNetworkAvailable()){
            Toast.makeText(AdicionarResumoComFotoActivity.this, "Ops, parece que vc está sem internet. Mas não se preocupe, a imagem será enviada e aparecerá no feed assim que tiver uma conexão...", Toast.LENGTH_LONG).show();

        }else {
            Toast.makeText(AdicionarResumoComFotoActivity.this, "Enviando imagem, aparecerá dentro de instantes...", Toast.LENGTH_SHORT).show();
        }
        finish();

        Task<Uri> urlTask =  uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()){
                    throw task.getException();
                }
                return imagemRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()){
                    Uri downloadUrl = task.getResult();

                    resumoComFoto = new Lembrete();
                    resumoComFoto.setTitulo(tituloResumoComFoto.getText().toString());
                    resumoComFoto.setImagem(downloadUrl.toString());
                    resumoComFoto.setData(DateCustom.dataAtual());
                    resumoComFoto.setTipo("foto");

                    resumoComFoto.salvarResumo(Base64Custom.CodificarBase64(disciplina.getNomeCurso()),
                            Base64Custom.CodificarBase64(disciplina.getSemestreCurso()),
                            Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()));

                    overridePendingTransition(0, 0);
                    Toast.makeText(AdicionarResumoComFotoActivity.this, "Resumo adicionado com Sucesso!", Toast.LENGTH_SHORT).show();

                }else{
                    Toast.makeText(AdicionarResumoComFotoActivity.this,
                            "Erro ao fazer upload da imagem!",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    public void verificarCampos(){
        if (!tituloResumoComFoto.getText().toString().isEmpty()){
                salvarResumo();
        }else{
            Toast.makeText(AdicionarResumoComFotoActivity.this,
                    "O título não pode estar vazio!",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
