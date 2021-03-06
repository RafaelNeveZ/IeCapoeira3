package br.com.iecapoeira.actv;

import android.support.v4.app.FragmentManager;

import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;
import br.com.iecapoeira.R;

import br.com.iecapoeira.fragment.NewEventFragment_;


@EActivity(R.layout.activity_new_event)
public class NewEventActivity extends AppCompatActivity  {

    @AfterViews
    public void init() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, NewEventFragment_.builder().build()).commit();
    }


}