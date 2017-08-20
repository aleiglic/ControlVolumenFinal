package com.example.alejandro.controlvolumenfinal.receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.alejandro.controlvolumenfinal.R;
import com.example.alejandro.controlvolumenfinal.constantes.EventEnum;
import com.example.alejandro.controlvolumenfinal.main.MainActivity;
import com.example.alejandro.controlvolumenfinal.modelo.EvaluacionEntradas;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by Alejandro on 19/2/2017.
 */

public class EEReceiver extends BroadcastReceiver {

    private Context actContext;
    private EvaluacionEntradas eEntradas;

    public EEReceiver(){}

    public EEReceiver(Context ctx, EvaluacionEntradas eEntradas){
        this.actContext = ctx;
        this.eEntradas = eEntradas;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        double valorActual = intent.getDoubleExtra(EventEnum.EVENT_LOG, 0L);
        double valorAmplitud = intent.getDoubleExtra(EventEnum.EVENT_AMP, 0L);

        DecimalFormat df = new DecimalFormat("###.#");
        df.setRoundingMode(RoundingMode.CEILING);
        String valorActualS = df.format(valorActual);

        TextView textVolumen = ((TextView) ((Activity) actContext).findViewById(R.id.volumenActual));
        textVolumen.setText(valorActualS);
        eEntradas.actualizarVolumen(valorActual);

        Log.d("EvaluacionEntradasImpl", "Valor recibido "+ valorActual);
    }
}
