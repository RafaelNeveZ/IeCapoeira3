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
import android.widget.Toast;

import java.text.SimpleDateFormat;
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
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        holder.btDate.setText(item.getselDay()+"/"+item.getselMonth()+"/"+item.getselYear());
        holder.btFinalHour.setText("13:00");
        holder.btHour.setText("12:00");
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

            /*itemView.setOnClickListener(this);*/

            btDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "DATA", Toast.LENGTH_LONG).show();
                }
            });
            btHour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "horaI", Toast.LENGTH_LONG).show();
                }
            });
            btFinalHour.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "horaF", Toast.LENGTH_LONG).show();
                }
            });


        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }

}