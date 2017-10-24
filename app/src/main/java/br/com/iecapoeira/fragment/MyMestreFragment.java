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
import br.com.iecapoeira.actv.NewMestreActivity_;
import br.com.iecapoeira.model.Mestre;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.main)
public class MyMestreFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @OptionsMenuItem(R.id.new_event)
    MenuItem newEvent;

    @ViewById
    PagerSlidingTabStrip tabs;


    private TabsAdapter adapter;
    final int[] title = {R.string.angola_title, R.string.regional_title};

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
                ((MyMestreListFragment_)adapter.getItem(pager.getCurrentItem())).update();

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ((MyMestreListFragment_)adapter.getItem(pager.getCurrentItem())).update();

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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

    @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewMestreActivity_.class), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                ((MyMestreListFragment_)adapter.getItem(pager.getCurrentItem())).update();

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }

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
                        MyMestreListFragment.LIST =1;
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