package br.com.iecapoeira.chat;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.UNIQUE)
public interface TimePref {
    long initialTime();

}
