package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EFragment(R.layout.frag_sala_chat)
@OptionsMenu(R.menu.new_remove_sponso)
public class EditalFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    @ViewById
    RecyclerView recyclerviewChat;

    private List<Edital> mList;


    @AfterViews
    void init(){
           recyclerviewChat.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerviewChat.setLayoutManager(llm);
            mList = setEditalList();
            EditalAdapter adapter;
            adapter = new EditalAdapter(getActivity(), mList);
            adapter.setRecyclerViewOnClickListenerHack(EditalFragment.this);
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


    @OptionsItem
    public void add() {
        startActivityForResult(new Intent(getActivity(), NewEditalActivity_.class), 10);
    }

    @OptionsItem
    public void remove() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                String title = data.getStringExtra("result");
                String link=data.getStringExtra("link");
                Edital edital = new Edital(title,link);
                Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
                mList.add(edital);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClickListener(View view, int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mList.get(position).getLInk()));
        startActivity(intent);
    }
}