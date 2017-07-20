package br.com.iecapoeira.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import br.com.iecapoeira.model.DashboardItem;
import br.com.iecapoeira.view.DashboardItemView;
import br.com.iecapoeira.view.DashboardItemView_;

public class DashboardAdapter extends BaseAdapter {

    private List<DashboardItem> models;

    public DashboardAdapter(List<DashboardItem> models) {
        this.models = models;
    }

    @Override
    public int getCount() {
        if(models != null)
            return models.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return models.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DashboardItemView cell;
        if (convertView == null) {
            cell = DashboardItemView_.build(parent.getContext());
            convertView = cell;
            convertView.setTag(cell);
        } else {
            cell = (DashboardItemView)convertView.getTag();
        }
        cell.bind(models.get(position));
        return cell;
    }
}