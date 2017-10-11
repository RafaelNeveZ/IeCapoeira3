package br.com.iecapoeira.fragment;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ClassScheduleDetailActivity;
import br.com.iecapoeira.actv.ClassScheduleDetailActivity_;
import br.com.iecapoeira.actv.MyClassAdapter;
import br.com.iecapoeira.actv.MyMusicaAdapter;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HENetworkUtil;
import br.com.iecapoeira.view.MusicaItemView;

@EFragment
public class MyClassListFragment extends ListFragment {

    public static final int LIST_BY_ANGOLA = 0;
    public static final int LIST_BY_REGIONAL = 1;


    public static final String TYPE_ANGOLA = "Anggola";
    public static final String TYPE_REGIONAL = "Regional";


    public static int LIST = 0;

    @FragmentArg
    int listType;


    @Bean
    public MyClassAdapter adapter;

    @AfterViews
    public void init() {
        setupListView();
    }


    @UiThread
    void showToast(String msg){

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    void update() {

        if(!HENetworkUtil.isOnline(getActivity())){
            showToast(getString(R.string.msg_erro_sem_conexao));
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Aulas");
        try {
            Log.d("TAG","" + listType);
            switch (listType) {
                case LIST_BY_ANGOLA:
                    //                   query.whereGreaterThan(Event.DATE, Calendar.getInstance().getTime());
                    query.whereEqualTo(Aula.TIPOCAPOEIRA, "Angola");
                    Log.d("TAG","CAPOEIRA");
                    Log.d("Event.TYPE",Event.TYPE);
                    Log.d("TYPE_CUTURAL", TYPE_ANGOLA);
                    break;
                case LIST_BY_REGIONAL:
                    //                query.whereGreaterThan(Event.DATE, Calendar.getInstance().getTime());
                    query.whereEqualTo(Aula.TIPOCAPOEIRA, "Regional");

                    Log.d("TAG","CULTURAL");
                    Log.d("Event.TYPE",Event.TYPE);
                    Log.d("TYPE_CUTURAL", TYPE_REGIONAL);
                    break;
            }
            query.whereEqualTo("teste", true);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> musics, ParseException e) {
                    Log.d("LISTA DE EVENT SIZE","" + musics.size());
                    handleResult(musics, e);
                }
            });


        } catch (Exception ex) {

        }

    }

    @Background
    void handleResult(List<ParseObject> musics, ParseException e) {
        /*if (e == null) {
            for (Event event : events) {
                getUsersGoing(event);
            }
        }*/
        setupAdapter(musics);
    }

    /*void getUsersGoing(ParseObject musica) {
        ParseRelation<ParseObject> relation = event.getRelation(Event.GOING);
        try {
            ParseObject.pinAll(relation.getQuery().find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }*/

    @UiThread
    void setupAdapter(List<ParseObject> musics) {
        try {
            adapter.setList(musics);
            adapter.notifyDataSetChanged();
            if (getListAdapter() == null)
                setListAdapter(adapter);
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
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
        setEmptyText("Não possui aulas aqui");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ParseObject item = adapter.getItem(position);
        ClassScheduleDetailActivity.model = item;
        startActivityForResult(new Intent(getActivity(), ClassScheduleDetailActivity_.class), 15);
    }

}