package br.com.iecapoeira.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
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
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.UiThread;

import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.EventAdapter;
import br.com.iecapoeira.actv.EventDetailActivity_;
import br.com.iecapoeira.actv.MyParceiroAdapter;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.utils.HENetworkUtil;

@EFragment
public class MyParceirosListFragment extends ListFragment {

    public static final int LIST_BY_PARCEIROS = 0;
    public static final int LIST_BY_PATROCINADOR = 1;
    public static final String TYPE_PARCEIRO = "0";
    public static final String TYPE_PATROCINADOR = "1";

    public static int LIST = 0;
    public boolean parceiro=true;
    @FragmentArg
    int listType;

    @FragmentArg
    String filter;

    @Bean
    MyParceiroAdapter adapter;
    private ProgressDialog progressDialog;

    @AfterViews
    public void init() {
        setupListView();
    }
    public static String notUpdate=null;

    @UiThread
    void showToast(String msg){

        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    void update(String filter) {

        if(!HENetworkUtil.isOnline(getActivity())){
            showToast(getString(R.string.msg_erro_sem_conexao));
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
//        query.include(Event.OWNER);
        try {
            Log.d("FILTER","" + filter);
            switch (listType) {
                case LIST_BY_PARCEIROS:
 //                   query.whereGreaterThan(Event.DATE, Calendar.getInstance().getTime());
                    query.whereEqualTo("parceiros", true);
                    parceiro=true;

                    break;
                case LIST_BY_PATROCINADOR:
 //                query.whereGreaterThan(Event.DATE, Calendar.getInstance().getTime());
                    query.whereEqualTo("patrocinador", true);
                    parceiro=false;
                    break;
            }
           // query.orderByAscending(Event.DATE);
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> part, ParseException e) {

                    handleResult(part, e);
                }
            });
        } catch (Exception e) {

        }
    }

    @Background
    void handleResult(List<ParseObject> part, ParseException e) {
        /*if (e == null) {
            for (Event event : events) {
                getUsersGoing(event);
            }
        }*/
        notUpdate=null;
        setupAdapter(part);
    }


    @UiThread
    void setupAdapter(List<ParseObject> part) {
        try {
            adapter.setList(part);
            adapter.notifyDataSetChanged();
            if (getListAdapter() == null)
                setListAdapter(adapter);
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        if (notUpdate != null){
            update(notUpdate);
            }else{
            update("");
        }
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
        setEmptyText("Não possui.");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final ParseObject item = adapter.getItem(position);
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.delete_par);
            dialog.setTitle("Parceiro/Patrocinador");
            dialog.show();
            TextView text = (TextView) dialog.findViewById(R.id.confirm_logout);
            Button btY = (Button) dialog.findViewById(R.id.yes);
            Button btN = (Button) dialog.findViewById(R.id.no);

            if (parceiro)
                text.setText("Deseja deletar o Parceiro " + item.get(Parceiro.NAME));
            else
                text.setText("Deseja deletar o Patrocinador " + item.get(Parceiro.NAME));

            btN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if (parceiro) {
                        showProgress("Deletando Parceiro...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
                        query.getInBackground(item.getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject thisParceiro, ParseException e) {
                                if (e == null) {
                                    thisParceiro.deleteInBackground();
                                    dismissProgress();
                                    adapter.notifyDataSetChanged();
                                } else {
                                    dismissProgress();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        showProgress("Deletando Patrocinador...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
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
                }
            });

        }else{
            if (parceiro) {
                showAlert("Parceiro", (String) item.get(Parceiro.NAME));
            }else{
                showAlert("Patrocinador", (String) item.get(Parceiro.NAME));
            }
        }
    }

    public void showAlert(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
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