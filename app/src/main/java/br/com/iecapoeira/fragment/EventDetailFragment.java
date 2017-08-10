package br.com.iecapoeira.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.text.SimpleDateFormat;
import java.util.List;

import br.com.hemobile.BaseActivity;
import br.com.hemobile.MyApplication;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.actv.ChatActivity_;
import br.com.iecapoeira.model.Event;
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

    @ViewById
    ImageView img;

    @ViewById
    ImageView profileImg;

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

    @UiThread
    public void setImage(Bitmap picture) {
        img.setImageBitmap(picture);
    }

    @UiThread
    public void setProfilePicture(Bitmap picture) {
        profileImg.setImageBitmap(picture);
    }

    @AfterViews
    public void init() {

        String id = getActivity().getIntent().getStringExtra("id");
        thisEvent = ParseObject.createWithoutData(Event.class, id);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(thisEvent.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    thisEvent = (Event) object;
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




    @Click
    public void profileImg(){
        menuDelete() ;
    }
    @UiThread
    void update() {
        textName.setText(thisEvent.get(Event.NAME).toString());
        String pattern = getString(R.string.date_hour_pattern);
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        textDate.setText(sdf.format(thisEvent.getDate()));
        textLocation.setText(String.format("%s\n%s, %s - %s", HETextUtil.toTitleCase(thisEvent.getAddress()), HETextUtil.toTitleCase(thisEvent.getCity()), thisEvent.getState().toUpperCase(), thisEvent.getCountry()));
        int howManyIsGoing = thisEvent.getHowManyIsGoing();

        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> models, ParseException e) {

                ParseRelation<ParseObject> relation = models.get(0).getRelation("eventgo");
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
                        }
                    }
                });
            }
        });

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
            if (thisEvent.getOwner().equals(IEApplication.getUserDetails())) {
                menuDelete.setVisible(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
    public void menuChat() {
        if (MyApplication.hasInternetConnection()) {
            startActivity(new Intent(getActivity(), ChatActivity_.class).putExtra(ChatActivity.EXTRA_ID, thisEvent.getObjectId()).putExtra(ChatActivity.EXTRA_CHAT_NAME, thisEvent.getName()));
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    @OptionsItem
    public void menuGo() {
        if(go) {
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> models, ParseException e) {
                ParseRelation<ParseObject> relation = models.get(0).getRelation("eventgo");
                relation.remove(ParseUser.getCurrentUser());
                models.get(0).saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            menuGo.setIcon(go ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
                            go=false;
                            update();
                        } else {

                        }
                    }
                });
            }
        });

    }else {
        ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,thisEvent.getObjectId());
        query.findInBackground(new FindCallback<Event>() {
            @Override
            public void done(List<Event> models, ParseException e) {
                ParseRelation<ParseObject> relation = models.get(0).getRelation("eventgo");
                relation.remove(ParseUser.getCurrentUser());
                models.get(0).saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            menuGo.setIcon(go ? R.drawable.ic_eventdetail_action_unselect : R.drawable.ic_eventdetail_action_select);
                            go=true;
                            update();
                        } else {

                        }
                    }
                });
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
        ((BaseActivity)getActivity()).showProgress(getString(R.string.aguarde));
        try {
            ParseQuery<Event> query = ParseQuery.getQuery("Event");
            query.getInBackground(thisEvent.getObjectId(), new GetCallback<Event>() {
                public void done(Event thisEvent, ParseException e) {
                    if (e == null) {
                        thisEvent.deleteInBackground();
                        getActivity().finish();
                    }else{

                    }
                }
            });
            thisEvent.delete();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        ((BaseActivity)getActivity()).dismissProgress();
        getActivity().finish();
    }

    @OptionsItem
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
    }

}