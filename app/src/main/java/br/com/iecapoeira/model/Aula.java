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

    public static final String FOTO = "foto";
    public static final String HORARIO_COMECO = "horario";
    public static final String HORARIO_FIM = "horarioFinal";
    public static final String DIASSEMANA = "data";
    public static final String ENDERECO = "endereco";
    public static final String ESTADO = "estado";
    public static final String PAIS = "pais";
    public static final String CIDADE = "cidade";
    public static final String GRADUACAO = "graduacao";
    public static final String NOMEACADEMIA = "nomeAcademia";
    public static final String MESTRE = "mestre";
    public static final String NOMEPROFESSOR = "nomeProfessor";
    public static final String SOBREAULA = "sobre";
    public static final String TIPOCAPOEIRA = "estilo";


    public  String getFOTO() {return getString(FOTO);}

    public String getHorarioStart() {
        return getString(HORARIO_COMECO);
    }

    public String getHorarioEnd() {
        return getString(HORARIO_FIM);
    }

    public String getDiasSemana() {
        return getString(DIASSEMANA);
    }

    public String getEndereco() {
        return getString(ENDERECO);
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
