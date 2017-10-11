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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    TextView textDate,textHour;

    @ViewById
    TextView textLocation;

    @ViewById
    TextView textQuantity;

    @ViewById
    TextView textDesc;

    @ViewById
    Toolbar toolbar;

    public Event thisEvent;

    @OptionsMenuItem(R.id.menu_edit)
    MenuItem menuEdit;

    @ViewById
    ImageView img;

    @ViewById
    LinearLayout people;

    @ViewById
    ImageView profileImg;

    Date  eventStartdate, eventFinaldate, timeStart,timeEnd;
    public  boolean go = true;

    @OptionsMenuItem(R.id.menu_go)
    MenuItem menuGo;

    @OptionsMenuItem(R.id.menu_delete)
    MenuItem menuDelete;
    private ProgressDialog progressDialog;
        public boolean owner = false;


    @AfterViews
    public void init() {
        String id = getActivity().getIntent().getStringExtra("id");
        thisEvent = ParseObject.createWithoutData(Event.class, id);
        showProgress("Carregando informações...");
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

    @Click
    public void people(){
        UserGoActivity_.thatEvent=thisEvent;
        Intent intent = new Intent(getActivity(), UserGoActivity_.class);
        startActivity(intent);
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
        eventStartdate =new Date();
        eventFinaldate =new Date();
        timeEnd = new Date();
        timeStart = new Date();
        eventStartdate = (Date) thisEvent.get("startDate");
        eventFinaldate = (Date) thisEvent.get("endDate");
        timeStart = (Date) thisEvent.get("startTime");
        timeEnd = (Date) thisEvent.get("endTime");
        int hourInit=timeStart.getHours();
        int hourFim=timeEnd.getHours();
        int minuteInit=timeStart.getMinutes();
        int minuteFim=timeEnd.getMinutes();

        String end = "";
        String start =((eventStartdate.getDate()<10)?"0"+eventStartdate.getDate(): eventStartdate.getDate()) + "/" + (((eventStartdate.getMonth()+1)<10)?"0"+(eventStartdate.getMonth()+1): (eventStartdate.getMonth()+1))  + "/" + (eventStartdate.getYear()+1900);
        if((boolean)thisEvent.get("hasMoreDays"))
            end =((eventFinaldate.getDate()<10)?"0"+eventFinaldate.getDate(): eventFinaldate.getDate()) + "/" + (((eventFinaldate.getMonth()+1)<10)?"0"+(eventFinaldate.getMonth()+1): (eventFinaldate.getMonth()+1))  + "/" + (eventFinaldate.getYear()+1900);
        textDate.setText(end.equals("")? start:start+" à "+end);
        textHour.setText(((hourInit<10)?"0"+hourInit: hourInit)+":"+((minuteInit<10)?"0"+minuteInit:minuteInit)+ " às "+ ((hourFim<10)?"0"+hourFim: hourFim)+":"+((minuteFim<10)?"0"+minuteFim:minuteFim));
        textDesc.setText(thisEvent.getDescription());

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
        menuEdit.setVisible(false);
        menuDelete.setVisible(false);
        ParseRelation<ParseObject> relation1 = thisEvent.getRelation("eventOwner");
        ParseQuery<ParseObject> qry = relation1.getQuery();
        qry.whereEqualTo("objectId", ParseUser.getCurrentUser().getObjectId());
        qry.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if (users.size() == 0 && e == null) {
                    owner = false;
                } else if (e == null) {
                    owner = true;
                } else {
                }
                if ((Boolean) ParseUser.getCurrentUser().get("Admin") || owner) {
                    Log.d("TAG", "ADM");
                    menuEdit.setVisible(true);
                    menuDelete.setVisible(true);
                } else {
                    Log.d("TAG", "Não é ADM");

                }
            }
        });
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
            if (resultCode == Activity.RESULT_CANCELED) {

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
                        thisEvent.deleteInBackground(new DeleteCallback() {
                            @Override
                            public void done(ParseException e) {
                                if(e==null){
                                    dismissProgress();
                                    getActivity().finish();
                                }else{
                                    dismissProgress();
                                    Toast.makeText(getActivity(), "Erro", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
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