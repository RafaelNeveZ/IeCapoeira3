package br.com.iecapoeira.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import br.com.iecapoeira.model.DashboardItem;
import br.com.iecapoeira.view.LeftMenuItemView;
import br.com.iecapoeira.view.LeftMenuItemView_;

public class LeftMenuAdapter extends BaseAdapter {

    private List<DashboardItem> models;

    public LeftMenuAdapter(List<DashboardItem> models) {
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
        LeftMenuItemView cell;
        if (convertView == null) {
            cell = LeftMenuItemView_.build(parent.getContext());
            convertView = cell;
            convertView.setTag(cell);
        } else {
            cell = (LeftMenuItemView)convertView.getTag();
        }
        cell.bind(models.get(position));
        return cell;
    }
}