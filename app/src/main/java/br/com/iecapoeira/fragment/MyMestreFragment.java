package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewClassActivity_;
import br.com.iecapoeira.model.Mestre;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.main)
public class MyMestreFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @OptionsMenuItem(R.id.new_event)
    MenuItem newsong;

    @ViewById
    PagerSlidingTabStrip tabs;

    private String filter="";

    public  List<Mestre> listAngola;
    public List<Mestre> listRegional;

    private TabsAdapter adapter;
    final int[] title = {R.string.angola_title, R.string.regional_title};

    @AfterViews
    public void init() {
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
        listAngola = new ArrayList<>();

        for(int i = 0; i < nomes.length; i++){
            Mestre m = new Mestre( nomes[i % nomes.length],historia[i % historia.length],photos[i % nomes.length],thumbs[i % nomes.length] );
            listAngola.add(m);
        }

        MyMestreListFragment_.angola = listAngola;

        String[] nomes1 = new String[]{"Mestre Pastinha,","Mestre Bimba","Mestre João Pequeno" };
        String[] historia1 = new String[]{getString(R.string.hist_mestre_pastinha),
                getString(R.string.hist_mestre_bimba), getString(R.string.hist_mestre_joao_pequeno)};
        int[] photos1 = new int[]{
                R.drawable.mestre_pastinha,R.drawable.mestre_bimba,R.drawable.mestre_joao_pequeno};
        int[] thumbs1 = new int[]{
                R.drawable.mestre_pastinha_thumb,R.drawable.mestre_bimba_thumb,R.drawable.mestre_joao_pequeno_thumb};
        listRegional = new ArrayList<>();

        for(int i = 0; i < nomes1.length; i++){
            Mestre m = new Mestre( nomes1[i % nomes1.length],historia1[i % historia1.length],photos1[i % nomes1.length],thumbs1[i % nomes1.length] );
            listRegional.add(m);
        }

        MyMestreListFragment_.regional = listRegional;





        adapter = new TabsAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabs.notifyDataSetChanged();
                ((MyMestreListFragment_)adapter.getItem(pager.getCurrentItem())).update();
//                getActivity().setTitle(title[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
                newsong.setVisible(false);

    }

   /* @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewClassActivity_.class), 10);
    }*/



   /* @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewEventActivity_.class), 10);
    }*/

   /* @OptionsItem
    public void filter() {
        startActivityForResult(new Intent(getActivity(), CityActivity_.class), 5);
    }*/


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
                adapter.update();
                filter = result;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Você não escolheu um filtro", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
        adapter.update();
    }

    private class TabsAdapter extends FragmentPagerAdapter {

        //        private int[] icons = {R.drawable.ic_eventlist_tab_globo_unselected, R.drawable.ic_eventlist_tab_mapa_unselected, R.drawable.ic_eventlist_tab_agend_unselected};
//        private int[] iconsSelected = {R.drawable.ic_eventlist_tab_globo_select, R.drawable.ic_eventlist_tab_mapa_selected, R.drawable.ic_eventlist_tab_agend_selected};
        private Fragment[] fragments;

        public TabsAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[getCount()];

        }

        @Override
        public Fragment getItem(int position) {
            if (fragments[position] == null) {
                switch (position) {
                    case 0:
                        MyMestreListFragment.LIST =0;
                        fragments[position] = MyMestreListFragment_.builder().listType(MyMestreListFragment_.LIST_BY_ANGOLA).build();
                        break;
                    case 1:
                        MyClassListFragment.LIST =1;
                        fragments[position] = MyMestreListFragment_.builder().listType(MyMestreListFragment_.LIST_BY_REGIONAL).build();
                        break;
                    default:
                        return null;
                }
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 2;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getString(title[position]);
        }

        public void update() {
            for (Fragment fragment : fragments) {
                try {
                    ((MyMestreListFragment) fragment).update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}