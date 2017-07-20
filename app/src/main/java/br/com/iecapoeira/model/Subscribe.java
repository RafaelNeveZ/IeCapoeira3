package br.com.iecapoeira.model;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface Subscribe {

    public String channels();
}
