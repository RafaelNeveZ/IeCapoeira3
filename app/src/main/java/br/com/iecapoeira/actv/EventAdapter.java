package br.com.iecapoeira.actv;

import android.content.Context;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.hemobile.GenericBaseAdapter;
import br.com.hemobile.ItemView;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.view.EventItemView_;

@EBean
public class EventAdapter extends GenericBaseAdapter<Event> {

    @RootContext
    Context c;

    @Override
    public ItemView<Event> buildItemView() {
        return EventItemView_.build(c);
    }
}
