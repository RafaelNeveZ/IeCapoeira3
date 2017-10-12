package br.com.iecapoeira.fragment;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ClassScheduleDetailActivity;
import br.com.iecapoeira.actv.ClassScheduleDetailActivity_;
import br.com.iecapoeira.actv.MestreActivity_;
import br.com.iecapoeira.actv.MyClassAdapter;
import br.com.iecapoeira.actv.MyMestreAdapter;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Mestre;
import br.com.iecapoeira.utils.HENetworkUtil;

@EFragment
public class MyMestreListFragment extends ListFragment {

    public static final int LIST_BY_ANGOLA = 0;
    public static final int LIST_BY_REGIONAL = 1;
    public static boolean oneTime=true;

    public static final String TYPE_ANGOLA = "Anggola";
    public static final String TYPE_REGIONAL = "Regional";


    public static int LIST = 0;

    @FragmentArg
    int listType;


    public static List<Mestre>angola;

    public static List<Mestre>regional;

    @Bean
    public MyMestreAdapter adapter;

    @AfterViews
    public void init() {
        setupListView();
    }


    @UiThread
    void showToast(String msg){

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    void update() {

        try {

            switch (listType) {
                case LIST_BY_ANGOLA:


                    handleResult(angola);

                    break;
                case LIST_BY_REGIONAL:

                    handleResult(regional);
                    break;
            }

        } catch (Exception ex) {

        }

    }

    @Background
    void handleResult(List<Mestre> mestres) {
        setupAdapter(mestres);
    }


    @UiThread
    void setupAdapter(List<Mestre> mestres) {
        try {
            adapter.setList(mestres);
            adapter.notifyDataSetChanged();
            if (getListAdapter() == null)
                setListAdapter(adapter);
        } catch (Exception e) {}
    }



    private void setupListView() {
        ListView listView = getListView();
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        listView.setClickable(false);
        listView.setItemsCanFocus(true);

        int padding = MyApplication.dpToPixels(16);
        listView.setPadding(padding, padding, padding, 0);
        listView.setDivider(null);
        listView.setDividerHeight(padding);
        setEmptyText("Não possui nada aqui");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Mestre item = adapter.getItem(position);
        MestreActivity_.intent(this).mestre(adapter.getList().get(position)).start();
    }

}