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
import br.com.iecapoeira.actv.NewVideoActivity_;

@EFragment(R.layout.frag_main)
@OptionsMenu(R.menu.main)
public class MyVideoFragment extends Fragment {

    @ViewById
    ViewPager pager;

    @OptionsMenuItem(R.id.new_event)
    MenuItem newsong;

    @ViewById
    PagerSlidingTabStrip tabs;

    private String filter="";

    private TabsAdapter adapter;
    final int[] title = {R.string.video_title};

    @AfterViews
    public void init() {
        adapter = new TabsAdapter(getChildFragmentManager());
        pager.setAdapter(adapter);
        tabs.setViewPager(pager);

        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tabs.notifyDataSetChanged();
                ((MyVideoListFragment_)adapter.getItem(pager.getCurrentItem())).update();
            }

            @Override
            public void onPageSelected(int position) {
                tabs.notifyDataSetChanged();
                ((MyVideoListFragment_)adapter.getItem(pager.getCurrentItem())).update();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                tabs.notifyDataSetChanged();
                ((MyVideoListFragment_)adapter.getItem(pager.getCurrentItem())).update();
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
        startActivityForResult(new Intent(getActivity(), NewVideoActivity_.class), 5);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                adapter.update();
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
                        MyVideoListFragment.LIST =0;
                        fragments[position] = MyVideoListFragment_.builder().listType(MyVideoListFragment.LIST_BY_CAPOEIRA).build();
                        break;
                    default:
                        return null;
                }
            }
            return fragments[position];
        }

        @Override
        public int getCount() {
            return 1;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            return getString(R.string.video_title);
        }

        public void update() {
            for (Fragment fragment : fragments) {
                try {
                    ((MyVideoListFragment) fragment).update();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}