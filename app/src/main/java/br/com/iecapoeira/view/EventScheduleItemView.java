package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.EventLog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HETextUtil;

/**
 * Created by Rafael on 09/08/16.
 */

@EViewGroup(R.layout.item_event_schedule)
public class EventScheduleItemView extends RelativeLayout {

    @ViewById
    TextView textName;

    @ViewById
    TextView textDate;

    @ViewById
    TextView textLocation,textTime;

    @ViewById
    ImageView btGoing;

    private Event obj;
    public List<JSONObject> jList;
    @ViewById
    ImageView img;
    private ProgressDialog progressDialog;


    public EventScheduleItemView(Context context) {
        super(context);
    }

    public EventScheduleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(Event obj) {
        this.obj = obj;
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
}
