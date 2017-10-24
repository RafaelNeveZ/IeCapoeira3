package br.com.iecapoeira.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;

import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ClassScheduleDetailActivity;
import br.com.iecapoeira.actv.ClassScheduleDetailActivity_;
import br.com.iecapoeira.actv.MestreActivity_;
import br.com.iecapoeira.actv.MyClassAdapter;
import br.com.iecapoeira.actv.MyMestreAdapter;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Mestre;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.utils.HENetworkUtil;

@EFragment
public class MyMestreListFragment extends ListFragment {

    public static final int LIST_BY_ANGOLA = 0;
    public static final int LIST_BY_REGIONAL = 1;

    public static int LIST = 0;

    @FragmentArg
    int listType;

    public static List<ParseObject>mestre;

    @Bean
    public MyMestreAdapter adapter;
    private ProgressDialog progressDialog;

    @AfterViews
    public void init() {
        setupListView();
    }


    @UiThread
    void showToast(String msg){

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    void update() {

        try {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Mestre");
            switch (listType) {
                case LIST_BY_ANGOLA:
                    query.whereEqualTo("tipo", 0);
                    break;
                case LIST_BY_REGIONAL:
                    query.whereEqualTo("tipo", 1);
                    break;
            }
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> mestre, ParseException e) {
                    handleResult(mestre);
                }
            });

        } catch (Exception ex) {

        }

    }

    @Background
    void handleResult(List<ParseObject> mestres) {
        setupAdapter(mestres);
    }


    @UiThread
    void setupAdapter(List<ParseObject> mestres) {
        try {
            adapter.setList(mestres);
            adapter.notifyDataSetChanged();
            if (getListAdapter() == null)
                setListAdapter(adapter);
        } catch (Exception e) {}
    }



    private void setupListView() {
        ListView listView = getListView();
        listView.setFocusable(false);
        listView.setFocusableInTouchMode(false);
        listView.setClickable(false);
        listView.setItemsCanFocus(true);

        int padding = MyApplication.dpToPixels(16);
        listView.setPadding(padding, padding, padding, 0);
        listView.setDivider(null);
        listView.setDividerHeight(padding);
        setEmptyText("Não possui nada aqui");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final ParseObject item = adapter.getItem(position);
        MestreActivity_.mestre = item;
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.delete_par);
            dialog.setTitle("Mestre");
            dialog.show();
            TextView text = (TextView) dialog.findViewById(R.id.confirm_logout);
            Button btY = (Button) dialog.findViewById(R.id.yes);
            Button btN = (Button) dialog.findViewById(R.id.no);
            text.setText("O que deseja fazer com o Mestre " + item.get("nome"));
            btY.setText("Abrir");
            btY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivityForResult(new Intent(getActivity(), MestreActivity_.class), 15);
                    dialog.dismiss();
                }
            });
            btN.setText("Deletar");
            btN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                        showProgress("Deletando Mestre...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Mestre");
                        query.getInBackground(item.getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject thisPatro, ParseException e) {
                                if (e == null) {
                                    thisPatro.deleteInBackground();
                                    dismissProgress();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    dismissProgress();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

                }
            });

        }else{
            startActivityForResult(new Intent(getActivity(), MestreActivity_.class), 15);
        }

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