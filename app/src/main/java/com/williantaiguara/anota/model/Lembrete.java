package com.williantaiguara.anota.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;

import java.io.Serializable;

/*
    Lembretes e resumos possuem os mesmos dados, logo a mesma classe foi utilizada para ambos, com a diferença apenas no
    método de salvar e atualizar.

 */

public class Lembrete implements Serializable {

    private String titulo;
    private String conteudo;
    private String data;
    private String key;
    private String imagem;
    private String tipo;
    private transient FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
    private transient DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
    private transient String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());

    public Lembrete() {
    }

    public void salvarLembrete(){
        firebase.child("usuarios")
                .child(idUsuario)
                .child("lembretes")
                .push()
                .setValue(this);
    }

    public void salvarLembreteDisciplina(Disciplina disciplina){
        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("lembretes")
                .push()
                .setValue(this);
    }

    public void atualizarLembrete(){

        firebase.child("usuarios")
                .child(idUsuario)
                .child("lembretes")
                .child(key)
                .setValue(this);
    }

    public void atualizarLembreteDisciplina(Disciplina disciplina){

        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(disciplina.getNomeCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getSemestreCurso()))
                .child(Base64Custom.CodificarBase64(disciplina.getNomeDisciplina()))
                .child("lembretes")
                .child(key)
                .setValue(this);
    }

    public void salvarResumo(String curso, String semestre, String disciplina){

        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(curso)
                .child(semestre)
                .child(disciplina)
                .child("resumos")
                .push()
                .setValue(this);
    }

    public void  atualizarResumo(String curso, String semestre, String disciplina){

        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(curso)
                .child(semestre)
                .child(disciplina)
                .child("resumos")
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

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
