package br.com.iecapoeira.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.view.ClassScheduleItemView;
import br.com.iecapoeira.view.ClassScheduleItemView_;
import br.com.iecapoeira.view.EventScheduleItemView;
import br.com.iecapoeira.view.EventScheduleItemView_;


/**
 * Created by Rafael on 09/08/16.
 */
public class EventScheduleAdapter extends BaseAdapter {

    private List<Event> models;

    public EventScheduleAdapter(List<Event> models) {
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
        EventScheduleItemView cell;
        if (convertView == null) {
            cell = EventScheduleItemView_.build(parent.getContext());
            convertView = cell;
            convertView.setTag(cell);
        } else {
            cell = (EventScheduleItemView)convertView.getTag();
        }
        cell.bind(models.get(position));
        return cell;
    }
}
