package br.com.iecapoeira.actv;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.MusicaAdapter;
import br.com.iecapoeira.adapter.SalaChatAdapter;
import br.com.iecapoeira.fragment.EventDetailFragment_;
import br.com.iecapoeira.fragment.MusicaFragment;
import br.com.iecapoeira.fragment.SalaChatFragment_;
import br.com.iecapoeira.model.Musica;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EActivity(R.layout.activity_sala_chat)
public class SalaChatActivity extends AppCompatActivity {

    @AfterViews
    public void init() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, SalaChatFragment_.builder().build()).commit();
    }

}
