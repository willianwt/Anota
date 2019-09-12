package com.williantaiguara.anota.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;

import java.io.Serializable;

public class Falta implements Serializable {

    private String data;
    private String qtd;
    private String key;
    private transient FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private transient DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
    private transient String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());

    public Falta() {
    }

    public void salvarFalta(String curso, String semestre, String disciplina){
            firebase.child("usuarios")
                    .child(idUsuario)
                    .child("cursos")
                    .child(curso)
                    .child(semestre)
                    .child(disciplina)
                    .child("faltas")
                    .push()
                    .setValue(this);
    }

    public void  atualizarFalta(String curso, String semestre, String disciplina){

        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(curso)
                .child(semestre)
                .child(disciplina)
                .child("faltas")
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

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getQtd() {
        return qtd;
    }

    public void setQtd(String qtd) {
        this.qtd = qtd;
    }
}
