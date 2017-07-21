package br.com.iecapoeira.actv;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.MestreAdapter;
import br.com.iecapoeira.fragment.MestreFragment;
import br.com.iecapoeira.model.Mestre;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

/**
 * Created by Felipe Berbert on 15/10/2016.
 */
@EActivity(R.layout.actv_lista_mestres)
public class ListaMestreActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    @ViewById
    Toolbar toolbar;

    @ViewById
    View viewAngola, viewRegional;

    @ViewById
    RecyclerView rvMestres;


    @ViewById
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    MestreAdapter adapter;

    @AfterViews
    void init(){



        setHeader();
        rvMestres.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvMestres.setLayoutManager(llm);
        adapter = new MestreAdapter(this, getSetMestreList());
        adapter.setRecyclerViewOnClickListenerHack(this);
        rvMestres.setAdapter(adapter);

        drawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_logo,  /* nav drawer icon to replace 'Up' caret */
                R.string.open,  /* "open drawer" description */
                R.string.close  /* "close drawer" description */
        );

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.ic_logo);
        toolbar.setTitle(getString(R.string.title_mestres));
        setSupportActionBar(toolbar);
    }

    public List<Mestre> getSetMestreList(){
        String[] nomes = new String[]{"Mestre Canjiquinha", "Mestre Paulo dos Anjos", "Mestre Caiçara", "Mestre Traíra", "Mestre Waldemar da Liberdade", "Mestre Cobrinha Verde", "Mestre Eziquiel","Mestre Pastinha,","Mestre Bimba","Mestre João Pequeno" };
        String[] historia = new String[]{getString(R.string.hist_mestre_canjiquinha), getString(R.string.hist_mestre_paulo_dos_anjos),
                getString(R.string.hist_mestre_caicara),getString(R.string.hist_mestre_traira),
                getResources().getString(R.string.hist_mestre_waldemar), getString(R.string.hist_mestre_cobrinha_verde),
                getString(R.string.hist_mestre_eziquiel), getString(R.string.hist_mestre_pastinha),
                getString(R.string.hist_mestre_bimba), getString(R.string.hist_mestre_joao_pequeno)};
        int[] photos = new int[]{R.drawable.mestre_canjiquinha, R.drawable.mestre_paulo_dos_anjos,R.drawable.mestre_caicara, R.drawable.mestre_traira,R.drawable.mestre_waldemar_da_lierdade,R.drawable.mestre_cobrinha_verde, R.drawable.mestre_eziquiel,
                R.drawable.mestre_pastinha,R.drawable.mestre_bimba,R.drawable.mestre_joao_pequeno};
        int[] thumbs = new int[]{R.drawable.mestre_canjiquinha_thumb, R.drawable.mestre_paulo_dos_anjos_thumb,R.drawable.mestre_caicara_thumb, R.drawable.mestre_traira_thumb,R.drawable.mestre_waldemar_da_lierdade_thumb,R.drawable.mestre_cobrinha_verde_thumb, R.drawable.mestre_eziquiel_thumb,
                R.drawable.mestre_pastinha_thumb,R.drawable.mestre_bimba_thumb,R.drawable.mestre_joao_pequeno_thumb};
        List<Mestre> listAux = new ArrayList<>();

        for(int i = 0; i < nomes.length; i++){
            Mestre m = new Mestre( nomes[i % nomes.length],historia[i % historia.length],photos[i % nomes.length],thumbs[i % nomes.length] );
            listAux.add(m);
        }
        return(listAux);
    }



    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(View view, int position) {
        MestreActivity_.intent(this).mestre(adapter.getList().get(position)).start();
    }
}
