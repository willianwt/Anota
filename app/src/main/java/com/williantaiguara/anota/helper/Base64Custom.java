package com.williantaiguara.anota.helper;

import android.util.Base64;

public class Base64Custom {

    public static String CodificarBase64(String email){
        return Base64.encodeToString(email.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String DecodificarBase64(String emailCodificado){
        return new String(Base64.decode(emailCodificado, Base64.DEFAULT));
    }
}
