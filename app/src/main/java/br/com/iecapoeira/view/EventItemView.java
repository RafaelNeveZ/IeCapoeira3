package br.com.iecapoeira.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HETextUtil;

@EViewGroup(R.layout.item_event)
public class EventItemView extends ItemView<Event> {

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

    public EventItemView(Context context) {
        super(context);
    }

   /* private final GetDataCallback callback = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            setProfilePicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    };*/

    @UiThread
    public void setProfilePicture(Bitmap picture) {
        img.setImageBitmap(picture);
    }

    @Override
    public void bind(Event obj, int position) {
        this.obj = obj;
      //  IEApplication.getUserDetails();
        textName.setText(obj.getName());
        String pattern = getContext().getString(R.string.date_pattern);
        final SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        textDate.setText(sdf.format(obj.getDate()));
        textLocation.setText(String.format("%s, %s - %s", HETextUtil.toTitleCase(obj.getCity()), obj.getState(), obj.getCountry()));
       /* try {
            boolean userGoing = obj.isUserGoing(IEApplication.getUserDetails());
            setImageBtGoing(userGoing);
            btGoing.setTag(userGoing);
        } catch (ParseException e) {
            setImageBtGoing(false);
            btGoing.setTag(false);
        }*/

        /*Bitmap picture = obj.getProfilePicture(callback);
        if (picture != null) {
            setProfilePicture(picture);
        }
        else {*/
            img.setImageResource(R.drawable.ic_eventlist_cell_photo);
        /*}*/

    }

    @Click
    public void btGoing() {
        Boolean userGoing = ! (Boolean) btGoing.getTag();
        setImageBtGoing(userGoing);
        btGoing.setTag(userGoing);
        changeGoingOnParse(userGoing);
    }

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