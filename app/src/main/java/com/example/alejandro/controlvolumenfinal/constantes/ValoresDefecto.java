package com.example.alejandro.controlvolumenfinal.constantes;

/**
 * Created by Alejandro on 8/2/2017.
 */

public class ValoresDefecto {
    public static final Double VOLUMEN_DEFECTO = 60.0;
    public static final String MODELO_DEFECTO = "RCA";
    public static final Integer N = 10;
    public static final Double VOLUMEN_AMBIENTE = 60.0;
    public static final Double LIMITE_SNR = 10.0;           //Límite en el índice de SNR debajo del cual se permite cambiar el volumen
    public static final Integer PERIODO_MUESTREO = 1000;    //Intervalo en ms entre cada muestra de volumen
    public static final Double RANGO_VOLUMEN = 5.0;         //Rango en dB alrededor del volumen ideal elegido
}
