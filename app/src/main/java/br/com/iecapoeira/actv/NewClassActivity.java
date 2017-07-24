package br.com.iecapoeira.actv;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.UserDetails;

@EActivity(R.layout.activity_new_class)
@OptionsMenu(R.menu.new_event)
public class NewClassActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    @ViewById
    ImageButton btPhoto;

    @ViewById
    ImageView photo;

    @ViewById
    EditText editName,editEstilo, editGraduation, editDesc,editAddress,editCity, editState, editCountry;

    @ViewById
    Button btHour;

    @ViewById
    Button btFinalHour;

    @ViewById
    Button addOtherClass;

    private ProgressDialog progressDialog;

    private int selHour, selMinute;
    private final Context context = this;
    private Bitmap bmp;
    public boolean isInit = true;
    public boolean dontLeave = false;

    @AfterViews
    public void init() {
        Calendar c = Calendar.getInstance();
        setupTime( (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
    }

    private void setupTime(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        selMinute = minute;
        selHour = hour;
        c.set(2000, 1, 1, hour, minute);
        btHour.setText(String.format("%02d:%02d", hour, minute));
        btFinalHour.setText(String.format("%02d:%02d", hour, minute));

    }

    private void setJustTime(int hour, int minute, boolean isInicial){
        Calendar c = Calendar.getInstance();
        selMinute = minute;
        selHour = hour;
        c.set(2000, 1, 1, hour, minute);
        if (isInicial){
            btHour.setText(String.format("%02d:%02d", hour, minute));
        }else{
            btFinalHour.setText(String.format("%02d:%02d", hour, minute));
        }
    }

    @Click
    public void btPhoto() {
        PhotoUtil.getCroppedImageFromGallery((NewClassActivity)context);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
        if (uri != null) {
            bmp = PhotoUtil.resizeBitmap((NewClassActivity)context, uri);
            photo.setImageBitmap(bmp);

            // photo.setBackgroundResource(android.R.color.transparent);
        }
    }

    @Click
    public void btHour() {
        isInit = true;
        TimePickerDialog tpd = new TimePickerDialog(this, this, selHour, selMinute, true);
        tpd.show();
    }

    @Click
    public void addOtherClass() {
        newEvent();
        if(!dontLeave) {
            Intent intent = new Intent(this, NewClassActivity_.class);
            startActivity(intent);
            finish();
        }
        dontLeave = false;

    }

    @Click
    public void btFinalHour() {
        isInit = false;
        TimePickerDialog tpd = new TimePickerDialog(this, this, selHour, selMinute, true);
        tpd.show();
    }

    @OptionsItem
    public void newEvent() {

        String name = editName.getText().toString().trim();
        String style = editEstilo.getText().toString().trim();
        String graduation = editGraduation.getText().toString().trim();
        String description = editDesc.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String country = editCountry.getText().toString().trim();
        String state = editState.getText().toString().trim();


        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }
        if (style.isEmpty()) {
            setError(editEstilo, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }
        if (graduation.isEmpty()) {
            setError(editGraduation, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }
        if (address.isEmpty()) {
            setError(editAddress, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }
        if (city.isEmpty()) {
            setError(editCity, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }
        if (state.isEmpty()) {
            setError(editState, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }
        if (country.isEmpty()) {
            setError(editCountry, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }

          showProgress(getString(R.string.aguarde));

       /* Aula aula = Aula.create(Aula.class);
        aula.setName(name);
        aula.setStyle(style);
        aula.setGraduation(graduation);
        aula.setAddress(address);
        aula.setCity(city);
        aula.setState(state);
        aula.setCountry(country);
        aula.setDescription(description);
        Calendar date = Calendar.getInstance();
        date.set(2000, 1, 1, selHour, selMinute);
        aula.setDate(date.getTime());
        aula.setOwner((UserDetails) ParseUser.getCurrentUser().get(UserDetails.USER_DETAILS));
        try {
            aula.setBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }

        aula.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, R.string.msg_salvo_sucesso, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(context, R.string.msg_erro_criar_evento, Toast.LENGTH_LONG).show();
                }
                    dismissProgress();
            }
        });*/

    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setJustTime(hourOfDay, minute,isInit);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

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

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(this, getString(R.string.aguarde), text, true, false);
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