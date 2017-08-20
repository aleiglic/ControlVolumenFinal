package com.example.alejandro.controlvolumenfinal.modelo.modelo.impl;

import android.content.Context;
import android.hardware.ConsumerIrManager;
import android.util.Log;

import com.example.alejandro.controlvolumenfinal.modelo.ConsumerIRManager;

import static android.content.Context.CONSUMER_IR_SERVICE;

/**
 * Created by Alejandro on 6/2/2017.
 */
public class ConsumerIRManagerImpl implements ConsumerIRManager {

    private ConsumerIrManager irService;

    public ConsumerIRManagerImpl(Context ctx){
        irService = (ConsumerIrManager) ctx.getSystemService(CONSUMER_IR_SERVICE);
    }

    @Override
    public void transmit(int frequency, int[] signal) {
        try {
            irService.transmit(frequency, signal);
        } catch (Exception e) {
            Log.d("ERROR", "ERROR EN LA TRANSMISION IR");
            e.printStackTrace();
        }
    }
}
