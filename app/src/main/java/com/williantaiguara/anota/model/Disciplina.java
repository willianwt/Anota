package com.williantaiguara.anota.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;

import java.io.Serializable;

public class Disciplina implements Serializable {
/*
    todo: pensar num nome melhor que envolva curso e disciplina.
    todo: o nome do curso não deve iniciar com número. criar um método para prevenir isso.
 */


    private String nomeCurso;
    private String semestreCurso;
    private String nomeDisciplina;
    private String nomeProfessorDisciplina;
    private String emailProfessorDisciplina;
    private String key;

    public Disciplina() {
    }

    public void salvarCurso(String nomeCurso, String semestreCurso){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());
        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(Base64Custom.CodificarBase64(nomeCurso))
                .child(Base64Custom.CodificarBase64(semestreCurso))
                .child(Base64Custom.CodificarBase64(nomeDisciplina))
                .setValue(this);
    }

    public Disciplina(String nomeDisciplina, String nomeProfessorDisciplina, String emailProfessorDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessorDisciplina = nomeProfessorDisciplina;
        this.emailProfessorDisciplina = emailProfessorDisciplina;
    }
    @Exclude
    public String getNomeCurso() {
        return nomeCurso;
    }

    public void setNomeCurso(String nomeCurso) {
        this.nomeCurso = nomeCurso;
    }
    @Exclude
    public String getSemestreCurso() {
        return semestreCurso;
    }

    public void setSemestreCurso(String semestreCurso) {
        this.semestreCurso = semestreCurso;
    }

    public String getNomeDisciplina() {
        return nomeDisciplina;
    }

    public void setNomeDisciplina(String nomeDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
    }

    public String getNomeProfessorDisciplina() {
        return nomeProfessorDisciplina;
    }

    public void setNomeProfessorDisciplina(String nomeProfessorDisciplina) {
        this.nomeProfessorDisciplina = nomeProfessorDisciplina;
    }

    public String getEmailProfessorDisciplina() {
        return emailProfessorDisciplina;
    }

    public void setEmailProfessorDisciplina(String emailProfessorDisciplina) {
        this.emailProfessorDisciplina = emailProfessorDisciplina;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
