package com.williantaiguara.anota.model;

public class Ajuda {

    private String pergunta;
    private String resposta;
    private boolean expandido;

    public Ajuda(String pergunta, String resposta) {
        this.pergunta = pergunta;
        this.resposta = resposta;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getResposta() {
        return resposta;
    }

    public void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public boolean isExpandido() {
        return expandido;
    }

    public void setExpandido(boolean expandido) {
        this.expandido = expandido;
    }
}
