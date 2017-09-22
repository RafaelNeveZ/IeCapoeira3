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
import com.parse.ParseRelation;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;

import java.util.Calendar;
import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.EventDetailActivity_;
import br.com.iecapoeira.actv.EventAdapter;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HENetworkUtil;
import br.com.iecapoeira.view.EventItemView;

@EFragment
public class EventListFragment extends ListFragment {

    public static final int LIST_BY_CAPOEIRA = 0;
    public static final int LIST_BY_CULTURAIS = 1;
    public static final String TYPE_CAPOEIRA = "0";
    public static final String TYPE_CULTURAL = "1";

    public static int LIST = 0;

    @FragmentArg
    int listType;

    @FragmentArg
    String filter;

    @Bean
    EventAdapter adapter;

    @AfterViews
    public void init() {
        setupListView();
    }
    public static String notUpdate=null;

    @UiThread
    void showToast(String msg){

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    void update(String filter) {

        if(!HENetworkUtil.isOnline(getActivity())){
            showToast(getString(R.string.msg_erro_sem_conexao));
            return;
        }

        ParseQuery<Event> query = ParseQuery.getQuery("Event");
//        query.include(Event.OWNER);
        try {
            Log.d("FILTER","" + filter);
            switch (listType) {
                case LIST_BY_CAPOEIRA:
                    query.orderByAscending("startDate");
                    query.whereGreaterThanOrEqualTo("startDate", Calendar.getInstance().getTime());
                    query.whereEqualTo(Event.TYPE, LIST_BY_CAPOEIRA);
                    if(!filter.equals("")){
                        Log.d("FILTER","ENTREI" + filter);
                        query.whereEqualTo(Event.CITY,filter);
                    }

                    break;
                case LIST_BY_CULTURAIS:
                query.orderByAscending("startDate");
                   query.whereGreaterThan("startDate", Calendar.getInstance().getTime());
                   query.whereEqualTo(Event.TYPE, LIST_BY_CULTURAIS);
                    if(!filter.equals("")){
                        Log.d("FILTER","ENTREI" + filter);
                        query.whereEqualTo(Event.CITY,filter);
                    }
                    break;
            }
           // query.orderByAscending(Event.DATE);
            query.findInBackground(new FindCallback<Event>() {
                @Override
                public void done(List<Event> events, ParseException e) {

                    handleResult(events, e);
                }
            });
        } catch (Exception e) {

        }
    }

    @Background
    void handleResult(List<Event> events, ParseException e) {
        /*if (e == null) {
            for (Event event : events) {
                getUsersGoing(event);
            }
        }*/
        notUpdate=null;
        setupAdapter(events);
    }

    void getUsersGoing(Event event) {
        ParseRelation<ParseObject> relation = event.getRelation(Event.GOING);
        try {
            ParseObject.pinAll(relation.getQuery().find());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @UiThread
    void setupAdapter(List<Event> events) {
        try {
            adapter.setList(events);
            adapter.notifyDataSetChanged();
            if (getListAdapter() == null)
                setListAdapter(adapter);
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        if (notUpdate != null){
            update(notUpdate);
            }else{
            update("");
        }
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
        setEmptyText(getString(R.string.msg_no_events));
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Event item = adapter.getItem(position);
        startActivity(new Intent(getActivity(), EventDetailActivity_.class).putExtra("id", item.getObjectId()));
    }
}