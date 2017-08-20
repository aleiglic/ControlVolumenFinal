package com.example.alejandro.controlvolumenfinal.entities;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Alejandro on 27/12/2016.
 */

@Root(name = "ListaModelos")
public class ListaModelos implements Serializable, Iterable<ModeloTV>{

    @ElementList(inline = true, required = false)
    private List<ModeloTV> modeloTVList;

    public ListaModelos() {
        this.modeloTVList = new ArrayList<ModeloTV>();
    }

    public void addModelo(ModeloTV modeloTV){
        this.modeloTVList.add(modeloTV);
    }

    public ModeloTV getModelo(int ord){
        return this.modeloTVList.get(ord);
    }

    public ModeloTV getModelo(String idModelo) throws Exception {
        ModeloTV buscado = null;
        for(ModeloTV modeloTV: this.modeloTVList){
            if(modeloTV.getIdModelo().equals(idModelo)) {
                buscado = modeloTV;
                break;
            }
        }
        if(buscado == null)
            throw new Exception("ListaModelos: ID del modelo no encontrado");
        return buscado;
    }

    @Override
    public Iterator iterator() {
        return modeloTVList.iterator();
    }
}
