package com.williantaiguara.anota.model;

public class Curso {

    private String nomeDoCurso;
    private String key;
    private String semestre;

    public Curso() {
    }

    public String getNomeDoCurso() {
        return nomeDoCurso;
    }

    public void setNomeDoCurso(String nomeDoCurso) {
        this.nomeDoCurso = nomeDoCurso;
    }

    public String getKey() {
        return key;
    }

    public String getSemestre() {
        return semestre;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
