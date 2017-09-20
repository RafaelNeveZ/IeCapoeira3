package br.com.iecapoeira.actv;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.MestreAdapter;
import br.com.iecapoeira.fragment.MainFragment_;
import br.com.iecapoeira.fragment.MestreFragment;
import br.com.iecapoeira.model.Mestre;
import br.com.iecapoeira.widget.ItemOffsetDecoration;
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


    MestreAdapter adapter;

    @ViewById
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;


    @AfterViews
    void init(){
        setHeader();

        rvMestres.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rvMestres.setLayoutManager(llm);
        adapter = new MestreAdapter(this, getSetMestreListAngola());
        adapter.setRecyclerViewOnClickListenerHack(this);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvMestres.addItemDecoration(itemDecoration);
        rvMestres.setAdapter(adapter);
        clickAngola();


    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_menu);
        toolbar.setTitle(getString(R.string.title_mestres_in));
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu, 0, 0);
        drawerLayout.setDrawerListener(drawerToggle);
    }

    public List<Mestre> getSetMestreListAngola(){
        String[] nomes = new String[]{"Mestre Pastinha", "Mestre Paulo dos Anjos",
                "Mestre Traíra", "Mestre Waldemar da Liberdade", "Mestre Cobrinha Verde",
                "Mestre João Pequeno", "Mestre Caiçara","Mestre Canjiquinha" };
        String[] historia = new String[]{getString(R.string.hist_mestre_pastinha),
                getString(R.string.hist_mestre_paulo_dos_anjos),
                getString(R.string.hist_mestre_traira),
                getResources().getString(R.string.hist_mestre_waldemar),
                getString(R.string.hist_mestre_cobrinha_verde),
                getString(R.string.hist_mestre_joao_pequeno),
                getString(R.string.hist_mestre_caicara),
                getString(R.string.hist_mestre_canjiquinha)};

        int[] photos = new int[]{R.drawable.mestre_pastinha, R.drawable.mestre_paulo_dos_anjos,
                R.drawable.mestre_traira,R.drawable.mestre_waldemar_da_lierdade,
                R.drawable.mestre_cobrinha_verde,
                R.drawable.mestre_joao_pequeno,
                R.drawable.mestre_caicara,
                R.drawable.mestre_canjiquinha};
        int[] thumbs = new int[]{R.drawable.mestre_pastinha_thumb, R.drawable.mestre_paulo_dos_anjos_thumb,
                 R.drawable.mestre_traira_thumb, R.drawable.mestre_waldemar_da_lierdade_thumb,
                R.drawable.mestre_cobrinha_verde_thumb,
                R.drawable.mestre_joao_pequeno_thumb,
                R.drawable.mestre_caicara_thumb,
                R.drawable.mestre_canjiquinha_thumb};
        List<Mestre> listAux = new ArrayList<>();

        for(int i = 0; i < nomes.length; i++){
            Mestre m = new Mestre( nomes[i % nomes.length],historia[i % historia.length],photos[i % nomes.length],thumbs[i % nomes.length] );
            listAux.add(m);
        }
        return(listAux);
    }

    public List<Mestre> getSetMestreListRegional(){
        String[] nomes = new String[]{"Mestre Eziquiel","Mestre Bimba" };
        String[] historia = new String[]{getString(R.string.hist_mestre_eziquiel),
                getString(R.string.hist_mestre_bimba)};
        int[] photos = new int[]{
                R.drawable.mestre_eziquiel,R.drawable.mestre_bimba};
        int[] thumbs = new int[]{
                R.drawable.mestre_eziquiel_thumb,R.drawable.mestre_bimba_thumb};
        List<Mestre> listAux = new ArrayList<>();

        for(int i = 0; i < nomes.length; i++){
            Mestre m = new Mestre( nomes[i % nomes.length],historia[i % historia.length],photos[i % nomes.length],thumbs[i % nomes.length] );
            listAux.add(m);
        }
        return(listAux);
    }

    @Click(R.id.ll_angola)
    public void clickAngola() {
        viewAngola.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
        viewRegional.setBackgroundColor(getResources().getColor(R.color.white));
        adapter.setAngola(getSetMestreListAngola());
    }

    @Click(R.id.ll_regional)
    public void clickRegional() {
        viewAngola.setBackgroundColor(getResources().getColor(R.color.white));
        viewRegional.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
        adapter.setAngola(getSetMestreListRegional());
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        if (drawerToggle.onOptionsItemSelected(item)) {
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
