package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import br.com.iecapoeira.actv.CityFiterActivity_;
import br.com.iecapoeira.actv.NewEventActivity_;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.event_menu)
public class MainFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @ViewById
    PagerSlidingTabStrip tabs;

    @OptionsMenuItem
    MenuItem newEvent;

    private TabsAdapter adapter;
    final int[] title = {R.string.titulo_eventos_capoeira, R.string.titulo_eventos_culturais};
    private String filter="";
    public boolean capoeira = true;
    public boolean adm = false;

    @AfterViews
    public void init() {
        adapter = new TabsAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

       // getActivity().invalidateOptionsMenu();
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               if(!adm) {
                   if (position == 0) {

                       getActivity().invalidateOptionsMenu();
                       capoeira = true;
                   } else {

                       getActivity().invalidateOptionsMenu();
                       capoeira = false;
                   }
               }
            }

            @Override
            public void onPageSelected(int position) {
                tabs.notifyDataSetChanged();
                ((EventListFragment_)adapter.getItem(pager.getCurrentItem())).update("");

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

    @OptionsItem
    public void filter() {
        startActivityForResult(new Intent(getActivity(), CityFiterActivity_.class), 5);
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

    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        adm = ((boolean) ParseUser.getCurrentUser().get("Admin"));
        if(!adm) {
            if (capoeira) {
                newEvent.setVisible(true);
           //     capoeira = false;
            } else {
                newEvent.setVisible(false);
         //       capoeira = true;
            }
        }
    }

    private class TabsAdapter extends FragmentPagerAdapter {

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
                        EventListFragment.LIST =0;
                        fragments[position] = EventListFragment_.builder().listType(EventListFragment.LIST_BY_CAPOEIRA).filter(filter).build();
                        break;
                    case 1:
                        EventListFragment.LIST =1;
                        fragments[position] = EventListFragment_.builder().listType(EventListFragment.LIST_BY_CULTURAIS).filter(filter).build();
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
                    ((EventListFragment) fragment).update(fil);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}