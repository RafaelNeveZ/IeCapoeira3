package br.com.iecapoeira.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.io.File;
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

        holder.bind(par);
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

        public void bind(ParseObject obj) {
            if(obj.get("Photo")!=null) {
                ParseFile image = (ParseFile) obj.get("Photo");
                image.getDataInBackground(new GetDataCallback() {
                    public void done(byte[] data, ParseException e) {
                        if (e == null) {
                            Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                            ivLogo.setImageBitmap(bmp);
                        } else {
                            Log.d("test", "There was a problem downloading the data.");
                        }
                    }
                });
            }
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getAdapterPosition());
            }
        }
    }
}
