package com.example.alejandro.controlvolumenfinal.modelo;

import android.content.BroadcastReceiver;
import android.content.Context;

/**
 * Created by Alejandro on 5/2/2017.
 */

public interface EvaluacionEntradas {
    void iniciar(Context ctx);
    void parar();
    void setVolumenDeseado(Double volumen);
    void setModelo(String idModelo);
    void actualizarVolumen(Double volumen);
}
