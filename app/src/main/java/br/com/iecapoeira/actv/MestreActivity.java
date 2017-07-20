package br.com.iecapoeira.actv;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Mestre;

@EActivity(R.layout.act_mestrefilter)
public class MestreActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView textoMestre, tvNome;

    @ViewById
    ImageView imgMestre;

    @Extra
    Mestre mestre;

    @AfterViews
    void ini(){
        setHeader();
        prepareContent();
    }

    private void prepareContent(){
        tvNome.setText(mestre.getNome());
        textoMestre.setText(mestre.getHistoria());
        imgMestre.setImageDrawable(getResources().getDrawable(mestre.getPhoto()));
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_voltar);
        toolbar.setTitle(getString(R.string.title_mestres));
        setSupportActionBar(toolbar);
    }
    // makes the back on action bar works as back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }
}
