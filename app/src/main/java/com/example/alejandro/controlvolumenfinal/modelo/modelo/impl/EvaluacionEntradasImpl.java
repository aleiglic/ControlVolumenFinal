package com.example.alejandro.controlvolumenfinal.modelo.modelo.impl;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.example.alejandro.controlvolumenfinal.constantes.EventEnum;
import com.example.alejandro.controlvolumenfinal.modelo.ComunicacionTV;
import com.example.alejandro.controlvolumenfinal.modelo.EvaluacionEntradas;
import com.example.alejandro.controlvolumenfinal.receiver.EEReceiver;
import com.example.alejandro.controlvolumenfinal.services.VolumenService;

import java.util.ArrayList;
import java.util.List;

import static com.example.alejandro.controlvolumenfinal.constantes.ValoresDefecto.LIMITE_SNR;
import static com.example.alejandro.controlvolumenfinal.constantes.ValoresDefecto.N;
import static com.example.alejandro.controlvolumenfinal.constantes.ValoresDefecto.RANGO_VOLUMEN;
import static com.example.alejandro.controlvolumenfinal.constantes.ValoresDefecto.VOLUMEN_AMBIENTE;

/**
 * Created by Alejandro on 6/2/2017.
 */
public class EvaluacionEntradasImpl implements EvaluacionEntradas {

    private Double volumenDeseado;
    private String idModelo;
    private VolumenService vService;
    private boolean mBound;
    private Context ctx;
    private List<Double> valoresVolumen;
    private ComunicacionTV comTV;
    private Integer cont = 0;

    private BroadcastReceiver mMessageReceiver = null;
    private ServiceConnection mConnection = null;



    private ServiceConnection getConnection(){
        return new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName className,
                                           IBinder service) {
                // We've bound to LocalService, cast the IBinder and get LocalService instance
                VolumenService.LocalBinder binder = (VolumenService.LocalBinder) service;
                vService = binder.getService();
                vService.start();
                mBound = true;
                Log.d("Bound", "Servicio enlazado");
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                mBound = false;
                vService.stop();
                vService = null;
            }
        };
    }

    @Override
    public void iniciar(Context ctx) {
        this.ctx = ctx;
        //this.mMessageReceiver = new EEReceiver(ctx);
        this.mConnection = getConnection();
        this.mMessageReceiver = new EEReceiver(ctx, this);
        Intent intent = new Intent(this.ctx, VolumenService.class);
        this.ctx.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        LocalBroadcastManager.getInstance(this.ctx).registerReceiver(mMessageReceiver,
                new IntentFilter(EventEnum.EVENT_LOG));
        this.valoresVolumen = new ArrayList<Double>();

        //Inicializar la lista con el valor del volumen deseado
        for(int i = 0; i < N; i++)
            valoresVolumen.add(volumenDeseado);

        this.comTV = new ComunicacionTVImpl(ctx);
        Log.d("EvaluacionEntradas", "inicio");
    }

    @Override
    public void parar() {
        this.vService.stop();
        this.ctx.unbindService(mConnection);
        this.mConnection = null;
        LocalBroadcastManager.getInstance(this.ctx).unregisterReceiver(mMessageReceiver);
        this.mMessageReceiver = null;
        Log.d("", "paró");
    }


    @Override
    public void setVolumenDeseado(Double volumen) {
        this.volumenDeseado = volumen;
    }

    @Override
    public void setModelo(String idModelo) {
        this.idModelo = idModelo;
    }

    @Override
    public void actualizarVolumen(Double volumenActual) {
        //Inserto el nuevo valor de volumen al final de la lista
        // y despues elimino el primer elemento, quedando en N
        this.valoresVolumen.add(N, volumenActual);
        this.valoresVolumen.remove(0);

        Double snr = calcularSNR();
        nivelarVolumen(volumenActual, snr);
        Log.d("EvaluacionEntradas", "ActualizarVolumen");
    }


    private Double calcularSNR(){
        double senial = 0, ruido = 0, sumaSenial = 0, sumaRuido = 0;
        for (Double vol : valoresVolumen)
            sumaSenial += vol;
        senial = sumaSenial / N;

        for (Double vol : valoresVolumen)
            sumaRuido += Math.pow(vol - VOLUMEN_AMBIENTE, 2);
        ruido = Math.sqrt(sumaRuido / (N - 1));

        Log.d("EvaluacionEntradas", "ActualizarVolumen");
        return senial / ruido;
    }

    private void nivelarVolumen(Double volumen, Double snr){
        Log.d("NIVELANDO VOLUMEN: ", snr + "");

        cont++;
        if (volumen > volumenDeseado + RANGO_VOLUMEN) {

            Log.d("ESTOY EN: ", "CONT = " + cont);
            if (cont >= N) {
                if (snr < LIMITE_SNR) {
                    Log.d("EN:", "Transmite");
                    cambiarVolumen(-1);
                }
                cont = 0;
            }
        }
        if (volumen < volumenDeseado - RANGO_VOLUMEN) {
            if (cont >= N) {
                if (snr < LIMITE_SNR) {
                    Log.d("EN:", "Transmite");
                    cambiarVolumen(1);
                }
                cont = 0;
            }
        }
    }

    private void cambiarVolumen(int direccion){
        Log.d("EvaluacionEntradas", "CambiarVolumen " + direccion);
        try {
            if (direccion >= 0) {
                this.comTV.enviarSenial(true, this.idModelo);
                this.comTV.enviarSenial(true, this.idModelo);
                this.comTV.enviarSenial(true, this.idModelo);
            } else {
                this.comTV.enviarSenial(false, this.idModelo);
                this.comTV.enviarSenial(false, this.idModelo);
                this.comTV.enviarSenial(false, this.idModelo);
            }
        }catch(Exception e){
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);
            e.printStackTrace();
        }
    }


}
