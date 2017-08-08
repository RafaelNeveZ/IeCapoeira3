package br.com.iecapoeira.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.ParseObject;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

/**
 * Created by Felipe Berbert on 07/10/2016.
 */

public class ParceirosAdapter extends RecyclerView.Adapter<ParceirosAdapter.ViewHolder> {
    public final static int TYPE_PARCEIROS = 1;
    public final static int TYPE_PATROCINADORES = 2;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private LayoutInflater mLayoutInflater;
    private Context context;

    private  List<ParseObject> list;
    int viewType;


    public ParceirosAdapter(Context c, List<ParseObject> l) {
        list = l;
        context=c;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setPartners(List<ParseObject> listPartners) {
        this.list = listPartners;
        viewType = TYPE_PARCEIROS;
        notifyDataSetChanged();
    }
    public void setSponsors(List<ParseObject>listPartners) {
        this.list = listPartners;
        viewType = TYPE_PATROCINADORES;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == TYPE_PARCEIROS)
            return new ViewHolder( mLayoutInflater.inflate(R.layout.item_partner, viewGroup, false));
        else
            return new ViewHolder( mLayoutInflater.inflate(R.layout.item_sponsor, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final ParseObject par = list.get(position);

        holder.bind((String) par.get(Parceiro.FOTO));
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivLogo;

        public ViewHolder(View itemView) {
            super(itemView);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
            itemView.setOnClickListener(this);
        }

        public void bind(String foto) {
            byte[] decodedString = Base64.decode(foto, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivLogo.setImageBitmap(decodedByte);
            ivLogo.setBackgroundResource(android.R.color.transparent);
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}
