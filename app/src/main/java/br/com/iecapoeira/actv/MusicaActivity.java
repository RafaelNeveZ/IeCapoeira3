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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.MusicaFragment_;
import br.com.iecapoeira.model.Musica;

@EActivity(R.layout.act_musica)
public class MusicaActivity extends AppCompatActivity {


    @ViewById
    Toolbar toolbar;

    @ViewById
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    public List<ParseObject> musicas;


    String[] musicas2 = {
            "É DE BAMBA",
            "CAPOEIRA É UMA ARTE",
    };

    @AfterViews
    void init(){
        setHeader();
        musicas=new ArrayList<>();
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

       /* final ListView lista = (ListView) findViewById(R.id.listView1);
        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, classes );
        lista.setAdapter(adapter);*/

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
        toolbar.setTitle(getString(R.string.title_musicas));
        setSupportActionBar(toolbar);
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private Fragment[] fragments;

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[getCount()];
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            //return PlaceholderFragment.newInstance(position + 1);
            if (fragments[position] == null) {
                switch (position) {
                    case 0:
                        fragments[position] = MusicaFragment_.builder().build();
                        break;
                    case 1:
                        fragments[position] = MusicaFragment_.builder().build();
                        break;
                    case 2:
                        fragments[position] = MusicaFragment_.builder().favoritos(true).build();
                        break;
                    default:
                        return null;
                }
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Angola";
                case 1:
                    return "Regional";
                case 2:
                    return "Play List";
            }
            return null;
        }
    }

    public void getSetMusicaList(){
        /*String[] classes = {
                "É DE BAMBA",
                "CAPOEIRA É UMA ARTE",
                "MUNDO ENGANADOR",
                "SAUDADE QUE FAZ CHORAR",
                "DONA MARIA DO CAMBOATÁ",
                "A Roda Já Começou",
                "MOLEQUE É TU",
                "Ê IDALINA",
                "SABIÁ CANTOU",
                "BAHIA, BAHIA - MARINHEIRO SÔ",
                "É QUANDO GUNGA ME CHAMA",
                "CAPOEIRA (É DEFESA, ATAQUE)",
                "BRINCADEIRA TEM HORA",
                "BESOURO PRETO (SANTO AMARO)",
                "PALMA DE BIMBA",
                "É LEGAL",
                "É MEU IRMÃO",
                "AVISA LÁ MEU MANO",
                "DEIXA BEM-TE-VI VOAR... O SENDERO ACABA DE CHEGAR",
                "CHEGA PRA CÁ",
                "EU VIM PRA VADIAR"
        };


        String[] links = {
                "G2sbWN_FB-w",
                "JZEs1kJeKD4",
                "EPaR0-__sW8",
                "PMCfv8D0LGM",
                "Xm8ZOQ7VObU",
                "AXGeQ5UrQnI",
                "vBSpY801MQc",
                "rHrQY2ti59A",
                "XZp3C-BNJZM",
                "yMk2DnH8iLw",
                "Lfzc41LvZus",
                "9Iwxyxg4ACg",
                "S4O0m702t9Q",
                "FVlnB9vkg1U",
                "voMkLe-ZV_U",
                "3CG3Pvg-76o",
                "yGePe7W-1VQ",
                "0TQkKtzo3pg",
                "fi30Ww2qODg",
                "axTUqHxG5xc",
                "DWIinWB4W_4"
        };
        String[] letras = {
                getString(R.string.musica_de_bamba),
                getString(R.string.musica_capoeira_arte),
                getString(R.string.musica_mundo_enganador),
                getString(R.string.musica_saudade_que_faz_chorar),
                getString(R.string.musica_dona_maria),
                getString(R.string.musica_roda_ja_comecou),
                getString(R.string.musica_moleque_e_tu),
                getString(R.string.musica_e_idalina),
                getString(R.string.musica_sabia_cantou),
                getString(R.string.musica_bahia_bahia),
                getString(R.string.musica_gunga_me_chama),
                getString(R.string.musica_capoeira_defesa_ataque),
                getString(R.string.musica_brincadeira_tem_hora),
                getString(R.string.musica_besouro_preto),
                getString(R.string.musica_palma_de_bimba),
                getString(R.string.musica_e_legal),
                getString(R.string.musica_e_meu_irmao),
                getString(R.string.musica_avisa_la),
                getString(R.string.musica_deixa_bemtevi_voar),
                getString(R.string.musica_chega_pra_ca),
                getString(R.string.musica_eu_vim_pra_vadiar)
        };
        List<Musica> listAux = new ArrayList<>();

        for(int i = 0; i < classes.length; i++){
            Musica m = new Musica(classes[i % classes.length], links[i % links.length]);
            listAux.add(m);
        }*/



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