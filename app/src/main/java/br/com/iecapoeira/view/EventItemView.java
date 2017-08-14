package br.com.iecapoeira.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ClassScheduleActivity;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HETextUtil;

@EViewGroup(R.layout.item_event)
public class EventItemView extends ItemView<Event> {

    @ViewById
    TextView textName;

    @ViewById
    TextView textDate,textTime;

    @ViewById
    TextView textLocation;

    @ViewById
    ImageView btGoing;

    private Event obj;

    public List<JSONObject> jList;

    @ViewById
    ImageView img;
    private ProgressDialog progressDialog;

    public EventItemView(Context context) {
        super(context);
    }

   /* private final GetDataCallback callback = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            setProfilePicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    };*/
   public  boolean go = true;
    @UiThread
    public void setProfilePicture(Bitmap picture) {
        img.setImageBitmap(picture);
    }

    @Override
    public void bind(Event obj, int positionj) {
        this.obj = obj;
        checkEvents();
        //  IEApplication.getUserDetails();
        textName.setText(obj.getName());
        String pattern = getContext().getString(R.string.date_pattern);
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        jList=new ArrayList<>();
        JSONObject jason = obj.getJSONObject("eventDate");
        for(int a=0;a<7;a++) {
            try {
                jList.add(jason.getJSONObject(a+""));
                Log.d("JSON "+a,jList.get(a).getString("startTime"));
            } catch (Exception c) {
                Log.e("JSON "+a,c.getMessage());
                break;
            }
        }
        try {
            textDate.setText(jList.get(0).getString("date") + "");
            textTime.setText("Das "+jList.get(0).getString("startTime")+" às "+jList.get(0).getString("endTime")+" horas");
        }catch (Exception c){

        }
        textLocation.setText(String.format("%s, %s - %s", HETextUtil.toTitleCase(obj.getCity()), obj.getState(), obj.getCountry()));
        /*Bitmap picture = obj.getProfilePicture(callback);
        if (picture != null) {
            setProfilePicture(picture);
        }
        else {*/
        if(obj.get(Event.FOTO)!=null) {
            byte[] decodedString = Base64.decode(obj.get(Event.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img.setImageBitmap(decodedByte);
        }

    }

    public  void checkEvents(){
        /*ParseQuery<Event> query = ParseQuery.getQuery("Event");
        query.whereEqualTo(Event.OBJECTID,obj.getObjectId());
        query.findInBackground(new FindCallback<Event>() {*/
           /* @Override
            public void done(List<Event> models, ParseException e) {*/
                //ParseRelation<ParseObject> relation = models.get(0).getRelation("eventGo");
                ParseRelation<ParseObject> relation = obj.getRelation("eventGo");
                ParseQuery<ParseObject> qry = relation.getQuery();
                qry.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                qry.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> users, ParseException e) {
                        if(users!=null) {
                            if (users.size() > 0) {
                                for (int a = 0; a < users.size(); a++) {
                                    Log.d("EVENT NAME", users.get(a).get(Event.NAME).toString());
                                }
                                setImageBtGoing(true);
                                //  go=false;
                            }else{
                                setImageBtGoing(false);
                            }
                        }
                    }
                });
        //    }
      //  });
    }
 /*  public void subscribe(){
       ParseQuery<Event> query = ParseQuery.getQuery("Event");
       query.whereEqualTo(Event.OBJECTID,obj.getObjectId());
       query.findInBackground(new FindCallback<Event>() {
           @Override
           public void done(List<Event> models, ParseException e) {
               ParseRelation<ParseObject> relation = models.get(0).getRelation("eventGo");
               relation.add(ParseUser.getCurrentUser());
               models.get(0).saveInBackground(new SaveCallback() {
                   public void done(ParseException e) {
                       if (e == null) {
                           setImageBtGoing(true);
                           go=false;
                       } else {

                       }
                   }
               });
           }
       });
   }

   public  void unSubscribe(){
       ParseQuery<Event> query = ParseQuery.getQuery("Event");
       query.whereEqualTo(Event.OBJECTID,obj.getObjectId());
       query.findInBackground(new FindCallback<Event>() {
           @Override
           public void done(List<Event> models, ParseException e) {
               ParseRelation<ParseObject> relation = models.get(0).getRelation("eventGo");
               relation.remove(ParseUser.getCurrentUser());
               models.get(0).saveInBackground(new SaveCallback() {
                   public void done(ParseException e) {
                       if (e == null) {
                           setImageBtGoing(false);
                           go=true;
                       } else {

                       }
                   }
               });
           }
       });
   }*/
    /*@Click
    public void btGoing() {

        if(go) {
            subscribe();

        }else {
            unSubscribe();
        }

    }*/

    @Background
    void changeGoingOnParse(Boolean userGoing) {
      /*  try {
            obj.setUserGoing(IEApplication.getUserDetails(), userGoing);
        } catch (ParseException e) {
            e.printStackTrace();
            setImageBtGoing(!userGoing);
        }*/
    }

    @UiThread
    void setImageBtGoing(boolean userGoing) {
        btGoing.setImageResource(userGoing ? R.drawable.ic_eventlist_cell_confirm_unselect : R.drawable.ic_eventlist_cell_confirm_select);
    }


}