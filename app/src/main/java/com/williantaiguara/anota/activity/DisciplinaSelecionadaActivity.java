package com.williantaiguara.anota.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.williantaiguara.anota.R;
import com.williantaiguara.anota.adapter.AdapterListaResumos;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;
import com.williantaiguara.anota.helper.Permissao;
import com.williantaiguara.anota.helper.RecyclerItemClickListener;
import com.williantaiguara.anota.model.Disciplina;
import com.williantaiguara.anota.model.Falta;
import com.williantaiguara.anota.model.Lembrete;
import com.williantaiguara.anota.model.Nota;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DisciplinaSelecionadaActivity extends AppCompatActivity {

    private String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    private Disciplina disciplina;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebaseDatabase();
    private DatabaseReference resumosRef;
    private Lembrete resumo;
    private FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private ValueEventListener valueEventListenerResumos;
    private AdapterListaResumos adapterListaResumos;
    private RecyclerView recyclerViewListaResumos;
    private List<Lembrete> resumos = new ArrayList<>();
    private String emailUsuario = autenticacao.getCurrentUser().getEmail();
    private String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
    private Button btTotalFaltas, btTotalNotas;
    private static final int SELECAO_CAMERA = 100;
    private StorageReference storageReference = ConfiguracaoFirebase.getFirebaseStorage();
    private String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disciplina_selecionada);
        //recupera os dados da disciplina
        Bundle dados = getIntent().getExtras();
        disciplina = (Disciplina) dados.getSerializable("disciplina");


        Permissao.validarPermissoes(permissoesNecessarias, this, 1);

        btTotalFaltas = findViewById(R.id.btListaFaltas);
        btTotalNotas = findViewById(R.id.btListaNota);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(disciplina.getNomeDisciplina());
        setSupportActionBar(toolbar);

        //recycler view
        recyclerViewListaResumos = findViewById(R.id.recyclerViewListaResumos);
        //ProgressBarCustom.openProgressBar(progressBar);

        //configurar adapter
        adapterListaResumos = new AdapterListaResumos(resumos, getApplicationContext());
        //configurar recyclerview
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerViewListaResumos.setLayoutManager(layoutManager);
        recyclerViewListaResumos.setHasFixedSize(true);
        recyclerViewListaResumos.setAdapter(adapterListaResumos);
        recyclerViewListaResumos.scrollToPosition(resumos.size() - 1);
        recyclerViewListaResumos.addItemDecoration(new DividerItemDecoration(recyclerViewListaResumos.getContext(), DividerItemDecoration.VERTICAL));

        //evento de clique
        recyclerViewListaResumos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewListaResumos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Lembrete resumo = resumos.get(position);
                                Intent intent = new Intent(DisciplinaSelecionadaActivity.this, AtualizarResumoActivity.class);
                                intent.putExtra("resumo", resumo);
                                intent.putExtra("disciplina", disciplina);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) {

                            }

                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                            }
                        }
                )
        );

        swipe();
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for ( int permissaoResultado : grantResults ){
            if ( permissaoResultado == PackageManager.PERMISSION_DENIED ){
                alertaValidacaoPermissao();
            }
        }

    }

    private void alertaValidacaoPermissao(){

        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Permissao.validarPermissoes(permissoesNecessarias, DisciplinaSelecionadaActivity.this, 1);
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_com_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.home){
            try{
                item.setEnabled(false);
                try {
                    Intent intent = new Intent(DisciplinaSelecionadaActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                item.setEnabled(true);

            }catch (Exception e){
                e.printStackTrace();
                item.setEnabled(true);

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bitmap bitmap = null;
        if ( resultCode == RESULT_OK ){
            try {
                switch ( requestCode ) {
                    case SELECAO_CAMERA:
                        File file = new File(currentPhotoPath);
                        bitmap = MediaStore.Images.Media
                                .getBitmap(getContentResolver(), Uri.fromFile(file));
                        break;
                }

                if (bitmap != null){
                    String filename = "bitmap.jpeg";
                    FileOutputStream stream = this.openFileOutput(filename, Context.MODE_PRIVATE);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, stream);

                    stream.close();
                    bitmap.recycle();


                    Intent intent = new Intent(DisciplinaSelecionadaActivity.this, AdicionarResumoComFotoActivity.class);
                    intent.putExtra("disciplina", disciplina);
                    intent.putExtra("imagem", filename);
                    startActivity(intent);

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void adicionarResumo(View view){
        Intent intent = new Intent(this, AdicionarResumoActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void adicionarFalta(View view){
        Intent intent = new Intent(this, AdicionarFaltasActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void adicionarNota(View view){
        Intent intent = new Intent(this, AdicionarNotaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void abrirNotas(View view){
        Intent intent = new Intent(this, NotasPorDisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void abrirFaltas(View view){
        Intent intent = new Intent(this, FaltasPorDisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void abrirLembretesDisciplina(View view){
        Intent intent = new Intent(this, LembretesDisciplinaActivity.class);
        intent.putExtra("disciplina", disciplina);
        startActivity(intent);
    }

    public void capturarImagem(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.williantaiguara.anota.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, SELECAO_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "image";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public void contarQtdFaltas(){
        Query query = firebaseRef.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("faltas").orderByChild("data");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int total = 0;
                for (DataSnapshot qtd: dataSnapshot.getChildren()){
                    Falta qtdFalta = qtd.getValue(Falta.class);
                    total += Integer.valueOf(qtdFalta.getQtd());
                    btTotalFaltas.setText("Faltas: " + total);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void contarNotas(){
        Query query = firebaseRef.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("notas").orderByChild("data");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double total = 0;
                for (DataSnapshot qtd: dataSnapshot.getChildren()){
                    Nota totalNota = qtd.getValue(Nota.class);
                    total += totalNota.getNota();
                }
                btTotalNotas.setText("Nota: " + Math.round(total* 100.0 ) / 100.0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void recuperarResumos(){

        Query query = firebaseRef.child("usuarios")
                                .child(idUsuario)
                                .child("cursos")
                                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                                .child("resumos").orderByChild("data");
        valueEventListenerResumos = query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                resumos.clear();

                for (DataSnapshot dados: dataSnapshot.getChildren()){
                    Lembrete resumo = dados.getValue(Lembrete.class);
                    resumo.setKey(dados.getKey());
                    resumos.add(resumo);
                }
                if (recyclerViewListaResumos.getAdapter().getItemCount() != 0) {
                    recyclerViewListaResumos.smoothScrollToPosition(recyclerViewListaResumos.getAdapter().getItemCount() - 1);
                }
                adapterListaResumos.notifyDataSetChanged();
                //ProgressBarCustom.closeProgressBar(progressBar);
                //nenhumLembreteEncontrado();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void excluirResumo(final RecyclerView.ViewHolder viewHolder){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        alertDialog.setTitle("Excluir Resumo:");
        alertDialog.setMessage("Tem certeza que deseja excluir esse resumo?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int position = viewHolder.getAdapterPosition();
                resumo = resumos.get(position);

                String emailUsuario = autenticacao.getCurrentUser().getEmail();
                String idUsuario = Base64Custom.CodificarBase64(emailUsuario);
                resumosRef = firebaseRef.child("usuarios")
                        .child(idUsuario)
                        .child("cursos")
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                        .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                        .child("resumos");
                resumosRef.child(resumo.getKey()).removeValue();
                Toast.makeText(getApplicationContext(), "Exclusão Confirmada!", Toast.LENGTH_SHORT).show();
                adapterListaResumos.notifyItemRemoved(position);

            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(DisciplinaSelecionadaActivity.this, "Exclusão Cancelada!", Toast.LENGTH_SHORT).show();
                adapterListaResumos.notifyDataSetChanged();

            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();

    }

    public void swipe(){

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swypeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swypeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                excluirResumo(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerViewListaResumos);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarResumos();
        contarQtdFaltas();
        contarNotas();
    }

    @Override
    protected void onStop() {
        super.onStop();
        firebaseRef.removeEventListener(valueEventListenerResumos);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        this.recreate();
    }
}
