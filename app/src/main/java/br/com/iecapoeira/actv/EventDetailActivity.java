package br.com.iecapoeira.actv;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsMenu;

import br.com.hemobile.BaseActivity;
import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.EventDetailFragment_;

@EActivity(R.layout.actv_detail_event)
public class EventDetailActivity extends AppCompatActivity {

    @AfterViews
    public void init() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, EventDetailFragment_.builder().build()).commit();
    }
}