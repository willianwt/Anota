package com.williantaiguara.anota.model;

public class Disciplina {

    private String nomeDisciplina;
    private String nomeProfessorDisciplina;
    private String emailProfessorDisciplina;

    public Disciplina() {
    }

    public Disciplina(String nomeDisciplina, String nomeProfessorDisciplina, String emailProfessorDisciplina) {
        this.nomeDisciplina = nomeDisciplina;
        this.nomeProfessorDisciplina = nomeProfessorDisciplina;
        this.emailProfessorDisciplina = emailProfessorDisciplina;
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
