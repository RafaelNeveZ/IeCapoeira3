package br.com.iecapoeira.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


public class NewEventAdapter extends RecyclerView.Adapter<NewEventAdapter.MyViewHolder> {
    private  List<LinearLayout> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context context;

    public NewEventAdapter(Context c, List<LinearLayout> list) {
        mList = list;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_new_event, viewGroup, false);
        Log.i("LOG", "INFLEI");
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Log.i("LOG", "onBindViewHolder()");
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Button btDate,btHour,btFinalHour;

        public MyViewHolder(View itemView) {
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