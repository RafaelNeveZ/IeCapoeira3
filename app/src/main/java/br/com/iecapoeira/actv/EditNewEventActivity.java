package br.com.iecapoeira.actv;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.EditNewEventFragment_;
import br.com.iecapoeira.fragment.NewEventFragment_;


@EActivity(R.layout.activity_new_event)
public class EditNewEventActivity extends AppCompatActivity  {

    @AfterViews
    public void init() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, EditNewEventFragment_.builder().build()).commit();
    }
}