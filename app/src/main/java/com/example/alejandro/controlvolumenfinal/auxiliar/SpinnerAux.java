package com.example.alejandro.controlvolumenfinal.auxiliar;

import android.content.Context;
import android.widget.Spinner;

import com.example.alejandro.controlvolumenfinal.R;

import java.util.HashMap;

/**
 * Created by Alejandro on 13/12/2016.
 */

public class SpinnerAux {

    private HashMap<String, Integer> spinnerMap;
    private static SpinnerAux instance;
    private Context context;

    private SpinnerAux(){
        this.llenadoArray();
    }

    private void setContext(Context context){
        this.context = context;
    }

    public static SpinnerAux getInstance(Context context) {
        if(instance == null)
            instance = new SpinnerAux();
        instance.setContext(context);
        return instance;
    }

    private String[] volumenes = volumenes();

    private String[] llaves;


    private Integer[] valores = valores();

    private void llenadoArray(){
        spinnerMap = new HashMap<String, Integer>();
        for (int i = 0; i < volumenes.length; i++)
            spinnerMap.put(volumenes[i], valores[i]);

    }

    public Integer getValor(String volumen){
        Integer valor = spinnerMap.get(volumen);
        return valor != null ? valor : 0;
    }

    private void llenarLlaves(){

    }

    private String[] volumenes(){
        return context.getResources().getStringArray(R.array.volumenes_valores_array);
    }

    private Integer[] valores(){
        String[] valoresS = context.getResources().getStringArray(R.array.volumenes_array);
        Integer[] valoresI = new Integer[valoresS.length];
        for(int i = 0; i < valoresS.length; i++)
            valoresI[i] = Integer.parseInt(valoresS[i]);
        return valoresI;
    }
}
