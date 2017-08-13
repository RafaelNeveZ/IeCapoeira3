package br.com.iecapoeira.fragment;

import android.content.Context;
import android.content.Intent;
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

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.actv.ChatActivity_;
import br.com.iecapoeira.actv.MainActivity;
import br.com.iecapoeira.actv.SalaChatActivity_;
import br.com.iecapoeira.adapter.SalaChatAdapter;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;
@EFragment(R.layout.frag_sala_chat)
public class SalaChatFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    @ViewById
    RecyclerView recyclerviewChat;

    private List<String> mList;
    SalaChatAdapter adapter;

    @AfterViews
    void init(){
           recyclerviewChat.setHasFixedSize(true);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            recyclerviewChat.setLayoutManager(llm);
            mList = getSetChatList();
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
        if (MyApplication.hasInternetConnection()) {
            startActivity(new Intent(getActivity(), ChatActivity_.class).putExtra(ChatActivity.EXTRA_ID, position + "|chat-IE-room_" + position).putExtra(ChatActivity.EXTRA_CHAT_NAME, mList.get(position)));
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }
}