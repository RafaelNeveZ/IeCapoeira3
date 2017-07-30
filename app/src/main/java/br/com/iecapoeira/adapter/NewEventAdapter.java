package br.com.iecapoeira.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewEventActivity;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.model.NewEvent;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


public class NewEventAdapter extends  RecyclerView.Adapter<NewEventAdapter.PaymentHolder>  {

    private List<NewEvent> items;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context context;

    public NewEventAdapter(Context context, List<NewEvent> items) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.items = items;

    }
/*
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PaymentHolder holder = null;

        LayoutInflater inflater = ((NewEventActivity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        holder = new PaymentHolder();

        holder.newEvent = items.get(position);

        holder.btDate = (Button) row.findViewById(R.id.bt_date);
        holder.btDate.setTag(holder.newEvent);

        holder.btHour = (Button) row.findViewById(R.id.bt_hour);
        holder.btHour.setTag(holder.newEvent);

        holder.btFinalHout = (Button) row.findViewById(R.id.bt_final_hour);
        holder.btFinalHout.setTag(holder.newEvent);

        row.setTag(holder);


        return row;
    }*/

    @Override
    public PaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_new_event, parent, false);
        Log.i("LOG", "INFLEI");
        PaymentHolder mvh = new PaymentHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(PaymentHolder holder, int position) {
        Log.i("LOG", "onBindViewHolder()");
        final NewEvent item = items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public class PaymentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button btDate;
        public Button btHour;
        public Button btFinalHour;


        public PaymentHolder(View itemView) {
            super(itemView);

            btDate = (Button) itemView.findViewById(R.id.bt_date);
            btHour = (Button) itemView.findViewById(R.id.bt_hour);
            btFinalHour = (Button) itemView.findViewById(R.id.bt_final_hour);

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