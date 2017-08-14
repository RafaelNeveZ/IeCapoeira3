package br.com.iecapoeira.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Musica;
import br.com.iecapoeira.model.Playlist;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


public class MusicaAdapter extends RecyclerView.Adapter<MusicaAdapter.MyViewHolder> {
    private List<ParseObject> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;
    private Context context;

    public MusicaAdapter(Context c, List<ParseObject> l) {
        mList = l;
        mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        context = c;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.i("LOG", "onCreateViewHolder()");
        View v = mLayoutInflater.inflate(R.layout.item_musica, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);
        return mvh;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int position) {
        Log.i("LOG", "onBindViewHolder()");
        final ParseObject musica = mList.get(position);
        myViewHolder.tvNome.setText(musica.getString("title"));
       /* if (musica.isFavorito()) {
            myViewHolder.tvAdd.setText(context.getString(R.string.icon_remove));
            myViewHolder.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ParseQuery<Playlist> query = ParseQuery.getQuery("Playlist");
                    query.fromLocalDatastore();
                    query.whereEqualTo(Playlist.USERID, IEApplication.getUser());
                    query.whereEqualTo(Playlist.MUSICA, musica.getNome());
                    query.findInBackground(new FindCallback<Playlist>() {
                        @Override
                        public void done(List<Playlist> objects, ParseException e) {
                            for (Playlist playlist : objects) {
                                playlist.unpinInBackground();
                                musica.setFavorito(false);
                                notifyDataSetChanged();
                            }
                        }
                    });
                }
            });
        } else {
            myViewHolder.tvAdd.setText(context.getString(R.string.icon_add));
            myViewHolder.tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Playlist playlist = new Playlist();
                    playlist.setMusica(musica.getNome());
                    playlist.setUser(IEApplication.getUser());
                    playlist.pinInBackground();
                    musica.setFavorito(true);
                    notifyDataSetChanged();
                }
            });
        }*/
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }


    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView tvNome;
        public TextView tvAdd;


        public MyViewHolder(View itemView) {
            super(itemView);

            tvNome = (TextView) itemView.findViewById(R.id.tv_nome);
            tvAdd = (TextView) itemView.findViewById(R.id.tv_add);
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