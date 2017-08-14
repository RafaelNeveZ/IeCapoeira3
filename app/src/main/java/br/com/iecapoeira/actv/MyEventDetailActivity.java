package br.com.iecapoeira.actv;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.EventDetailFragment_;
import br.com.iecapoeira.fragment.MyEventDetailFragment_;

@EActivity(R.layout.actv_detail_event)
public class MyEventDetailActivity extends AppCompatActivity {

    @AfterViews
    public void init() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, MyEventDetailFragment_.builder().build()).commit();
    }
}