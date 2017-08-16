package br.com.iecapoeira.actv;

import android.content.Context;
import android.widget.Filter;
import android.widget.Filterable;

import com.parse.ParseObject;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.GenericBaseAdapter;
import br.com.hemobile.ItemView;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.view.EventItemView_;
import br.com.iecapoeira.view.MyParceiroItemView;
import br.com.iecapoeira.view.MyParceiroItemView_;

@EBean
public class MyParceiroAdapter extends GenericBaseAdapter<ParseObject> implements Filterable {

    @RootContext
    Context c;

    private List<String>filteredData = null;
    private List<String> originalData = null;
    private ItemFilter mFilter = new ItemFilter();


    @Override
    public ItemView<ParseObject> buildItemView() {
        return MyParceiroItemView_.build(c);
    }

    public Filter getFilter() {
        return mFilter;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<String> list = originalData;

            int count = list.size();
            final ArrayList<String> nlist = new ArrayList<String>(count);

            String filterableString ;

            for (int i = 0; i < count; i++) {
                filterableString = list.get(i);
                if (filterableString.toLowerCase().contains(filterString)) {
                    nlist.add(filterableString);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredData = (ArrayList<String>) results.values;
            notifyDataSetChanged();
        }

    }
}
