package br.com.iecapoeira.actv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.ClassScheduleAdapter;
import br.com.iecapoeira.adapter.EventScheduleAdapter;
import br.com.iecapoeira.chat.PubnubService;

import br.com.iecapoeira.fragment.AgendaFragment;
import br.com.iecapoeira.fragment.AgendaFragment_;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.SubscribeHolder;

/**
 * Created by Rafael on 09/08/16.
 */
@EActivity(R.layout.actv_main)
public class AgendaActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @ViewById
    Toolbar toolbar;

    @ViewById
    DrawerLayout drawerLayout;


    @Bean
    SubscribeHolder subscribeHolder;

    private ActionBarDrawerToggle drawerToggle;


    @AfterViews
    public void init() {
        PubnubService.startPubnubService();
        setHeader();
        loadFragment();
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.ic_logo);
        toolbar.setTitle("Agenda");
        setSupportActionBar(toolbar);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.drawable.ic_menu, 0, 0);
        drawerLayout.setDrawerListener(drawerToggle);
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
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        loadFragment();
    }


    public void showToast(final String msg) {

        AgendaActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AgendaActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment() {
        Fragment fragment = AgendaFragment_.builder().build();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content, fragment)
                .commit();

        drawerLayout.closeDrawers();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }



    /*private ProgressDialog progressDialog;

    @ViewById
    View viewAula, viewEvento;

    @ViewById
    ListView listClasses;

    @ViewById
    Toolbar toolbar;

    @ViewById
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    List<Aula> aula;
    List<Event> events;
    boolean isAula;
    private boolean other=false;

    @AfterViews
    public void init() {
//        setHeader();
        getEventoList();
        drawerToggle = new ActionBarDrawerToggle(
                this,                  *//* host Activity *//*
                drawerLayout,         *//* DrawerLayout object *//*
                R.drawable.ic_logo,  *//* nav drawer icon to replace 'Up' caret *//*
                R.string.open,  *//* "open drawer" description *//*
                R.string.close  *//* "close drawer" description *//*
        );

        // Set the drawer toggle as the DrawerListener
        drawerLayout.setDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        if (drawerToggle != null)
            drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (drawerToggle != null)
            drawerToggle.onConfigurationChanged(newConfig);
    }


    @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(this, NewClassActivity_.class), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                getAulaList();
            }
            if (resultCode == Activity.RESULT_FIRST_USER) {
                other=true;
                getAulaList();
            }
        }
        if (requestCode == 15) {
            if(resultCode == Activity.RESULT_OK){
                getAulaList();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                getAulaList();
            }
        }
    }





    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

  *//*  public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.ic_logo);
        toolbar.setTitle(getString(R.string.title_aulas));
        setSupportActionBar(toolbar);
    }*//*

    // makes the back on action bar works as back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Pass the event to ActionBarDrawerToggle, if it returns
        // true, then it has handled the app icon touch event
        *//*if (drawerToggle != null && drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }*//*
        // Handle your other action bar items...

        return super.onOptionsItemSelected(item);
    }

    public void getListAulas() {
        Log.d("AULA","getlistAula");
        showProgress(getString(R.string.loading_data));
        ParseRelation<Aula> relation = ParseUser.getCurrentUser().getRelation("aulaGo");
        ParseQuery<Aula> query = relation.getQuery();
        query.findInBackground(new FindCallback<Aula>() {
            @Override
            public void done(List<Aula> models, ParseException e) {
                dismissProgress();
                if(other){
                    other=false;
                    newEvent();
                }
                AgendaActivity.this.aula = models;
                updateList();

            }
        });
    }
    public void getListEventos() {
        Log.d("Event","getlistEvento");
        showProgress(getString(R.string.loading_data));
        ParseRelation<Event> relation = ParseUser.getCurrentUser().getRelation("eventGo");
        ParseQuery<Event> query = relation.getQuery();
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> models, ParseException e) {
                dismissProgress();
                if(other){
                    other=false;
                    newEvent();
                }
                AgendaActivity.this.events = models;
                updateList();

            }
        });
    }

    @ItemClick
    void listClassesItemClicked(ParseObject selectedModel) {
            if(isAula) {
                if (aula == null) {
                    Toast.makeText(this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                }
                //ClassScheduleDetailActivity.model = selectedModel;
                startActivityForResult(new Intent(this, ClassScheduleDetailActivity_.class), 15);
            }else{
                if (events == null) {
                    Toast.makeText(this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
                }
                startActivity(new Intent(this, EventDetailActivity_.class).putExtra("id", selectedModel.getObjectId()));
            }


    }

    @UiThread
    public void updateList() {
        if(isAula) {
            if (aula == null) {
                Toast.makeText(this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            }
            listClasses.setAdapter(new ClassScheduleAdapter(aula));
        }else{
            if (events == null) {
                Toast.makeText(this, getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
            }
            listClasses.setAdapter(new EventScheduleAdapter(events));
        }
    }

    @Click({R.id.bt_aula, R.id.ll_aula, R.id.view_aula})
    public void getAulaList() {
        if (MyApplication.hasInternetConnection()) {
            isAula = true;
            viewAula.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
            viewEvento.setBackgroundColor(getResources().getColor(R.color.white));
            getListAulas();
        } else {
            Toast.makeText(this, R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @Click({R.id.bt_evento, R.id.ll_evento, R.id.view_evento})
    public void getEventoList() {
        if (MyApplication.hasInternetConnection()) {
            isAula = false;
            viewAula.setBackgroundColor(getResources().getColor(R.color.white));
            viewEvento.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));

            getListEventos();
        } else {
            Toast.makeText(this, R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(this, getString(R.string.aguarde), text, true, false);
        } catch (Exception e) { e.printStackTrace(); }

    }

    @UiThread
    public void dismissProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) { e.printStackTrace(); }

        }
    }*/
}
