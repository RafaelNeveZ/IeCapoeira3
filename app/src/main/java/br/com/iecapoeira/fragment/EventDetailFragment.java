package br.com.iecapoeira.fragment;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.BaseActivity;
import br.com.hemobile.MyApplication;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.actv.ChatActivity_;
import br.com.iecapoeira.actv.EditNewEventActivity_;
import br.com.iecapoeira.actv.EventDetailActivity_;
import br.com.iecapoeira.actv.MainActivity_;
import br.com.iecapoeira.actv.NewEventActivity;
import br.com.iecapoeira.actv.UserGoActivity_;
import br.com.iecapoeira.adapter.EditalAdapter;
import br.com.iecapoeira.adapter.TimeEventoAdapter;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.EventDate;
import br.com.iecapoeira.model.UserDetails;
import br.com.iecapoeira.utils.HETextUtil;

@EFragment(R.layout.frag_event_detail)
@OptionsMenu(R.menu.event_detail)
public class EventDetailFragment extends Fragment {

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



    private final GetDataCallback callbackProfilePicture = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            if(e == null)
                setProfilePicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            else
                e.printStackTrace();

        }
    };

    private final GetDataCallback callback = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            if(e == null)
                setImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            else
                e.printStackTrace();
        }
    };

    @OptionsMenuItem(R.id.menu_go)
    MenuItem menuGo;

    @OptionsMenuItem(R.id.menu_delete)
    MenuItem menuDelete;
    private ProgressDialog progressDialog;
    private TimeEventoAdapter adapter;

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
        recyclerviewChat.setHasFixedSize(false);
        recyclerviewChat.setNestedScrollingEnabled(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerviewChat.setLayoutManager(llm);
        adapter = new TimeEventoAdapter(getActivity(), jList);
        adapter.setRecyclerViewOnClickListenerHack((EventDetailActivity_) getActivity());
        recyclerviewChat.setAdapter(adapter);
        adapter.notifyDataSetChanged();



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
        /*thisEvent.fetchIfNeededInBackground(new GetCallback<Event>() {
            @Override
            public void done(Event event, ParseException e) {
                thisEvent = event;
                try {
                    thisEvent.setOwner((UserDetails) thisEvent.getOwner().fetchIfNeeded());
                } catch (ParseException e1) {}
                if (e == null) {
                    update();
                }
            }
        });*/
    }
    public  void checkEvent(){
      /*  ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID, thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(final List<Event> models, ParseException e) {*/
                 ParseRelation<ParseObject> relation1 = thisEvent.getRelation("eventGo");
              //  ParseRelation<ParseObject> relation1 = models.get(0).getRelation("eventGo");
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
                            // Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
        /*    }
        });*/
    }





    @UiThread
    void update() {



        textName.setText(thisEvent.get(Event.NAME).toString());
        String pattern = getString(R.string.date_hour_pattern);
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
      //  textDate.setText(/*sdf.format(*//*)*/);
        textLocation.setText(String.format("%s\n%s, %s - %s", HETextUtil.toTitleCase(thisEvent.getAddress()), HETextUtil.toTitleCase(thisEvent.getCity()), thisEvent.getState().toUpperCase(), thisEvent.getCountry()));
        int howManyIsGoing = thisEvent.getHowManyIsGoing();

        /*ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> models, ParseException e) {*/
                ParseRelation<ParseObject> relation = thisEvent.getRelation("eventGo");
                //  ParseRelation<ParseObject> relation1 = models.get(0).getRelation("eventGo");
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
         /*   }
        });*/

       // textQuantity.setText(getResources().getQuantityString(R.plurals.x_pessoas_irao, howManyIsGoing, howManyIsGoing));
        UserDetails owner = null;
        try {
            owner = thisEvent.getOwner();
            textDesc.setText(thisEvent.getDescription() + "\n\n- " + owner.getName());
        } catch (Exception ex) {
            textDesc.setText(thisEvent.getDescription());
        }

        if(thisEvent.get(Event.FOTO)!=null) {
            byte[] decodedString = Base64.decode(thisEvent.get(Event.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            setImage(decodedByte);
        }



        if (owner != null) {
            Bitmap profilePicture = owner.getProfilePicture(callbackProfilePicture);
            if (profilePicture != null)
                setProfilePicture(profilePicture);
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

 /*   @OptionsItem
    public void menuChat() {
        if (MyApplication.hasInternetConnection()) {
            startActivity(new Intent(getActivity(), ChatActivity_.class).putExtra(ChatActivity.EXTRA_ID, thisEvent.getObjectId()).putExtra(ChatActivity.EXTRA_CHAT_NAME, thisEvent.getName()));
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }*/

    @OptionsItem
    public void menuGo() {

        if(go) {
            showProgress("Se inscrevendo no evento...");
        /*ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(final List<Event> models, ParseException e) {*/
            ParseRelation<ParseObject> relation = thisEvent.getRelation("eventGo");
            //   ParseRelation<ParseObject> relation = models.get(0).getRelation("eventGo");
            relation.add(ParseUser.getCurrentUser());
            thisEvent.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        ParseRelation<Event> relation = ParseUser.getCurrentUser().getRelation("eventGo");
                        relation.add(thisEvent);
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                Log.d("OK", "COLOCADO");
                                Toast.makeText(getActivity(), "Você está inscrito no evento", Toast.LENGTH_LONG).show();
                                menuGo.setIcon(R.drawable.ic_eventdetail_action_unselect);
                                go =false;
                                update();
                            }
                        });
                        dismissProgress();
                    } else {
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                        dismissProgress();
                    }
            /*        }
                });*/
                }
            });

        }else {
            showProgress("Retirando inscrição no evento...");
        /*ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(final List<Event> models, ParseException e) {*/
            ParseRelation<ParseObject> relation = thisEvent.getRelation("eventGo");
            relation.remove(ParseUser.getCurrentUser());
            thisEvent.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        ParseRelation<Event> relation = ParseUser.getCurrentUser().getRelation("eventGo");
                        relation.remove(thisEvent);
                        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                menuGo.setIcon(R.drawable.ic_eventdetail_action_select);
                                go=true;
                                update();

                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                        dismissProgress();
                    }
                }
            });
         /*   }
        });*/
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