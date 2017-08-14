package br.com.iecapoeira.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.ParseObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.EventDetailActivity;
import br.com.iecapoeira.actv.EventDetailActivity_;
import br.com.iecapoeira.actv.NewEventActivity;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


public class TimeEventoAdapter extends RecyclerView.Adapter<TimeEventoAdapter.MyViewHolder> {
    private List<JSONObject> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context context;

    public TimeEventoAdapter(Context c, List<JSONObject> list) {
        mList = list;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_event_date, viewGroup, false);

        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {

        final JSONObject json = mList.get(position);

        try {
            myViewHolder.date.setText( json.getString("date"));
            myViewHolder.hour.setText("Das " + json.getString("startTime")+" às "+json.getString("endTime"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public void setRecyclerViewOnClickListenerHack(EventDetailActivity_ evdAcy) {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView date,hour;

        public MyViewHolder(View itemView) {
            super(itemView);

            date = (TextView) itemView.findViewById(R.id.text_date);
            hour = (TextView) itemView.findViewById(R.id.text_hour);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}