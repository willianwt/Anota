package com.williantaiguara.anota.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;

import java.io.Serializable;

public class Nota implements Serializable {

    private String titulo;
    private String data;
    private double nota;
    private String key;
    private transient FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private transient DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
    private transient String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());

    public Nota() {
    }

    public void salvarNota(String curso, String semestre, String disciplina){
        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(curso)
                .child(semestre)
                .child(disciplina)
                .child("notas")
                .push()
                .setValue(this);
    }

    public void atualizarNota(String curso, String semestre, String disciplina){
        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(curso)
                .child(semestre)
                .child(disciplina)
                .child("notas")
                .child(key)
                .setValue(this);
    }

    @Exclude
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
