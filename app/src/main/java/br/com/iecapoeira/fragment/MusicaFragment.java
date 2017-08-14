package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.MusicaActivity;
import br.com.iecapoeira.actv.MusicaDetalheActivity_;
import br.com.iecapoeira.actv.NewEventActivity_;
import br.com.iecapoeira.actv.NewMusicActivity_;
import br.com.iecapoeira.adapter.MusicaAdapter;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Musica;
import br.com.iecapoeira.model.Playlist;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


@EFragment(R.layout.frag_musica)
@OptionsMenu(R.menu.main)
public class MusicaFragment extends Fragment implements RecyclerViewOnClickListenerHack {
    public static final int ANGOLA =0;
    public static final int REGIONAL =1;
    public static final int PLAYLIST =2;
    @ViewById
    RecyclerView recyclerviewMusica;

    @FragmentArg
    boolean favoritos;

    @OptionsMenuItem(R.id.new_event)
    MenuItem newsong;

    private ProgressDialog progressDialog;

    private List<ParseObject> mList;
    private List<Playlist> mListFavoritos;
    MusicaAdapter adapter;


    @AfterViews
    void init(){
        mList=new ArrayList<>();
        recyclerviewMusica.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewMusica.setLayoutManager(llm);
        showProgress(getString(R.string.loading_data));
        ParseQuery<ParseObject> query= ParseQuery.getQuery("Music");
        //query.whereEqualTo("tyoe",ANGOLA);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listAux, ParseException e) {
                if (e == null) {
                    mList = listAux;
                    adapter = new MusicaAdapter(getActivity(), mList);
                    adapter.setRecyclerViewOnClickListenerHack(MusicaFragment.this);
                    recyclerviewMusica.setAdapter(adapter);
                    dismissProgress();

                    /*ParseRelation<ParseObject>user=
                    query.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                    query.findInBackground(new FindCallback<Playlist>() {
                        @Override
                        public void done(List<Playlist> objects, ParseException e) {

                        }*/
                      /*  @Override
                        public void done(List<Playlist> objects, ParseException e) {
                            dismissProgress();
                            if (e == null) {
                                mListFavoritos = objects;
                                updateFavoritos();
                            } else {
                                Log.d("Playlist query", "Error: " + e.getMessage());
                            }

                            if (favoritos) {
                                List<Musica> musicasFavoritas = new ArrayList<>();
                                for (ParseObject musica : mList)
                                    if (musica.isFavorito()) musicasFavoritas.add(musica);
                                adapter = new MusicaAdapter(getActivity(), musicasFavoritas);
                            } else
                                adapter = new MusicaAdapter(getActivity(), mList);

                            adapter.setRecyclerViewOnClickListenerHack(MusicaFragment.this);
                            recyclerviewMusica.setAdapter(adapter);
                        }
                    });

                }else{

                }
            }*/
                }
            }
        });

    }

    @OptionsItem
    public void newEvent() {
        startActivityForResult(new Intent(getActivity(), NewMusicActivity_.class), 10);
    }

    /*private void updateFavoritos(){
        for (Musica musica : mList){
            for (Playlist favorito : mListFavoritos) {
                if (musica.getNome().toLowerCase().equals(favorito.getMusica().toLowerCase()))
                    musica.setFavorito(true);
            }
        }
    }*/



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        try {
            if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
                Log.d("TAG", "ADM");
                newsong.setVisible(true);
            }else{
                newsong.setVisible(false);
                Log.d("TAG", "Não é ADM");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                String title=data.getStringExtra("title");
                String link=data.getStringExtra("link");
                ParseObject mus = ParseObject.create("Music");
                mus.put("title",title);
                mus.put("link",link);
                mList.add(mus);
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    @Override
    public void onClickListener(View view, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(""+ mList.get(position).getString("link"))));
        //MusicaDetalheActivity_.intent(getContext()).musica(mList.get(position)).start();
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