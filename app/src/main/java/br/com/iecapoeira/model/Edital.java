package br.com.iecapoeira.model;

/**
 * Created by rafae on 25/07/2017.
 */

public class Edital {
    private String titulo;
    private String link;

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
