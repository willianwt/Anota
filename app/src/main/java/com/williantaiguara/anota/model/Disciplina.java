package com.williantaiguara.anota.model;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.williantaiguara.anota.config.ConfiguracaoFirebase;
import com.williantaiguara.anota.helper.Base64Custom;

public class Disciplina {

    private String nomeCurso;
    private String semestreCurso;
    private String nomeDisciplina;
    private String nomeProfessorDisciplina;
    private String emailProfessorDisciplina;

    public Disciplina() {
    }

    public void salvarCurso(String nomeCurso, String semestreCurso){
        FirebaseAuth autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        DatabaseReference firebase = ConfiguracaoFirebase.getFirebaseDatabase();
        String idUsuario = Base64Custom.CodificarBase64(autenticacao.getCurrentUser().getEmail());
        firebase.child("usuarios")
                .child(idUsuario)
                .child("cursos")
                .child(nomeCurso)
                .child(semestreCurso)
                .push()
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
}
