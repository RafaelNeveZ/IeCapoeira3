package br.com.iecapoeira.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;

/**
 * Created by rafae on 10/08/2017.
 */
@ParseClassName("EventDate")
public class EventDate extends ParseObject {

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";
    public static final String HOURINIT = "horaInicial";
    public static final String HOUREND = "horaFinal";
    public static final String MIMINIT = "minuteInicial";
    public static final String MIMEND = "minuteFinal";

    public  int selDay = 0;
    public   int selMinute = 0;
    public   int selYear=0;
    public   int selMonth =0;
    public   int selHour=0;


    public  int finalHour =0;
    public   int finalMinute=0;
    public    String date="";

    public  EventDate(){

    }

    public EventDate(int selDay, int selMonth, int selYear, int selHour, int selMinute, int finalHour, int finalMinute) {
        this.selDay=selDay;
        this.selMonth=selMonth;
        this.selYear =selYear;
        this.selHour=selHour;
        this.selMinute=selMinute;
        this.finalHour=finalHour;
        this.finalMinute=finalMinute;

    }

    public  void setHOURINIT(String horainit) {
        put(HOURINIT,horainit);
    }
    public String getHOURINIT() {
        return get(HOURINIT).toString();
    }

    public void setHOUREND(String horfim) {
        put(HOUREND,horfim);
    }
    public String getHOUREND() {
        return get(HOUREND).toString();
    }


    public  void setFinalHour(int finalHour) {
        this.finalHour = finalHour;
    }

    public  int getFinalMinute() {
        return finalMinute;
    }

    public  void setFinalMinute(int finalMinute) {
        this.finalMinute = finalMinute;
    }

    public void setDate(String date){this.date = date;}
    public String getDate(){return date;}

    public int getselDay() {
        return selDay;
    }

    public void setselDay(int selDay) {
        this.selDay = selDay;
    }
    public int getselMonth() {
        return selMonth;
    }

    public void setselMonth(int selMonth) {
        this.selMonth = selMonth;
    }

    public int getselYear() {
        return selYear;
    }

    public void setselYear(int selYear) {
        this.selYear = selYear;
    }

    public int getselHour() {
        return selHour;
    }

    public void setselHour(int selHour) {
        this.selHour = selHour;
    }

    public int getselMinute() {
        return selMinute;
    }

    public void setselMinute(int selMinute) {
        this.selMinute = selMinute;
    }

    public  int getFinalHour() {
        return finalHour;
    }
}
