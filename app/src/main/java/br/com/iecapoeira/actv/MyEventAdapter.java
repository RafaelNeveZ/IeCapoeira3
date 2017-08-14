package br.com.iecapoeira.actv;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.hemobile.GenericBaseAdapter;
import br.com.hemobile.ItemView;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.view.EventItemView_;
import br.com.iecapoeira.view.MyEventItemView_;

@EBean
public class MyEventAdapter extends GenericBaseAdapter<Event>implements Filterable {

    @RootContext
    Context c;

    @Override
    public ItemView<Event> buildItemView() {
        return MyEventItemView_.build(c);
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
