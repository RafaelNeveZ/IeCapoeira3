package br.com.iecapoeira.model;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

/**
 * Created by rafae on 30/07/2017.
 */

public class NewEvent {

    private int name = 0;

    public NewEvent(int name) {
        this.setName(name);

    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}
