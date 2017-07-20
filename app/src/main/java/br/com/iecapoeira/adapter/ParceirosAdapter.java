package br.com.iecapoeira.adapter;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import br.com.iecapoeira.R;

/**
 * Created by Felipe Berbert on 07/10/2016.
 */

public class ParceirosAdapter extends RecyclerView.Adapter<ParceirosAdapter.ViewHolder> {
    public final static int TYPE_PARCEIROS = 1;
    public final static int TYPE_PATROCINADORES = 2;

    ArrayList<Drawable> list;
    int viewType;


    public ParceirosAdapter() {
        list = new ArrayList<>();
    }

    public void setPartners(ArrayList<Drawable> listPartners) {
        this.list = listPartners;
        viewType = TYPE_PARCEIROS;
        notifyDataSetChanged();
    }
    public void setSponsors(ArrayList<Drawable> listPartners) {
        this.list = listPartners;
        viewType = TYPE_PATROCINADORES;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return viewType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_PARCEIROS)
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_partner, parent, false));
        else
            return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sponsor, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivLogo;

        ViewHolder(View itemView) {
            super(itemView);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_logo);
        }

        public void bind(Drawable drawable) {
            ivLogo.setImageDrawable(drawable);
        }
    }
}
