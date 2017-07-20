package br.com.iecapoeira.fragment;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.MusicaActivity;
import br.com.iecapoeira.actv.MusicaDetalheActivity_;
import br.com.iecapoeira.adapter.MusicaAdapter;
import br.com.iecapoeira.model.Musica;
import br.com.iecapoeira.model.Playlist;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


@EFragment(R.layout.frag_musica)
public class MusicaFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    @ViewById
    RecyclerView recyclerviewMusica;

    @FragmentArg
    boolean favoritos;

    private ProgressDialog progressDialog;

    private List<Musica> mList;
    private List<Playlist> mListFavoritos;



    @AfterViews
    void init(){
        recyclerviewMusica.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewMusica.setLayoutManager(llm);
        mList = ((MusicaActivity) getActivity()).getSetMusicaList();
        ParseQuery<Playlist> query = ParseQuery.getQuery("Playlist");
        query.fromLocalDatastore();
        showProgress(getString(R.string.loading_data));
        query.whereEqualTo(Playlist.USERID, IEApplication.getUser());
        query.findInBackground(new FindCallback<Playlist>() {
            @Override
            public void done(List<Playlist> objects, ParseException e) {
                dismissProgress();
                if (e == null) {
                    mListFavoritos = objects;
                    updateFavoritos();
                } else {
                    Log.d("Playlist query", "Error: " + e.getMessage());
                }
                MusicaAdapter adapter;
                if (favoritos) {
                    List<Musica> musicasFavoritas = new ArrayList<>();
                    for (Musica musica : mList)
                        if (musica.isFavorito()) musicasFavoritas.add(musica);
                    adapter = new MusicaAdapter(getActivity(), musicasFavoritas);
                } else
                    adapter = new MusicaAdapter(getActivity(), mList);
                adapter.setRecyclerViewOnClickListenerHack(MusicaFragment.this);
                recyclerviewMusica.setAdapter(adapter);
            }
        });

    }

    private void updateFavoritos(){
        for (Musica musica : mList){
            for (Playlist favorito : mListFavoritos) {
                if (musica.getNome().toLowerCase().equals(favorito.getMusica().toLowerCase()))
                    musica.setFavorito(true);
            }
        }
    }
    @Override
    public void onClickListener(View view, int position) {
        MusicaDetalheActivity_.intent(getContext()).musica(mList.get(position)).start();
    }

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.aguarde), text, true, false);
        } catch (Exception e) { e.printStackTrace(); }

    }

    @UiThread
    public void dismissProgress() {
        if (progressDialog != null) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) { e.printStackTrace(); }

        }
    }
}