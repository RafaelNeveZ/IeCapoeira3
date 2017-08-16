package br.com.iecapoeira.actv;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.icu.util.MeasureUnit;
import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.ParceirosAdapter;
import br.com.iecapoeira.fragment.EditalFragment;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.widget.ItemOffsetDecoration;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


/**
 * Created by Felipe Berbert on 07/10/2016.
 */
@EActivity(R.layout.actv_parceiros)
@OptionsMenu(R.menu.new_remove_sponso)
public class ParceirosActivity extends AppCompatActivity implements RecyclerViewOnClickListenerHack {

    @ViewById
    Toolbar toolbar;

    @ViewById
    View viewParceiros, viewPatrocinadores;

    @ViewById
    RecyclerView rvParceiros;

    @OptionsMenuItem
    MenuItem add,remove;

    private  final Context context = this;
    ArrayList<Drawable> listaParceiros;
    ArrayList<Drawable> listaPatrocinadores;
    List<ParseObject> listPar, listPatro;
    ParceirosAdapter adapter;
    GridLayoutManager layoutManager;
    private ProgressDialog progressDialog;
    private boolean firsTime = true;
    private int TABPOSITION = 0;
    private boolean firstTimeParceiro=true;
    private boolean firstTimeAdapter=true;

    @AfterViews
    void init() {
        listPar = new ArrayList<>();
        listPatro = new ArrayList<>();

        setupParceiro();

    }



    @OptionsItem
    public void remove() {

    }

    @OptionsItem(R.id.add)
       void add() {
        startActivityForResult(new Intent(this, NewParceiroActivity_.class), 10);
    }


    private void getListParceiros(){
        TABPOSITION = 0;
    /*    listaParceiros = new ArrayList<>();
        listaParceiros.add(getResources().getDrawable(R.drawable.parceiro_01));
        listaParceiros.add(getResources().getDrawable(R.drawable.parceiro_02));
        listaParceiros.add(getResources().getDrawable(R.drawable.parceiro_03));*/
        if(layoutManager!=null)
        layoutManager.setSpanCount(2);
        adapter.setPartners(listPar);
    }
    private void getListPatrocinadores(){
        TABPOSITION = 1;

      /*  listaPatrocinadores = new ArrayList<>();
        listaPatrocinadores.add(getResources().getDrawable(R.drawable.patrocinador_01));
        listaPatrocinadores.add(getResources().getDrawable(R.drawable.patrocinador_02));*/
        if(layoutManager!=null)
        layoutManager.setSpanCount(2);
        adapter.setSponsors(listPatro);
    }

    @Click(R.id.ll_parceiros)
    public void clickParceiros() {
        viewParceiros.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
        viewPatrocinadores.setBackgroundColor(getResources().getColor(R.color.white));
        if (listPar.size() != 0) {

        }else{

        }
        getListParceiros();
    }

    @Click(R.id.ll_patrocinadores)
    public void clickPatrocinadores() {
        if(firsTime){
            setupPatrocinador();
            firsTime=false;
            viewParceiros.setBackgroundColor(getResources().getColor(R.color.white));
            viewPatrocinadores.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
            getListPatrocinadores();
        }else {
            viewParceiros.setBackgroundColor(getResources().getColor(R.color.white));
            viewPatrocinadores.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
            if (listPatro.size() != 0) {

            }else{

            }
            getListPatrocinadores();
        }
    }

    public  void setupParceiro(){
        showProgress("Carregando Parceiros");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
        query.whereEqualTo(Parceiro.PARC,true);
        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> parceiro, ParseException e) {
                    listPar = parceiro;
                /*    if (listPar.size() != 0) {*/
                        if (firstTimeParceiro) {
                            if(firstTimeAdapter)
                            setupAdapter(listPar);
                            firstTimeParceiro = false;
                        }
                        clickParceiros();
                        adapter.notifyDataSetChanged();

                   /* }*/
                    dismissProgress();
                }
            });
        } catch (Exception e) {

        }

    }


    public void  setupAdapter(List<ParseObject> lis){
        rvParceiros.setHasFixedSize(true);
        layoutManager = new GridLayoutManager(context, 3);
        rvParceiros.setLayoutManager(layoutManager);
        adapter = new ParceirosAdapter(context, lis);
        adapter.setRecyclerViewOnClickListenerHack((RecyclerViewOnClickListenerHack) context);
        rvParceiros.setAdapter(adapter);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(context, R.dimen.item_offset);
        rvParceiros.addItemDecoration(itemDecoration);
        firstTimeAdapter = false;
    }

    public void  setupPatrocinador(){
        showProgress("Carregando Patrocinadores");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
        query.whereEqualTo(Parceiro.PART,true);
        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> patro, ParseException e) {
                    listPatro = patro;
                    if (listPatro.size() != 0) {
                        if(firstTimeAdapter)
                            setupAdapter(listPatro);
                        getListPatrocinadores();
                        viewParceiros.setBackgroundColor(getResources().getColor(R.color.white));
                        viewPatrocinadores.setBackgroundColor(getResources().getColor(R.color.pager_strip_border));
                        adapter.notifyDataSetChanged();

                    }
                    dismissProgress();
                }
            });
        } catch (Exception e) {

        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if (resultCode == Activity.RESULT_OK) {
                setupParceiro();
                firsTime=true;
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Algum erro aconteceu", Toast.LENGTH_SHORT).show();
            }
        }
    }












    @Override
    protected void onPostCreate(Bundle savedInstanceState) {

        super.onPostCreate(savedInstanceState);

    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            Log.d("TAG", "ADM");

            add.setVisible(true);
            remove.setVisible(false);
        } else {
            Log.d("TAG", "Não é ADM");

            add.setVisible(false);
            remove.setVisible(false);
        }
        return true;

    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClickListener(View view, final int position) {
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.delete_par);
            dialog.setTitle("Parceiro/Patrocinador");
            dialog.show();
            TextView text = (TextView) dialog.findViewById(R.id.confirm_logout);
            Button btY = (Button) dialog.findViewById(R.id.yes);
            Button btN = (Button) dialog.findViewById(R.id.no);

            if (TABPOSITION == 0)
                text.setText("Deseja deletar o Parceiro " + listPar.get(position).get(Parceiro.NAME));
            else
                text.setText("Deseja deletar o Patrocinador " + listPatro.get(position).get(Parceiro.NAME));

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
                    if (TABPOSITION == 0) {
                        showProgress("Deletando Parceiro...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
                        query.getInBackground(listPar.get(position).getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject thisParceiro, ParseException e) {
                                if (e == null) {
                                    thisParceiro.deleteInBackground();
                                    listPar.remove(position);
                                    dismissProgress();
                                    setupParceiro();
                                } else {
                                    dismissProgress();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } else {
                        showProgress("Deletando Patrocinador...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Parceiro");
                        query.getInBackground(listPatro.get(position).getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject thisPatro, ParseException e) {
                                if (e == null) {
                                    thisPatro.deleteInBackground();
                                    listPatro.remove(position);
                                    dismissProgress();
                                    firsTime=false;
                                    setupParceiro();
                                } else {
                                    dismissProgress();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });


                    }
                }
            });

        }else{
            if (TABPOSITION == 0) {
                    showAlert("Parceiro", (String) listPar.get(position).get(Parceiro.NAME));
            }else{
                    showAlert("Patrocinador", (String) listPatro.get(position).get(Parceiro.NAME));
            }
        }
    }

    public void showAlert(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
            progressDialog = ProgressDialog.show(this, getString(R.string.aguarde), text, true, false);
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
