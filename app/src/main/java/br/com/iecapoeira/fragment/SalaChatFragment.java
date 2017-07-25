package br.com.iecapoeira.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.MainActivity;
import br.com.iecapoeira.actv.SalaChatActivity_;
import br.com.iecapoeira.adapter.SalaChatAdapter;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;
@EFragment(R.layout.frag_sala_chat)
public class SalaChatFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    @ViewById
    RecyclerView recyclerviewChat;

    private List<String> mList;


    @AfterViews
    void init(){
           recyclerviewChat.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerviewChat.setLayoutManager(llm);
            mList = getSetChatList();
            SalaChatAdapter adapter;
            adapter = new SalaChatAdapter(getActivity(), mList);
            adapter.setRecyclerViewOnClickListenerHack(SalaChatFragment.this);
            recyclerviewChat.setAdapter(adapter);

    }


    public List<String> getSetChatList(){
        String[] chat = {
                "Capoeira de Angola",
                "Capoeira Regional",
                "Capoeira de Rua",
                "História de Capoeira",
        };

        List<String> listAux = new ArrayList<>();

        for(int i = 0; i < chat.length; i++){
            String s = chat[i];
            Log.i("LOG", s);
            listAux.add(s);
        }
        return(listAux);
    }


    @Override
    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), mList.get(position), Toast.LENGTH_SHORT).show();
        Log.e("TAG", mList.get(position));
    }
}