package br.com.iecapoeira.actv;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import com.parse.ParseObject;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.hemobile.GenericBaseAdapter;
import br.com.hemobile.ItemView;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.view.MusicaItemView_;
import br.com.iecapoeira.view.MyEventItemView_;

@EBean
public class MyMusicaAdapter extends GenericBaseAdapter<ParseObject>implements Filterable {

    @RootContext
    Context c;

    @Override
    public ItemView<ParseObject> buildItemView() {
        return MusicaItemView_.build(c);
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
