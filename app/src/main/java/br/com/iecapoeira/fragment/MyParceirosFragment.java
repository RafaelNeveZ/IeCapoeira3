package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.Menu;
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

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.CityActivity_;
import br.com.iecapoeira.actv.NewEventActivity_;
import br.com.iecapoeira.actv.NewParceiroActivity_;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.part_menu)
public class MyParceirosFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @ViewById
    PagerSlidingTabStrip tabs;

    @OptionsMenuItem
    MenuItem newEvent;

    private TabsAdapter adapter;
    final int[] title = {R.string.title_parceiros, R.string.title_patrocinadores};
    private String filter="";
    GridLayoutManager layoutManager;

    @AfterViews
    public void init() {
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
                ((MyParceirosListFragment_)adapter.getItem(pager.getCurrentItem())).update("");
//                getActivity().setTitle(title[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });



    }




    @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewParceiroActivity_.class), 10);
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {


        // checkClass();
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            Log.d("TAG", "ADM");
            newEvent.setVisible(true);

        } else {
            Log.d("TAG", "Não é ADM");
            newEvent.setVisible(false);

         /*   menu.getItem(1).setEnabled(false);
           menu.getItem(0).setEnabled(true);*/
        }

    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                Toast.makeText(getActivity(), "Filtrando por eventos em " + result, Toast.LENGTH_SHORT).show();
                filter = result;
                adapter.update(filter);

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                adapter.update("");
                Toast.makeText(getActivity(), "Você não escolheu um filtro", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                adapter.update("");
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
//        adapter.update();
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
                        MyParceirosListFragment.LIST =0;
                        fragments[position] = MyParceirosListFragment_.builder().listType(MyParceirosListFragment.LIST_BY_PARCEIROS).filter(filter).build();
                        break;
                    case 1:
                        MyParceirosListFragment.LIST =1;
                        fragments[position] = MyParceirosListFragment_.builder().listType(MyParceirosListFragment.LIST_BY_PATROCINADOR).filter(filter).build();
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

        public void update(String fil) {
            for (Fragment fragment : fragments) {
                try {
                    ((MyParceirosListFragment) fragment).update(fil);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}