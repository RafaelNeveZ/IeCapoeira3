package br.com.iecapoeira.fragment;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.astuetz.PagerSlidingTabStrip;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewEventActivity_;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.main)
public class MainFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @ViewById
    PagerSlidingTabStrip tabs;

    private TabsAdapter adapter;
    final int[] title = {R.string.titulo_eventos_capoeira, R.string.titulo_eventos_culturais};

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
                ((EventListFragment_)adapter.getItem(pager.getCurrentItem())).update();
//                getActivity().setTitle(title[position]);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewEventActivity_.class), 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
                        fragments[position] = EventListFragment_.builder().listType(EventListFragment.LIST_BY_CAPOEIRA).build();
                        break;
                    case 1:
                        fragments[position] = EventListFragment_.builder().listType(EventListFragment.LIST_BY_CULTURAIS).build();
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
                    ((EventListFragment) fragment).update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}