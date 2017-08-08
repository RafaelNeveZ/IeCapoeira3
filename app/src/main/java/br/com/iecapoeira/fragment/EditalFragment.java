package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.NewEditalActivity_;
import br.com.iecapoeira.adapter.EditalAdapter;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EFragment(R.layout.frag_sala_chat)
@OptionsMenu(R.menu.new_remove_sponso)
public class EditalFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    @ViewById
    RecyclerView recyclerviewChat;

    private List<ParseObject> mList;

    public    EditalAdapter adapter;
    private  final Context context = getActivity();

    @OptionsMenuItem
    MenuItem add, remove;
    private Dialog progressDialog;

    @AfterViews
    void init(){
        mList = new ArrayList<>();
            setupEdital();



    }

    public void setupEdital(){
        showProgress("Carregando Editais");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Edital");
        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> edit, ParseException e) {
                    mList = edit;
                    Log.d("SIZE",mList.size() +"");
                        recyclerviewChat.setHasFixedSize(false);
                        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                        recyclerviewChat.setLayoutManager(llm);
                        adapter = new EditalAdapter(getActivity(), mList);
                        adapter.setRecyclerViewOnClickListenerHack(EditalFragment.this);
                        recyclerviewChat.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    dismissProgress();
                }
            });
        } catch (Exception e) {
            dismissProgress();
        }
    }

    public void updateEdital(){
        showProgress("Carregando Editais");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Edital");
        try {
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> edit, ParseException e) {
                    mList = edit;
                    adapter.notifyDataSetChanged();
                    dismissProgress();
                }
            });
        } catch (Exception e) {
            dismissProgress();
        }
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


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                setupEdital();
                adapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onPrepareOptionsMenu (Menu menu) {
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            Log.d("TAG", "ADM");

            add.setVisible(true);
            remove.setVisible(false);

        } else {
            Log.d("TAG", "Não é ADM");

            add.setVisible(false);
            remove.setVisible(false);
        }
    }


    @Override
    public void onClickListener(View view, int position) {
        final int pos = position;
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            final Dialog dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.delete_edit);
            dialog.setTitle("Edital");
            dialog.show();

            Button btY = (Button) dialog.findViewById(R.id.yes);
            Button btN = (Button) dialog.findViewById(R.id.no);

            btY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mList.get(pos).get(Edital.LINK).toString()));
                    startActivity(intent);
                    dialog.dismiss();
                }
            });
            btN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                        showProgress("Deletando edital...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Edital");
                        query.getInBackground(mList.get(pos).getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject thisParceiro, ParseException e) {
                                if (e == null) {
                                    thisParceiro.deleteInBackground();
                                    mList.remove(pos);
                                    dismissProgress();
                                    setupEdital();
                                } else {
                                    dismissProgress();
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
            });
        }else{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + mList.get(position).get(Edital.LINK).toString()));
            startActivity(intent);
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