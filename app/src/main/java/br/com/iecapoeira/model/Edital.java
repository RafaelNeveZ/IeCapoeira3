package br.com.iecapoeira.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Created by rafae on 25/07/2017.
 */
@ParseClassName("Edital")
public class Edital extends ParseObject{
    private String titulo;
    private String link;
    public static final String TITLE = "titulo";
    public static final String LINK = "link";

    public Edital(){}
    public Edital(String t, String l){
        titulo = t;
        link = l;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLInk() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
