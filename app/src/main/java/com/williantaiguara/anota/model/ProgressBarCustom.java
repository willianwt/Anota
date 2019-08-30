package com.williantaiguara.anota.model;

import android.view.View;
import android.widget.ProgressBar;

public class ProgressBarCustom {

    public static void openProgressBar(ProgressBar progressBar){
        progressBar.setVisibility( View.VISIBLE );
    }

    public static void closeProgressBar(ProgressBar progressBar){
        progressBar.setVisibility( View.GONE );
    }
}
