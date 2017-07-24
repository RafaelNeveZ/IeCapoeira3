package br.com.iecapoeira.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.view.ClassScheduleDetailItemView;
import br.com.iecapoeira.view.ClassScheduleDetailItemView_;
import br.com.iecapoeira.view.ClassScheduleItemView;
import br.com.iecapoeira.view.ClassScheduleItemView_;

/**
 * Created by rafae on 23/07/2017.
 */

public class ClassScheduleDetailAdapter extends BaseAdapter {


    private List<Aula> models;

    public ClassScheduleDetailAdapter(List<Aula> models) {
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
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ClassScheduleDetailItemView cell;
        if (convertView == null) {
            cell = ClassScheduleDetailItemView_.build(parent.getContext());
            convertView = cell;
            convertView.setTag(cell);
        } else {
            cell = (ClassScheduleDetailItemView)convertView.getTag();
        }
        cell.bind(models.get(position));
        return cell;
    }
}
