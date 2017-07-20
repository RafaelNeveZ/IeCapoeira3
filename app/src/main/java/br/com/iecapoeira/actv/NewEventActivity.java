package br.com.iecapoeira.actv;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.hemobile.BaseActivity;
import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.UserDetails;

@EActivity(R.layout.actv_new_event)
@OptionsMenu(R.menu.new_event)
public class NewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @ViewById
    Toolbar toolbar;

    @ViewById
    EditText editName;

    @ViewById
    EditText editAddress;

    @ViewById
    EditText editCity;

    @ViewById
    EditText editState;

    @ViewById
    EditText editCountry;

    @ViewById
    EditText editDesc;

    @ViewById
    ImageButton btPhoto;

    @ViewById
    Button btDate;

    @ViewById
    Button btHour;

    private int selDay, selMonth, selYear;
    private int selHour, selMinute;

    private Bitmap bmp;

    @AfterViews
    public void init() {
        setHeader();
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_voltar);
        toolbar.setTitle(getString(R.string.title_eventos));
        setSupportActionBar(toolbar);

    }

    private void setupTime(int year, int month, int day, int hour, int minute) {
        Calendar c = Calendar.getInstance();
        selDay = day;
        selMonth = month;
        selYear = year;
        selMinute = minute;
        selHour = hour;
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        c.set(year, month, day, hour, minute);

        btDate.setText(sdf.format(c.getTime()));
        btHour.setText(String.format("%02d:%02d", hour, minute));
    }

    @OptionsItem
    public void newEvent() {
        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String country = editCountry.getText().toString().trim();
        String state = editState.getText().toString().trim();
        String description = editDesc.getText().toString().trim();

        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (address.isEmpty()) {
            setError(editAddress, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (city.isEmpty()) {
            setError(editCity, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (state.isEmpty()) {
            setError(editState, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (country.isEmpty()) {
            setError(editCountry, getString(R.string.msg_erro_campo_vazio));
            return;
        }

      //  showProgress(getString(R.string.aguarde));

        Event event = Event.create(Event.class);
        event.setName(name);
        event.setAddress(address);
        event.setCity(city);
        event.setState(state);
        event.setCountry(country);
        event.setDescription(description);
        Calendar date = Calendar.getInstance();
        date.set(selYear, selMonth, selDay, selHour, selMinute);
        event.setDate(date.getTime());
        event.setOwner((UserDetails) ParseUser.getCurrentUser().get(UserDetails.USER_DETAILS));

        try {
            event.setBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        event.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
          //          Toast.makeText(getActivity(), R.string.msg_salvo_sucesso, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
           //         Toast.makeText(getActivity(), R.string.msg_erro_criar_evento, Toast.LENGTH_LONG).show();
                }
           //     dismissProgress();
            }
        });
    }

    @Click
    public void btPhoto() {
      //  PhotoUtil.getCroppedImageFromGallery(getActivity());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
        if (uri != null) {
            /*bmp = PhotoUtil.resizeBitmap(getActivity(), uri);
            btPhoto.setImageBitmap(bmp);
            btPhoto.setBackgroundResource(android.R.color.transparent);*/
        }
    }

    @Click
    public void btHour() {
        TimePickerDialog tpd = new TimePickerDialog(this, this, selHour, selMinute, true);
        tpd.show();
    }

    @Click
    public void btDate() {
        DatePickerDialog dpd = new DatePickerDialog(this, this, selYear, selMonth, selDay);
        dpd.show();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setupTime(selYear, selMonth, selDay, hourOfDay, minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
         setupTime(year, monthOfYear, dayOfMonth, selHour, selMinute);
    }

    public void setError(EditText edit, String error) {
        edit.requestFocus();
        edit.setError(error);
        dismissError(edit);
    }

    @UiThread(delay = 3000)
    public void dismissError(EditText edit) {
        edit.setError(null);
    }
}