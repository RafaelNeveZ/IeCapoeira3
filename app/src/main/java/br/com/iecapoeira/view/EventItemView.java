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
import com.parse.ParseFile;
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
import java.util.Date;
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
    Date  eventStartdate, eventFinaldate, timeStart,timeEnd;
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
       // checkEvents();
        eventStartdate =new Date();
        eventFinaldate =new Date();
        timeEnd = new Date();
        timeStart = new Date();
        eventStartdate = (Date) obj.get("startDate");
        eventFinaldate = (Date) obj.get("endDate");
        timeStart = (Date) obj.get("startTime");
        timeEnd = (Date) obj.get("endTime");



        //  IEApplication.getUserDetails();
        textName.setText(obj.getName());
        int hourInit=timeStart.getHours();
        int hourFim=timeEnd.getHours();
        int minuteInit=timeStart.getMinutes();
        int minuteFim=timeEnd.getMinutes();




            String end = "";
            String start =((eventStartdate.getDate()<10)?"0"+eventStartdate.getDate(): eventStartdate.getDate()) + "/" + (((eventStartdate.getMonth()+1)<10)?"0"+(eventStartdate.getMonth()+1): (eventStartdate.getMonth()+1))  + "/" + (eventStartdate.getYear()+1900);
            if((boolean)obj.get("hasMoreDays"))
            end =((eventFinaldate.getDate()<10)?"0"+eventFinaldate.getDate(): eventFinaldate.getDate()) + "/" + (((eventFinaldate.getMonth()+1)<10)?"0"+(eventFinaldate.getMonth()+1): (eventFinaldate.getMonth()+1))  + "/" + (eventFinaldate.getYear()+1900);
            textDate.setText(end.equals("")? start:start+" à "+end);

            textTime.setText(((hourInit<10)?"0"+hourInit: hourInit)+":"+((minuteInit<10)?"0"+minuteInit:minuteInit)+ " às "+ ((hourFim<10)?"0"+hourFim: hourFim)+":"+((minuteFim<10)?"0"+minuteFim:minuteFim));
        textLocation.setText(String.format("%s, %s - %s", HETextUtil.toTitleCase(obj.getCity()), obj.getState(), obj.getCountry()));
        /*Bitmap picture = obj.getProfilePicture(callback);
        if (picture != null) {
            setProfilePicture(picture);
        }
        else {*/
       /* if(obj.get(Event.FOTO)!=null) {
            byte[] decodedString = Base64.decode(obj.get(Event.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            img.setImageBitmap(decodedByte);
        }*/
        if(obj.get("Photo")!=null) {
            ParseFile image = (ParseFile) obj.get("Photo");
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

    public  void checkEvents(){

                ParseRelation<ParseObject> relation = obj.getRelation("eventGo");
                ParseQuery<ParseObject> qry = relation.getQuery();
                qry.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                qry.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> users, ParseException e) {
                        if(users!=null) {
                            if (users.size() > 0) {
                                for (int a = 0; a < users.size(); a++) {
                                }
                                setImageBtGoing(true);

                            }
                        }
                    }
                });

    }
    @UiThread
    void setImageBtGoing(boolean userGoing) {
        btGoing.setImageResource(userGoing ? R.drawable.ic_eventlist_cell_confirm_unselect : R.drawable.ic_eventlist_cell_confirm_select);
    }


}