package br.com.iecapoeira.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by rafae on 08/08/2017.
 */
@ParseClassName("Parceiro")
public class Parceiro  extends ParseObject {

    public static final String NAME = "name";
    public static final String FOTO = "foto";
    public static final String PART = "patrocinador";
    public static final  String PARC ="parceiros";


    public Parceiro() {

    }


    public String getName() {
        return get(NAME).toString();
    }

    public void setName(String name) {
        put(NAME,name);
    }

    public String getFoto() {
        return get(FOTO).toString();
    }

    public void setFoto(String foto) {
        put(FOTO,foto);
    }

}
