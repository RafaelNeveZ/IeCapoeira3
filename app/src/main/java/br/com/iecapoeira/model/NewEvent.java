package br.com.iecapoeira.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by rafae on 30/07/2017.
 */

public class NewEvent {

    public  int selDay = 0;
    public   int selMinute = 0;
    public   int selYear=0;
    public   int selMonth =0;
    public   int selHour=0;


    public  int finalHour =0;
    public   int finalMinute=0;
    public    String date="";


    public NewEvent(int selDay, int selMonth, int selYear, int selHour, int selMinute, int finalHour, int finalMinute) {
        this.setselDay(selDay);
        this.setselMonth(selMonth);
        this.setselYear(selYear);
        this.setselHour(selHour);
        this.setselMinute(selMinute);
        this.setFinalHour(finalHour);
        this.setFinalMinute(finalMinute);
    }


    public  int getFinalHour() {
        return finalHour;
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



}
