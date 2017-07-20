package br.com.iecapoeira.actv;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;

/**
 * Created by Felipe Berbert on 12/10/2016.
 */

@EActivity(R.layout.actv_contato)
public class ContatoActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @AfterViews
    void init() {
        setHeader();

    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.ic_logo);
        toolbar.setTitle(getString(R.string.title_contato));
        setSupportActionBar(toolbar);
    }
}
