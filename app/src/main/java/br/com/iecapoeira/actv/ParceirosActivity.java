package br.com.iecapoeira.actv;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.ParceirosAdapter;
import br.com.iecapoeira.widget.ItemOffsetDecoration;


/**
 * Created by Felipe Berbert on 07/10/2016.
 */
@EActivity(R.layout.actv_parceiros)
@OptionsMenu(R.menu.new_remove_sponso)
public class ParceirosActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    View viewParceiros, viewPatrocinadores;

    @ViewById
    RecyclerView rvParceiros;



    ParceirosAdapter adapter;
    GridLayoutManager layoutManager;

    @AfterViews
    void init() {
        rvParceiros.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        rvParceiros.setLayoutManager(layoutManager);
        adapter = new ParceirosAdapter();
        rvParceiros.setAdapter(adapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvParceiros.addItemDecoration(itemDecoration);
        clickParceiros();
    }

    @OptionsItem
    public void remove() {

    }

    @OptionsItem
    public void add() {
        Intent intent = new Intent(this, NewParceiroActivity_.class);
        startActivity(intent);
    }

    private void getListParceiros(){
        ArrayList<Drawable> listaParceiros = new ArrayList<>();
        listaParceiros.add(getResources().getDrawable(R.drawable.parceiro_01));
        listaParceiros.add(getResources().getDrawable(R.drawable.parceiro_02));
        listaParceiros.add(getResources().getDrawable(R.drawable.parceiro_03));
        layoutManager.setSpanCount(3);
        adapter.setPartners(listaParceiros);
    }
    private void getListPatrocinadores(){
        ArrayList<Drawable> listaPatrocinadores = new ArrayList<>();
        listaPatrocinadores.add(getResources().getDrawable(R.drawable.patrocinador_01));
        listaPatrocinadores.add(getResources().getDrawable(R.drawable.patrocinador_02));
        layoutManager.setSpanCount(2);
        adapter.setSponsors(listaPatrocinadores);
    }

    @Click(R.id.ll_parceiros)
    public void clickParceiros() {
        viewParceiros.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
        viewPatrocinadores.setBackgroundColor(getResources().getColor(R.color.white));
        getListParceiros();
    }

    @Click(R.id.ll_patrocinadores)
    public void clickPatrocinadores() {
        viewParceiros.setBackgroundColor(getResources().getColor(R.color.white));
        viewPatrocinadores.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
        getListPatrocinadores();
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
}
