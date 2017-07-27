package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewEditalActivity_;
import br.com.iecapoeira.adapter.EditalAdapter;
import br.com.iecapoeira.adapter.NewEventAdapter;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EFragment(R.layout.frag_new_event)
public class NewEventFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    @ViewById
    RecyclerView recyclerviewChat;

    private List<LinearLayout> mList;


    @AfterViews
    void init(){
           recyclerviewChat.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerviewChat.setLayoutManager(llm);
            NewEventAdapter adapter;
            adapter = new NewEventAdapter(getActivity(),mList);
            adapter.setRecyclerViewOnClickListenerHack(NewEventFragment.this);
            recyclerviewChat.setAdapter(adapter);
    }

    public List<Edital> setEditalList(){
        String[] title = {
                "Prosas"
        };
        String[] link = {
                "prosas.com.br/editais"
        };

        List<Edital> listAux = new ArrayList<>();

        for(int i = 0; i < title.length; i++){
            Edital e = new Edital(title[i % title.length], link[i % link.length]);
            listAux.add(e);
        }
        return(listAux);
    }




    @Override
    public void onClickListener(View view, int position) {

    }
}