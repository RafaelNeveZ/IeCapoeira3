package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.EditNewClassActivity_;
import br.com.iecapoeira.actv.EditNewEventActivity_;
import br.com.iecapoeira.actv.EventDetailActivity_;
import br.com.iecapoeira.actv.MyEventDetailActivity_;
import br.com.iecapoeira.actv.UserGoActivity_;
import br.com.iecapoeira.adapter.MyTimeEventoAdapter;
import br.com.iecapoeira.adapter.TimeEventoAdapter;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.UserDetails;
import br.com.iecapoeira.utils.HETextUtil;

@EFragment(R.layout.frag_event_detail)
@OptionsMenu(R.menu.event_detail)
public class MyEventDetailFragment extends Fragment {

    @ViewById
    TextView textName;

    @ViewById
    TextView textDate;

    @ViewById
    TextView textLocation;

    @ViewById
    TextView textQuantity;

    @ViewById
    TextView textDesc;

    @ViewById
    Toolbar toolbar;

    public static  Event thisEvent;

    @OptionsMenuItem(R.id.menu_edit)
    MenuItem menuEdit;

    @ViewById
    ImageView img;

    @ViewById
    LinearLayout people;

    @ViewById
    ImageView profileImg;

    public List<JSONObject> jList;
    public  boolean go = true;


    @OptionsMenuItem(R.id.menu_go)
    MenuItem menuGo;

    @OptionsMenuItem(R.id.menu_delete)
    MenuItem menuDelete;
    private ProgressDialog progressDialog;
    private MyTimeEventoAdapter adapter;

    @ViewById
    RecyclerView recyclerviewChat;

    @UiThread
    public void setImage(Bitmap picture) {
        img.setImageBitmap(picture);
    }

    @UiThread
    public void setProfilePicture(Bitmap picture) {
        profileImg.setImageBitmap(picture);
    }

    @Click
    public void people(){
        UserGoActivity_.thatEvent=thisEvent;
        Intent intent = new Intent(getActivity(), UserGoActivity_.class);
        startActivity(intent);
    }

