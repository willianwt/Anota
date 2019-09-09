package com.williantaiguara.anota.model;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateCustom {

    public static String dataAtual(){
        long data = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-YYYY", Locale.getDefault());
        String dataString = simpleDateFormat.format(data);
        return dataString;
    }
}
