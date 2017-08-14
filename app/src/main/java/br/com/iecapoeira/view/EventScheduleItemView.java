package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.EventLog;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

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
    TextView textLocation;

    @ViewById
    ImageView btGoing;

    private Event obj;

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
        textDate.setText(sdf.format(obj.getDate()));
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
