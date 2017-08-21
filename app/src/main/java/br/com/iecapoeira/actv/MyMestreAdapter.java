package br.com.iecapoeira.actv;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import com.parse.ParseObject;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.hemobile.GenericBaseAdapter;
import br.com.hemobile.ItemView;
import br.com.iecapoeira.model.Mestre;
import br.com.iecapoeira.view.ClassItemView_;
import br.com.iecapoeira.view.MestreItemView_;

@EBean
public class MyMestreAdapter extends GenericBaseAdapter<Mestre>implements Filterable {

    @RootContext
    Context c;

    @Override
    public ItemView<Mestre> buildItemView() {
        return MestreItemView_.build(c);
    }

    @Override
    public Filter getFilter() {
        return null;
    }
}
