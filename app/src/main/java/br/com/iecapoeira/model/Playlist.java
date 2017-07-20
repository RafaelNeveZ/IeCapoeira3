package br.com.iecapoeira.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;


@ParseClassName("Playlist")
public class Playlist extends ParseObject {

    public static final String USERID = "userId";
    public static final String MUSICA = "musica";

    public Playlist() {
    }

    public String getMusica() {
        return getString(MUSICA);
    }

    public ParseUser getUser() { return getParseUser(USERID); }

    public void setMusica(String musica) { put(MUSICA, musica);}
    public void setUser(ParseUser user) { put(USERID, user);}
}
