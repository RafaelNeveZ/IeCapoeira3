package br.com.iecapoeira.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by rafae on 30/07/2017.
 */

public class NewEvent {

    public static int selDay = 0;
    public static  int selMinute = 0;
    public static  int selYear=0;
    public static  int selMonth =0;
    public static  int selHour=0;


    public NewEvent(int selDay, int selMonth, int selYear, int selHour, int selMinute) {
        this.setselDay(selDay);
        this.setselMonth(selMonth);
        this.setselYear(selYear);
        this.setselHour(selHour);
        this.setselMinute(selMinute);

    }

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
