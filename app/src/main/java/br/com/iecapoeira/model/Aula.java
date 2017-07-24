package br.com.iecapoeira.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Rafael on 09/08/16.
 */
@ParseClassName("Aulas")
public class Aula extends ParseObject {

    public static final String FOTOPROFESSOR = "fotoProfessor";
    public static final String HORARIO_COMECO = "horario";
    public static final String HORARIO_FIM = "horario";
    public static final String DIASSEMANA = "diasSemana";
    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";
    public static final String NOMEACADEMIA = "nomeAcademia";
    public static final String MESTRE = "mestre";
    public static final String NOMEPROFESSOR = "nomeProfessor";
    public static final String SOBREAULA = "sobreAula";
    public static final String TIPOCAPOEIRA = "tipoCapoeira";


    public String getHorarioStart() {
        return getString(HORARIO_COMECO);
    }

    public String getHorarioEnd() {
        return getString(HORARIO_FIM);
    }

    public List<Integer> getDiasSemana() {
        return (List<Integer>)get(DIASSEMANA);
    }

    public String getLongitude() {
        return getString(LONGITUDE);
    }

    public String getLatitude() {
        return getString(LATITUDE);
    }

    public String getNomeAcademia() {
        return getString(NOMEACADEMIA);
    }

    public String getMestre() {
        return getString(MESTRE);
    }

    public String getNomeProfessor() {
        return getString(NOMEPROFESSOR);
    }

    public String getSobreAula() {
        return getString(SOBREAULA);
    }

    public int getTipoCapoeira() {
        return getInt(TIPOCAPOEIRA);
    }
}
