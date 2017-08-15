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

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewMusicActivity_;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.main)
public class MyMusicaFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @OptionsMenuItem(R.id.new_event)
    MenuItem newsong;

    @ViewById
    PagerSlidingTabStrip tabs;

    private String filter="";

    private TabsAdapter adapter;
    final int[] title = {R.string.angola_title, R.string.regional_title,R.string.play_list};

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
                ((MyMusicaListFragment_)adapter.getItem(pager.getCurrentItem())).update();
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

        try {
            if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
                Log.d("TAG", "ADM");
                newsong.setVisible(true);
            }else{
                newsong.setVisible(false);
                Log.d("TAG", "Não é ADM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewMusicActivity_.class), 10);
    }



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
                        MyMusicaListFragment.LIST =0;
                        fragments[position] = MyMusicaListFragment_.builder().listType(MyMusicaListFragment.LIST_BY_ANGOLA).build();
                        break;
                    case 1:
                        MyMusicaListFragment.LIST =1;
                        fragments[position] = MyMusicaListFragment_.builder().listType(MyMusicaListFragment.LIST_BY_REGIONAL).build();
                        break;
                    case 2:
                        MyMusicaListFragment.LIST =2;
                        fragments[position] = MyMusicaListFragment_.builder().listType(MyMusicaListFragment.LIST_BY_PLAYLIST).build();
                        break;
                    default:
                        return null;
                }
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getString(title[position]);
        }

        public void update() {
            for (Fragment fragment : fragments) {
                try {
                    ((MyMusicaListFragment) fragment).update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}