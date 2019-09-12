package com.williantaiguara.anota.helper;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateCustom {

    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }

    //pega a data selecionada no calendario
    public static String dataSelecionada(Date date){
        String myFormat = "yyyy-MM-dd"; //formato da data
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("pt","BR"));
        return sdf.format(date);
    }

    //utilizado para converter a data do tipo yyyy-mm-dd para dd-MM-yyyy
    public static String formataData(String data){
        List<String> dataSeparada = Arrays.asList(data.split("-"));
        Collections.reverse(dataSeparada);
        String dataFormatada = TextUtils.join("-", dataSeparada);
        return dataFormatada;
    }
}
