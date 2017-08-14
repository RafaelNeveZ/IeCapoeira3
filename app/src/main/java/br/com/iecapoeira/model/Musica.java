package br.com.iecapoeira.model;

import java.io.Serializable;

public class Musica implements Serializable {
    private String nome;
    private String link;
    private String letra;
    private boolean isFavorito;


    public Musica(String nome, String link) {
        this.nome = nome;
        this.link = link;

    }

    public Musica(String m, int p) {
        nome = m;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public boolean isFavorito() {
        return isFavorito;
    }

    public void setFavorito(boolean favorito) {
        isFavorito = favorito;
    }
}
