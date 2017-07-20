package br.com.iecapoeira.actv;

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
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.ParceirosAdapter;
import br.com.iecapoeira.widget.ItemOffsetDecoration;


/**
 * Created by Felipe Berbert on 07/10/2016.
 */
@EActivity(R.layout.actv_parceiros)
public class ParceirosActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    View viewParceiros, viewPatrocinadores;

    @ViewById
    RecyclerView rvParceiros;

    @ViewById
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    ParceirosAdapter adapter;
    GridLayoutManager layoutManager;

    @AfterViews
    void init() {
        setHeader();
        rvParceiros.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(this, 3);
        rvParceiros.setLayoutManager(layoutManager);
        adapter = new ParceirosAdapter();
        rvParceiros.setAdapter(adapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        rvParceiros.addItemDecoration(itemDecoration);
        clickParceiros();

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
        toolbar.setTitle(getString(R.string.title_parceiros));
        setSupportActionBar(toolbar);
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
}
