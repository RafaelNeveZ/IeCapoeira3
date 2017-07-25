package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.EditalFragment;
import br.com.iecapoeira.fragment.EditalFragment_;
import br.com.iecapoeira.fragment.SalaChatFragment_;

@EActivity(R.layout.activity_edital)
public class EditalActivity extends AppCompatActivity {


    @AfterViews
    public void init() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.content, EditalFragment_.builder().build()).commit();
    }

}
