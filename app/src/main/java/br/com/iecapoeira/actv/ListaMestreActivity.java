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
        toolbar.setNavigationIcon(R.drawable.ic_logo);
        toolbar.setTitle(getString(R.string.title_mestres));
        setSupportActionBar(toolbar);
    }

    public List<Mestre> getSetMestreListAngola(){
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

    public List<Mestre> getSetMestreListRegional(){
        String[] nomes = new String[]{"Mestre Pastinha,","Mestre Bimba","Mestre João Pequeno" };
        String[] historia = new String[]{getString(R.string.hist_mestre_pastinha),
                getString(R.string.hist_mestre_bimba), getString(R.string.hist_mestre_joao_pequeno)};
        int[] photos = new int[]{
                R.drawable.mestre_pastinha,R.drawable.mestre_bimba,R.drawable.mestre_joao_pequeno};
        int[] thumbs = new int[]{
                R.drawable.mestre_pastinha_thumb,R.drawable.mestre_bimba_thumb,R.drawable.mestre_joao_pequeno_thumb};
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(View view, int position) {
        MestreActivity_.intent(this).mestre(adapter.getList().get(position)).start();
    }
}
