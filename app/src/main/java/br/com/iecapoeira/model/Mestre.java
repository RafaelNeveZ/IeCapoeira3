package br.com.iecapoeira.model;

import java.io.Serializable;

public class Mestre implements Serializable{
    private String nome;
    private String historia;
    private int photo;
    private int thumb;


    public Mestre(){}
    public Mestre(String m, String h, int p, int t){
        nome = m;
        historia = h;
        photo = p;
        thumb = t;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPhoto() {
        return photo;
    }

    public void setPhoto(int photo) {
        this.photo = photo;
    }

    public String getHistoria() {
        return historia;
    }

    public void setHistoria(String historia) {
        this.historia = historia;
    }

    public int getThumb() {
        return thumb;
    }

    public void setThumb(int thumb) {
        this.thumb = thumb;
    }
}
