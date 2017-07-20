package br.com.iecapoeira.actv;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.ClassScheduleAdapter;
import br.com.iecapoeira.model.Aula;

/**
 * Created by Rafael on 09/08/16.
 */
@EActivity(R.layout.actv_class_schedule)
public class ClassScheduleActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;

    @ViewById
    View viewAngola, viewRegional;

    @ViewById
    ListView listClasses;

    @ViewById
    Toolbar toolbar;

    @ViewById
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    List<Aula> models;
    boolean isAngola;

    @AfterViews
    public void init() {
        setHeader();
        getAngolaList();
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



    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawers();
        else
            super.onBackPressed();
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.ic_logo);
        toolbar.setTitle(getString(R.string.class_schedule));
        setSupportActionBar(toolbar);
    }

    // makes the back on action bar works as back button
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

    public void getList() {
        showProgress(getString(R.string.loading_data));

        ParseQuery<Aula> query = ParseQuery.getQuery(Aula.class);
        query.whereEqualTo(Aula.TIPOCAPOEIRA, isAngola ? 0 : 1);
        query.findInBackground(new FindCallback<Aula>() {
            @Override
            public void done(List<Aula> models, ParseException e) {
                dismissProgress();

                ClassScheduleActivity.this.models = models;
                updateList();
            }
        });
    }

    @ItemClick
    void listClassesItemClicked(Aula selectedModel) {
        ClassScheduleDetailActivity.model = selectedModel;
        Intent intent = new Intent(this, ClassScheduleDetailActivity_.class);
        startActivity(intent);
    }

    @UiThread
    public void updateList() {
        if (models == null) {
            Toast.makeText(this,getString(R.string.generic_error), Toast.LENGTH_SHORT).show();
        }
        listClasses.setAdapter(new ClassScheduleAdapter(models));
    }

    @Click({R.id.bt_angola, R.id.ll_angola, R.id.view_angola})
    public void getAngolaList() {
        if (MyApplication.hasInternetConnection()) {
            isAngola = true;
            viewAngola.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
            viewRegional.setBackgroundColor(getResources().getColor(R.color.white));

            getList();
        } else {
            Toast.makeText(this, R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @Click({R.id.bt_regional, R.id.ll_regional, R.id.view_regional})
    public void getRegionalList() {
        if (MyApplication.hasInternetConnection()) {
            isAngola = false;
            viewAngola.setBackgroundColor(getResources().getColor(R.color.white));
            viewRegional.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));

            getList();
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
    }
}
