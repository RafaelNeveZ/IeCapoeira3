package br.com.iecapoeira.actv;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import java.util.List;

import br.com.hemobile.BaseActivity;
import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.NewEventAdapter;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.UserDetails;

@EActivity(R.layout.actv_new_event)
@OptionsMenu(R.menu.new_event)
public class NewEventActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

//    @ViewById
//    Toolbar toolbar;
//    List<Aula> aula;
    private int STORAGE_PERMISSION_CODE = 23;

    public boolean isInit = true;

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
    ImageView photo;

    @ViewById
    Button btDate;

    @ViewById
    Button btHour;

    @ViewById
    Button btFinalHour;

    @ViewById
    Button addOtherEvent;

    private ProgressDialog progressDialog;

    private int selDay, selMonth, selYear;
    private int selHour, selMinute;
    private final Context context = this;
    private Bitmap bmp;

    @AfterViews
    public void init() {
//        setHeader();
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);


//        ListView listaDeCursos = (ListView) findViewById(R.id.list);
//
//        //chamada da implementaçao do android:
//        //ArrayAdapter<Curso> adapter = new ArrayAdapter<Curso>(this,
//        //android.R.layout.simple_list_item_1, cursos);
//
//        //chamada da nossa implementação
//        NewEventAdapter adapter =
//                new NewEventAdapter(aula, this);
//
//        listaDeCursos.setAdapter(adapter);
    }

/*
    public void setHeader() {
//        toolbar.setNavigationIcon(R.drawable.logo_voltar);
//        toolbar.setTitle(getString(R.string.title_eventos));
//        setSupportActionBar(toolbar);

    }
*/



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

    private void setJustDate(int year, int month, int day){
        Calendar c = Calendar.getInstance();
        selDay = day;
        selMonth = month;
        selYear = year;
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        c.set(year, month, day, 00, 00);
        btDate.setText(sdf.format(c.getTime()));


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

       showProgress(getString(R.string.aguarde));

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
                    Toast.makeText(context, R.string.msg_salvo_sucesso, Toast.LENGTH_LONG).show();
                    setResult(RESULT_OK);
                    finish();
                } else {
                    Toast.makeText(context, R.string.msg_erro_criar_evento, Toast.LENGTH_LONG).show();
                }
               dismissProgress();
            }
        });
    }

    @Click
    public void btPhoto() {
        galleyView();
    }

    public void galleyView(){
        if(isReadStorageAllowed()) {
            PhotoUtil.getCroppedImageFromGallery(this);
        }else{
            requestStoragePermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
        if (uri != null) {
            bmp = PhotoUtil.resizeBitmap(this, uri);
            photo.setImageBitmap(bmp);
            photo.setBackgroundResource(android.R.color.transparent);
        }
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
            //Caso o usuario tenha negado anteriormente a permissão
        }
        //Pedidndo a permissão
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //Caso a permissão tenha sido aceita
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleyView();
            } else {
                //Caso a permissão tenha sido recusada
                Toast.makeText(this, "Permissão negada", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isReadStorageAllowed() {
        //Testando se a permissão já foi aceita
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        //Caso sim
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //Caso não
        return false;
    }

    @Click
    public void btHour() {
        isInit = true;
        TimePickerDialog tpd = new TimePickerDialog(this, this, selHour, selMinute, true);
        tpd.show();
    }

    @Click
    public void addOtherEvent() {

    }

    @Click
    public void btFinalHour() {
        isInit = false;
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
        setJustTime(hourOfDay, minute,isInit);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
         setJustDate(year, monthOfYear, dayOfMonth);
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