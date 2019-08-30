package com.williantaiguara.anota.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;

import java.io.Serializable;

public class Lembrete implements Serializable {

    private String titulo;
    private String conteudo;
    private String data;
    private String key;

    public Lembrete() {
    }

    public void salvar(){

        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());
        firebase.child("lembretes")
                .child(idUsuario)
                .push()
                .setValue(this);
    }

    public void atualizar(){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());
        firebase.child("lembretes")
                .child(idUsuario)
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

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


}
