package com.example.alejandro.controlvolumenfinal.entities;




import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Alejandro on 14/12/2016.
 */

@Root(name = "ModeloTV")
public class ModeloTV implements Serializable, Comparable<ModeloTV>{

    public ModeloTV(){}

    @Element(name = "IDModelo")
    private String idModelo;

    public String getIdModelo() {
        return idModelo;
    }

    public void setIdModelo(String idModelo) {
        this.idModelo = idModelo;
    }

    @Element(name = "SenialSubirVolumen")
    private String senialSubirVolumen;

    public String getSenialSubirVolumen() {
        return senialSubirVolumen;
    }

    public void setSenialSubirVolumen(String senialSubirVolumen) {
        this.senialSubirVolumen = senialSubirVolumen;
    }

    @Element(name = "SenialBajarVolumen")
    private String senialBajarVolumen;

    public String getSenialBajarVolumen() {
        return senialBajarVolumen;
    }

    public void setSenialBajarVolumen(String senialBajarVolumen) {
        this.senialBajarVolumen = senialBajarVolumen;
    }

    @Element(name = "Frecuencia")
    public Integer frecuencia;

    public Integer getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(Integer frecuencia) {
        this.frecuencia = frecuencia;
    }

    public int [] enterosSenialSubirVolumen(){
        return stringToInt(this.senialSubirVolumen);
    }

    public int [] enterosSenialBajarVolumen(){
        return stringToInt(this.senialBajarVolumen);
    }

    private int [] stringToInt(String senial){
        List<String> stringList = Arrays.asList(senial.split(","));
        int[] arrayNumeros = new int[stringList.size()];
        int i = 0;
        for(String numero: stringList) {
            numero = numero.trim();
            arrayNumeros[i++] = Integer.parseInt(numero);
        }
        return arrayNumeros;
    }


    @Override
    public int compareTo(ModeloTV another) {
        return this.getIdModelo().compareTo(another.getIdModelo());
    }
}
