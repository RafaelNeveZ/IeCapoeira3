package br.com.iecapoeira.actv;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

    public void faceLink(View v){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/I%C3%AA-Capoeira-Aplicativo-663020423814756/")));
    }
    public void instaLink(View v){
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/iecapoeira/")));
    }
    public void siteLink(View v) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://iecapoeira.com.br/")));
    }
}
