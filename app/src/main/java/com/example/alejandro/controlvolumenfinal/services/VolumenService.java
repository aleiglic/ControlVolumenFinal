package com.example.alejandro.controlvolumenfinal.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.example.alejandro.controlvolumenfinal.constantes.EventEnum;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import static com.example.alejandro.controlvolumenfinal.constantes.ValoresDefecto.PERIODO_MUESTREO;

public class VolumenService extends Service {

    private Timer mTimer1;
    private TimerTask mTt1;
    private Handler mTimerHandler = new Handler();
    private LocalBroadcastManager manager;
    private MediaRecorder recorder;
    private Double amplitud;

    private final IBinder mBinder = new LocalBinder();


    public VolumenService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        manager = LocalBroadcastManager.getInstance(this);
        return mBinder;
    }

    public void start(){
        Log.d("Start", "Start");
        this.startTimer();
        Log.d(this.getPackageResourcePath(), "enlazado");
    }

    public void stop() {
        this.stopTimer();
    }

    private void startTimer(){
        if(mTimer1 == null && mTt1 == null) {

            mTimer1 = new Timer();
            mTt1 = new TimerTask() {
                public void run() {
                    mTimerHandler.post(new Runnable() {
                        public void run() {
                            try {
                                createMediaRecorder();
                            }catch(Exception e){
                                e.printStackTrace();
                            }
                            double amplitude = getAmplitude();
                            double log = getLog(amplitude);
                            Intent intent = new Intent(EventEnum.EVENT_LOG.toString());
                            intent.putExtra(EventEnum.EVENT_AMP, amplitude);
                            intent.putExtra(EventEnum.EVENT_LOG, log);
                            manager.sendBroadcast(intent);
                            Log.d("Timer", "Ciclo completado"+ manager.hashCode());

                        }
                    });
                }
            };
            mTimer1.schedule(mTt1, 1, PERIODO_MUESTREO);

        }
    }

    private void stopTimer(){
        if(mTimer1 != null){
            stopMediaRecorder();
            mTimer1.cancel();
            mTimer1.purge();
            mTt1.cancel();
            mTimer1 = null;
            mTt1 = null;
        }
    }

    private void createMediaRecorder() throws IOException{
        if(recorder == null) {
            recorder = new MediaRecorder();
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            recorder.setOutputFile("/dev/null");
            recorder.prepare();
            recorder.start();
        }
    }

    private double getAmplitude(){
        return recorder.getMaxAmplitude();
    }

    private double getLog(double amplitude){
        return (20 * Math.log10((double)Math.abs(amplitude)));
    }

    private void stopMediaRecorder(){
        if (recorder != null) {
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    public class LocalBinder extends Binder {
        public VolumenService getService() {
            // Return this instance of LocalService so clients can call public methods
            return VolumenService.this;
        }
    }
}
