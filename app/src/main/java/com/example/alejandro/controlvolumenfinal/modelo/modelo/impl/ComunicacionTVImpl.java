package com.example.alejandro.controlvolumenfinal.modelo.modelo.impl;

import android.content.Context;
import android.util.Log;

import com.example.alejandro.controlvolumenfinal.R;
import com.example.alejandro.controlvolumenfinal.modelo.ComunicacionTV;
import com.example.alejandro.controlvolumenfinal.modelo.ConsumerIRManager;
import com.example.alejandro.controlvolumenfinal.entities.ListaModelos;
import com.example.alejandro.controlvolumenfinal.entities.ModeloTV;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alejandro on 6/2/2017.
 */
public class ComunicacionTVImpl implements ComunicacionTV {

    private Context ctx;
    private ConsumerIRManager consumerIRManager;
    private ListaModelos listaModelos;

    public ComunicacionTVImpl(Context ctx){
        this.consumerIRManager = new ConsumerIRManagerImpl(ctx);
        this.ctx = ctx;
        InputStream is = this.ctx.getResources().openRawResource(R.raw.seniales);
        Serializer ser = new Persister();
        try {
            this.listaModelos = ser.read(ListaModelos.class, is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("ComunicacionTVImpl", ""+listaModelos.getModelo(0).getIdModelo());
    }

    @Override
    public void enviarSenial(Boolean subir, String idModelo) throws Exception{
        ModeloTV modeloTV = buscarModelo(idModelo);
        String senial;
        if(subir) {
            consumerIRManager.transmit(modeloTV.getFrecuencia(), modeloTV.enterosSenialSubirVolumen());
        }else{
            consumerIRManager.transmit(modeloTV.getFrecuencia(), modeloTV.enterosSenialBajarVolumen());
        }
        Log.d("ComunicacinTV", "EnviarSeñal: " + modeloTV.getSenialSubirVolumen());
    }

    private ModeloTV buscarModelo(String idModelo) throws Exception{
        return listaModelos.getModelo(idModelo);
    }
}
