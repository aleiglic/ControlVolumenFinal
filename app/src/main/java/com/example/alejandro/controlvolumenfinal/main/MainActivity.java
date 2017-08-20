package com.example.alejandro.controlvolumenfinal.main;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.alejandro.controlvolumenfinal.R;
import com.example.alejandro.controlvolumenfinal.constantes.ValoresDefecto;
import com.example.alejandro.controlvolumenfinal.modelo.EvaluacionEntradas;
import com.example.alejandro.controlvolumenfinal.entities.ListaModelos;
import com.example.alejandro.controlvolumenfinal.entities.ModeloTV;
import com.example.alejandro.controlvolumenfinal.modelo.modelo.impl.EvaluacionEntradasImpl;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends Activity {

    private static final String LOGTAG = "LOG";
    private HashMap<String, Integer> spinnerMap;
    private Double volumen = ValoresDefecto.VOLUMEN_DEFECTO;
    private String idModelo = ValoresDefecto.MODELO_DEFECTO;
    private EvaluacionEntradas eEntradas;
    private boolean started = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getActionBar().setTitle(this.getResources().getString(R.string.action_bar_title));
        setContentView(R.layout.activity_main);
        setPagina();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(eEntradas != null)
            this.eEntradas.parar();
    }

    private void setPagina() {
        Spinner volumenesSpinner = (Spinner) findViewById(R.id.spinnerVolumen);


        llenarModelos();
        llenarSpinnerVolumen();
        llenarSpinnerModelos();
    }

    private void llenarModelos() {
        InputStream is = this.getResources().openRawResource(R.raw.seniales);
        Serializer ser = new Persister();
        ListaModelos lista = null;
        try {
            lista = ser.read(ListaModelos.class, is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> modelos = new ArrayList<String>();

        for (ModeloTV modeloTV: lista) {
            modelos.add(modeloTV.getIdModelo());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, modelos);

        ((Spinner)this.findViewById(R.id.spinnerModelo)).setAdapter(adapter);
    }

    private void llenarSpinnerVolumen(){
        Spinner volumenesSpinner = (Spinner) findViewById(R.id.spinnerVolumen);
        Integer[] valores = valores();
        String[] volumenes = this.getResources().getStringArray(R.array.volumenes_array);

        spinnerMap = new HashMap<String, Integer>();
        for (int i = 0; i < volumenes.length; i++)
            spinnerMap.put(volumenes[i], valores[i]);
        volumenesSpinner.setOnItemSelectedListener(new SpinnerVolumenActivity());
    }

    private void llenarSpinnerModelos(){
        Spinner modelosSpinner = (Spinner) findViewById(R.id.spinnerModelo);

        InputStream is = this.getResources().openRawResource(R.raw.seniales);
        Serializer ser = new Persister();
        ListaModelos lista = null;
        try {
            lista = ser.read(ListaModelos.class, is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<String> modelos = new ArrayList<String>();

        for (ModeloTV modeloTV: lista) {
            modelos.add(modeloTV.getIdModelo());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, modelos);

        modelosSpinner.setAdapter(adapter);
        modelosSpinner.setOnItemSelectedListener(new SpinnerModelosActivity());
    }

    private Integer[] valores(){
        String[] valoresS = this.getResources().getStringArray(R.array.volumenes_valores_array);
        Integer[] valoresI = new Integer[valoresS.length];
        for(int i = 0; i < valoresS.length; i++)
            valoresI[i] = Integer.parseInt(valoresS[i]);
        return valoresI;
    }

    public Integer getValor(String volumen){
        Integer valor = spinnerMap.get(volumen);
        return valor != null ? valor : 0;
    }

    public void onStartClick(View v) {
        Log.d("MainActivity", "Started = " + started);
        if (!started) {
            if(eEntradas == null)
                eEntradas = new EvaluacionEntradasImpl();
            eEntradas.setVolumenDeseado(this.volumen);
            eEntradas.setModelo(this.idModelo);
            eEntradas.iniciar(this);


            started = true;
            Log.d("MainActivity", "Inicio");
        }else{
            Log.d("MainActivity", "Stop");
            eEntradas.parar();
            started = false;
        }
    }

    public void setVolumen(Double volumen){
        this.volumen = volumen;
        if(eEntradas != null)
            this.eEntradas.setVolumenDeseado(this.volumen);
        Log.d("MainActivity", "seteo volumen");
    }

    public void setIdModelo(String idModelo){
        this.idModelo = idModelo;
        if(eEntradas != null)
            this.eEntradas.setModelo(idModelo);

    }


    private class SpinnerVolumenActivity extends Activity implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Integer valor = 0;
            Spinner spinner = (Spinner) parent;
            String volumen = (String) spinner.getItemAtPosition(position);
            valor = getValor(volumen);
            ((MainActivity)parent.getContext()).setVolumen(valor.doubleValue());

            Toast.makeText(parent.getContext(), valor + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }

    private class SpinnerModelosActivity extends Activity implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            Spinner spinner = (Spinner) parent;
            String modelo = (String) spinner.getItemAtPosition(position);
            ((MainActivity)parent.getContext()).setIdModelo(modelo);

            Toast.makeText(parent.getContext(), modelo + "", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }

    }


}
