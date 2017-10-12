package br.com.iecapoeira.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
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

import java.util.List;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.MyMusicaAdapter;
import br.com.iecapoeira.actv.MyVideoAdapter;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HENetworkUtil;
import br.com.iecapoeira.view.MusicaItemView;

@EFragment
public class MyVideoListFragment extends ListFragment {

    public static final int LIST_BY_CAPOEIRA = 0;

    public static final String TYPE_ANGOLA = "0";

    public static final String TYPE_REGIONAL = "1";
    public static final String TYPE_PLAYLIST = "2";

    public static int LIST = 0;
    List<ParseObject> updatade_videos;
    public
    @FragmentArg
    int listType;


    @Bean
    public MyVideoAdapter adapter;
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

        if(!HENetworkUtil.isOnline(getActivity())){
            showToast(getString(R.string.msg_erro_sem_conexao));
            return;
        }

    ;
        try {
            Log.d("TAG","" + listType);
            switch (listType) {
                case LIST_BY_CAPOEIRA:
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Video");
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> videos, ParseException e) {
                            updatade_videos=videos;
                            handleResult(updatade_videos, e);
                        }
                    });
                    break;
            }
        } catch (Exception ex) {
        }

    }

    @Background
    void handleResult(List<ParseObject> videos, ParseException e) {
        if(e==null)
        setupAdapter(videos);
    }


    @UiThread
    void setupAdapter(List<ParseObject> videos) {
        try {
            adapter.setList(videos);
            adapter.notifyDataSetChanged();
            if (getListAdapter() == null)
                setListAdapter(adapter);
        } catch (Exception e) {}
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
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
        setEmptyText("Não possui vídeos aqui");
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        final ParseObject item = adapter.getItem(position);
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            final Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.delete_edit);
            dialog.setTitle("Vídeo");
            dialog.show();
            TextView text = (TextView) dialog.findViewById(R.id.confirm_logout);
            Button btY = (Button) dialog.findViewById(R.id.yes);
            Button btN = (Button) dialog.findViewById(R.id.no);
            text.setText("Deseja deletar o vídeo " + item.get("title")+"?");
            btY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(""+ item.getString("link"))));
                }
            });
            btN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                        showProgress("Deletando vídeo...");
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("Video");
                        query.getInBackground(item.getObjectId(), new GetCallback<ParseObject>() {
                            public void done(ParseObject item, ParseException e) {
                                if (e == null) {
                                    item.deleteInBackground();
                                    dismissProgress();
                                    Toast.makeText(getActivity(), "Vídeo deletado", Toast.LENGTH_LONG).show();
                                } else {
                                    dismissProgress();
                                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                }
            });

        }else{
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(""+ item.getString("link"))));
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