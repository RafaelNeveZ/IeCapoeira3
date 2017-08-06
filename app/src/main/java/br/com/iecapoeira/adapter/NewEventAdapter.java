package br.com.iecapoeira.adapter;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewEventActivity;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.model.NewEvent;
import br.com.iecapoeira.utils.OnButtonClicked;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


public class NewEventAdapter extends  RecyclerView.Adapter<NewEventAdapter.PaymentHolder> implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private List<NewEvent> items;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context context;
    private OnButtonClicked listener=null;

    public NewEventAdapter(Context context, List<NewEvent> items, OnButtonClicked listener) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.items = items;
        this.listener=listener;
    }


    @Override
    public PaymentHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = mLayoutInflater.inflate(R.layout.item_new_event, parent, false);

        PaymentHolder mvh = new PaymentHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(final PaymentHolder holder, int position) {

        final NewEvent item = items.get(position);
        /*Log.d("POSITION",position+"");
        Log.d("DAY",item.getselDay()+"");*/
        String pattern = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        holder.btDate.setText(item.getselDay()+"/"+item.getselMonth()+"/"+item.getselYear());
        holder.btDate.setTag(position);
        holder.btDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onBtnClick((Integer) v.getTag(), 0);
                }
            }
        });
        holder.btFinalHour.setText(String.format("%02d:%02d", item.getFinalHour(),item.getFinalMinute()));
        holder.btFinalHour.setTag(position);
        holder.btFinalHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onBtnClick((Integer) v.getTag(), 2);
                }
            }
        });
        holder.btHour.setText(String.format("%02d:%02d", item.getselHour(), item.getselMinute()));
        holder.btHour.setTag(position);
        holder.btHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener!=null) {
                    listener.onBtnClick((Integer) v.getTag(),1);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
       // setJustTime(hourOfDay, minute,isInit);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
     //   setJustDate(year, monthOfYear, dayOfMonth);
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


        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }

}