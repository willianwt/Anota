package com.williantaiguara.anota.helper;

public class FormatadorDeCaracteresIniciais {

    /*
    Esta classe foi criada para remover e adicionar 2 caracteres, dependendo do tipo, para evitar problemas com o banco de dados.
    EX: caso um curso comece com número, pode gerar problema na hora de listar os itens.
     */

    public static String remove2char(String s){
        //remove os 2 primeiros caracteres da string, no caso, remove os "C:"/"D:"/"S:" para listar no aplicativo.
        return s.substring(2);
    }

    public static String adicionaCharParaCurso(String s){
        return "C:"+s;
    }

    public static String adicionaCharParaDisciplina(String s){
        return "D:"+s;
    }
    public static String adicionaCharParaSemestre(String s){
        return "S:"+s;
    }
}