    @AfterViews
    public void init() {
        String id = getActivity().getIntent().getStringExtra("id");
        thisEvent = ParseObject.createWithoutData(Event.class, id);
        showProgress("Carregando informações...");
        jList=new ArrayList<>();
        JSONObject jason = thisEvent.getJSONObject("eventDate");
        for(int a=0;a<7;a++) {
            try {
                jList.add(jason.getJSONObject(a+""));
                Log.d("JSON "+a,jList.get(a).getString("startTime"));
            } catch (Exception c) {
                Log.e("JSON "+a,c.getMessage());
                break;
            }
        }
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(thisEvent.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    thisEvent = (Event) object;
                    checkEvent();
                    update();
                }else{

                }
            }
        });
    }
    public  void checkEvent(){
        ParseRelation<ParseObject> relation1 = thisEvent.getRelation("eventGo");
        ParseQuery<ParseObject> qry = relation1.getQuery();
        qry.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
        qry.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if(users.size()==0 && e==null){
                    Log.d("SELECET","NÃO TEM GENTE");
                    menuGo.setIcon(R.drawable.ic_eventdetail_action_select);
                }else if(e==null) {
                    Log.d("SELECET","TEM GENTE");
                    menuGo.setIcon(R.drawable.ic_eventdetail_action_unselect);
                    go=false;
                }else{

                }
            }
        });
    }


    @UiThread
    void update() {
        textName.setText(thisEvent.get(Event.NAME).toString());
        textLocation.setText(String.format("%s\n%s, %s - %s", HETextUtil.toTitleCase(thisEvent.getAddress()), HETextUtil.toTitleCase(thisEvent.getCity()), thisEvent.getState().toUpperCase(), thisEvent.getCountry()));
        ParseRelation<ParseObject> relation = thisEvent.getRelation("eventGo");

        ParseQuery<ParseObject> qry = relation.getQuery();
        qry.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                Log.d("SIUZE", users.size() + "");
                if (users != null) {
                    if (users.size() == 1)
                        textQuantity.setText(users.size() + " pessoa irá à este evento");
                    if (users.size() == 0)
                        textQuantity.setText("Ninguém irá a este evento até o momento");
                    if (users.size() > 1)
                        textQuantity.setText(users.size() + " pessoas irão a este evento");
                    dismissProgress();
                }else {
                    dismissProgress();
                }
            }
        });

        if(thisEvent.get("Photo")!=null) {
            ParseFile image = (ParseFile) thisEvent.get("Photo");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        img.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try {
            menuGo.setIcon(thisEvent.isUserGoing(IEApplication.getUserDetails()) ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
        } catch (Exception e) {
            setIconLater();
        }
        try {
            if ((Boolean) ParseUser.getCurrentUser().get("Admin") || thisEvent.get(Event.OWNER).equals(ParseUser.getCurrentUser().getUsername())) {
                menuDelete.setVisible(true);
                menuEdit.setVisible(true);
            }else{
                menuDelete.setVisible(false);
                menuEdit.setVisible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OptionsItem
    public void menuEdit(){
                EditNewEventFragment.event=thisEvent;
                startActivityForResult(new Intent(getActivity(), EditNewEventActivity_.class), 10);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                getActivity().setResult(Activity.RESULT_OK, returnIntent);
                getActivity().finish();
            }
            if (resultCode == Activity.RESULT_FIRST_USER) {

            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            getActivity().onBackPressed();
            return true;
        }
        return false;
    }




    @UiThread
    public void setIconLater() {
        try {
            menuGo.setIcon(thisEvent.isUserGoing(IEApplication.getUserDetails()) ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
        } catch (Exception e) {
        }
        try {
            if (thisEvent.getOwner().equals(IEApplication.getUserDetails())) {
                menuDelete.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OptionsItem
    public void menuGo() {
        if(go) {
            showProgress("Se inscrevendo no evento...");
            ParseRelation<ParseObject> relation = thisEvent.getRelation("eventGo");
            relation.add(ParseUser.getCurrentUser());
            thisEvent.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                                Log.d("OK", "COLOCADO");
                                Toast.makeText(getActivity(), "Você está inscrito no evento", Toast.LENGTH_LONG).show();
                                menuGo.setIcon(R.drawable.ic_eventdetail_action_unselect);
                                go =false;
                                update();

                        dismissProgress();
                    } else {
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                        dismissProgress();
                    }
                }
            });

        }else {
            showProgress("Retirando inscrição no evento...");
            ParseRelation<ParseObject> relation = thisEvent.getRelation("eventGo");
            relation.remove(ParseUser.getCurrentUser());
            thisEvent.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                                menuGo.setIcon(R.drawable.ic_eventdetail_action_select);
                                go=true;
                                update();

                    } else {
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                        dismissProgress();
                    }
                }
            });
        }

    }


    @OptionsItem
    public void menuDelete() {
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.titulo_delete_event)
                .setPositiveButton(R.string.menu_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteEvent();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null).show();
    }

    @Background
    public void deleteEvent() {
        showProgress("Deletando evento...");
//        try {
           /* ParseQuery<Event> query = ParseQuery.getQuery("Event");
            query.getInBackground(thisEvent.getObjectId(), new GetCallback<Event>() {
                public void done(Event thisEvent, ParseException e) {*/
                    /*if (e == null) {*/
        thisEvent.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    dismissProgress();
                    getActivity().finish();
                }else{
                    dismissProgress();
                    Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                    getActivity().finish();

                }
            }
        });

                   /* }else{

                    }*/
             /*   }
            });*/
      /*  } catch (ParseException e) {
            e.printStackTrace();
        }*/

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

   /* @OptionsItem
    public void menuDenunciar() {
        final EditText edit = new EditText(getActivity());
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.titulo_denuncia)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String string = edit.getText().toString();
                        if (!string.isEmpty()) {
                            ParseObject denuncia = new ParseObject("Denuncia");
                            denuncia.put("user", IEApplication.getUserDetails());
                            denuncia.put("text", string);
                            denuncia.put("sala", thisEvent.getObjectId());
                            denuncia.saveEventually();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setView(edit).show();
    }*/

}