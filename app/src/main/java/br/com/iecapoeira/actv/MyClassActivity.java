package br.com.iecapoeira.actv;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.parse.ParseObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.chat.PubnubService;
import br.com.iecapoeira.fragment.MyClassFragment_;
import br.com.iecapoeira.fragment.MyMusicaFragment_;
import br.com.iecapoeira.model.SubscribeHolder;

@EActivity(R.layout.actv_main)
public class MyClassActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

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
        toolbar.setTitle("Aulas");
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

       this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MyClassActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadFragment() {
        Fragment fragment = MyClassFragment_.builder().build();
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
}