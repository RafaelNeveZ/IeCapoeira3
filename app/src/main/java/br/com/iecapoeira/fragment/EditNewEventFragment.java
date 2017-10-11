package br.com.iecapoeira.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.CityActivity_;
import br.com.iecapoeira.actv.NewEventActivity_;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EFragment(R.layout.actv_new_event)
@OptionsMenu(R.menu.new_event)
public class EditNewEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, RecyclerViewOnClickListenerHack {

    private int STORAGE_PERMISSION_CODE = 23;

    public boolean isInit = true;

    @ViewById
    EditText editName;

    @ViewById
    EditText editAddress;

    @ViewById
    TextView editCity;

    @ViewById
    LinearLayout cityChoice;

    @ViewById
    EditText editState;

    @ViewById
    EditText editCountry;

    @ViewById
    EditText editDesc;

    @ViewById
    Button btDate, btFinalDate;

    @ViewById
    ImageButton btPhoto;

    @ViewById
    ImageView photo;

    @ViewById
    Button btHour;

    @ViewById
    Button btFinalHour;

    @ViewById
    Button addOtherEvent;


    @ViewById
    CheckBox checkCountry;

    @ViewById
    EditText editTrueCity;

    @ViewById
    RadioButton rdCultural, rdCapoeira;

    @ViewById
    LinearLayout tipoEvento;

    private boolean dataInicialSetada = false;
    private boolean dataFinalSetada = false;
    private ProgressDialog progressDialog;

    private int selDay, selMonth, selYear;
    private int selHour, selMinute;
    private Bitmap bmp;
    private int horaInicial, minutoInicial, horafinal,minutofinal;
    private final static String TAG = "TAG";
    public final  Context context= getContext();
    private String my64foto=null;
    private boolean isInitDate=true;
    Date initalDate, endDate, startTime, endTime, noChangeDate;
    private boolean dontPutEndDate;
    private boolean notOther=false;
    public static Event event;
    private boolean mudouInicial=false;

    @AfterViews
    public void init(){
        addOtherEvent.setVisibility(View.GONE);
        getActivity().setTitle("Editar Evento");
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);

        if(!isAdmin()){
            tipoEvento.setVisibility(View.GONE);
        }else{
            String cap="Capoeira";
            if(cap.equals(event.get(Event.TYPE)))
                rdCapoeira.setChecked(true);
            else
                rdCultural.setChecked(true);
        }

        editName.setText(event.getString(Event.NAME));
        editDesc.setText(event.getString(Event.DESCRIPTION));
        editAddress.setText(event.getString(Event.ADDRESS));
        editState.setText(event.getString(Event.STATE));

        if (event.getString(Event.COUNTRY).equals("Brasil")) {
                editCity.setText(event.getString(Event.CITY));
        } else {
           checkCountry.setChecked(false);
            editTrueCity.setVisibility(View.VISIBLE);
            editCountry.setVisibility(View.VISIBLE);
            cityChoice.setVisibility(View.GONE);
            editCountry.setText(event.getString(Event.COUNTRY));
            editTrueCity.setText(event.getString(Event.CITY));
        }

        if(event.get("Photo")!=null) {
            ParseFile image = (ParseFile) event.get("Photo");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        photo.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }

        initalDate =new Date();

        startTime = new Date();
        endTime = new Date();
        initalDate = (Date) event.get("startDate");

        endDate = new Date(initalDate.getYear(),initalDate.getMonth(),initalDate.getDate()+1);
        endTime = (Date) event.get("startTime");
        startTime = (Date) event.get("endTime");
        int hourInit=endTime.getHours();
        int hourFim=startTime.getHours();
        int minuteInit=endTime.getMinutes();
        int minuteFim=startTime.getMinutes();
        String end = "";
        String start =((initalDate.getDate()<10)?"0"+ initalDate.getDate(): initalDate.getDate()) + "/" + (((initalDate.getMonth()+1)<10)?"0"+(initalDate.getMonth()+1): (initalDate.getMonth()+1))  + "/" + (initalDate.getYear()+1900);
        btDate.setText(start);
        btFinalDate.setEnabled(true);
        dataInicialSetada=true;

        btHour.setText(((hourInit<10)?"0"+hourInit: hourInit)+":"+((minuteInit<10)?"0"+minuteInit:minuteInit));
        btFinalHour.setText(((hourFim<10)?"0"+hourFim: hourFim)+":"+((minuteFim<10)?"0"+minuteFim:minuteFim));
    }

    public boolean isAdmin(){
        if((boolean)ParseUser.getCurrentUser().get("Admin"))
            return true;
        else
            return false;

    }


    @Click
    public void btDate() {
        isInitDate=true;
        putDate();
    }

    @Click
    public void btFinalDate() {
        isInitDate=false;
        putDate();

    }

    @Click
    public void btHour() {
        isInit=true;
        putTime();

    }
    @Click
    public void btFinalHour() {
        isInit=false;
        putTime();

    }

    public void putTime() {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, selHour, selMinute, true);
        tpd.show();
    }


    public void putDate() {
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, selYear, selMonth, selDay);
        dpd.show();
    }



    @Click
    public void addOtherEvent() {
        notOther = true;
        newEvent();
    }

    @Click
    public void checkCountry(){
        if(checkCountry.isChecked()){
            editCountry.setVisibility(View.GONE);
            editTrueCity.setVisibility(View.GONE);
            cityChoice.setVisibility(View.VISIBLE);

        }else{
            editCountry.setVisibility(View.VISIBLE);
            editTrueCity.setVisibility(View.VISIBLE);
            cityChoice.setVisibility(View.GONE);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        setJustTime(hourOfDay, minute,isInit);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Date date = new Date(year, monthOfYear, dayOfMonth);
        setJustDate(date);

    }


    private void setupTime(int year, int month, int day, int hour, int minute) {
        noChangeDate= new Date();
        selDay = day;
        noChangeDate.setYear(year);
        noChangeDate.setMonth(month);
        noChangeDate.setDate(day);
        selMonth = month;
        selYear = year;
        selMinute = minute;
        selHour = hour;
        horaInicial = hour;
        minutoInicial = minute;
        horafinal = hour+1;
        minutofinal= minute;

    }

    private void setJustTime(int hour, int minute, boolean isInicial){
        Calendar c = Calendar.getInstance();
        selMinute = minute;
        selHour = hour;
        c.set(0,0,0,hour, minute);
        if (isInit){
            int horaInitInSec = hour*3600 + minute*60;
            int horaFimInSec = endTime.getHours()*3600 + endTime.getMinutes()*60;
            if( horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }
            startTime.setHours(hour);
            startTime.setMinutes(minute);
            btHour.setText(((hour<10)?"0"+hour: hour)+":"+((minute<10)?"0"+minute:minute));
            horaInicial = hour;
            minutoInicial = minute;

        }else{
            int horaInitInSec = startTime.getHours()*3600 + startTime.getMinutes()*60;
            int horaFimInSec = hour*3600 + minute*60;
            if( horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }
            endTime.setHours(hour);
            endTime.setMinutes(minute);
            btFinalHour.setText(((hour<10)?"0"+hour: hour)+":"+((minute<10)?"0"+minute:minute));
            horafinal = hour;
            minutofinal = minute;
        }

    }

    private void setJustDate(Date myDate){
        Calendar cal = Calendar.getInstance();

        cal.set(myDate.getYear(), myDate.getMonth(), myDate.getDate());
        boolean sameday =(myDate.getYear() == noChangeDate.getYear() && myDate.getMonth()==noChangeDate.getMonth() && myDate.getDate()==noChangeDate.getDate());

        long dateRecivedinMills = cal.getTimeInMillis();
        if(isInitDate){
            if(System.currentTimeMillis() < dateRecivedinMills || sameday) {
                if(!dataFinalSetada){
                    mudouInicial=true;
                    Log.d("!datafinalsetada","false");
                    initalDate.setYear(myDate.getYear());
                    initalDate.setMonth(myDate.getMonth());
                    initalDate.setDate(myDate.getDate());
                    btDate.setText(((myDate.getDate()<10)?"0"+myDate.getDate(): myDate.getDate()) + "/" + (((myDate.getMonth()+1)<10)?"0"+(myDate.getMonth()+1): (myDate.getMonth()+1))  + "/" + myDate.getYear());
                    btFinalDate.setEnabled(true);
                    dataInicialSetada=true;
                }else{
                    Log.d("!datafinalsetada","true");
                    if(endDate.getTime() > myDate.getTime()) {
                        mudouInicial=true;
                        initalDate.setYear(myDate.getYear());
                        initalDate.setMonth(myDate.getMonth());
                        initalDate.setDate(myDate.getDate());
                        btDate.setText(((myDate.getDate()<10)?"0"+myDate.getDate(): myDate.getDate()) + "/" + (((myDate.getMonth()+1)<10)?"0"+(myDate.getMonth()+1): (myDate.getMonth()+1))  + "/" + myDate.getYear());
                        btFinalDate.setEnabled(true);
                    }else{
                        showAlert(getString(R.string.erro_date_title), getString(R.string.erro_final_date_title));
                    }
                }
            }else{
                showAlert(getString(R.string.erro_date_title), getString(R.string.erro_valid_date));
            }
        }else{
            if(myDate.getTime() > initalDate.getTime()) {
                endDate.setYear(myDate.getYear());
                endDate.setMonth(myDate.getMonth());
                endDate.setDate(myDate.getDate());
                dataFinalSetada=true;
                btFinalDate.setText(((myDate.getDate()<10)?"0"+myDate.getDate(): myDate.getDate()) + "/" + (((myDate.getMonth()+1)<10)?"0"+(myDate.getMonth()+1): (myDate.getMonth()+1))  + "/" + myDate.getYear());
                dontPutEndDate = false;
            }else{
                showAlert(getString(R.string.erro_date_title), getString(R.string.erro_final_date_title));
            }
        }
    }

    public void showAlert(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.dialog_confirm),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    @OptionsItem
    public void newEvent() {

        if(validateFields()) {
            showProgress("Criando evento...");
            int editCityVisb = cityChoice.getVisibility();

            event.put(Event.NAME, editName.getText().toString());
            event.put(Event.DESCRIPTION, editDesc.getText().toString());
            event.put(Event.ADDRESS, editAddress.getText().toString());

            event.put(Event.STATE, editState.getText().toString());

            if (editCityVisb == View.VISIBLE) {
                event.put(Event.COUNTRY, "Brasil");
                event.put(Event.CITY, editCity.getText().toString());
            } else {
                event.put(Event.COUNTRY, editCountry.getText().toString());
                event.put(Event.CITY, editTrueCity.getText().toString());
            }

            if(isAdmin()) {
                event.put(Event.TYPE, isCapeira());
            }else{
                event.put(Event.TYPE, 0);
            }

            if(bmp!=null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile(editName.getText().toString()+"_foto", image);
                event.put("Photo",file);
            }

            if(mudouInicial)
            initalDate.setYear(initalDate.getYear()-1900);
            else
            initalDate.setYear(initalDate.getYear());

            event.put("startDate",initalDate);
            if(dataFinalSetada) {
                event.put("hasMoreDays",true);
                endDate.setYear(endDate.getYear()-1900);
                event.put("endDate", endDate);
                Log.d("FINAL IGUAL", "SIM");
            }else {
                event.put("hasMoreDays",false);
                Log.d("FINAL IGUAL", "NAO");
            }


            event.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        dismissProgress();
                        if(!notOther){
                            Intent returnIntent = new Intent();
                            getActivity().setResult(Activity.RESULT_OK,returnIntent);
                            getActivity().finish();
                        }else{
                            Intent intent = new Intent(getActivity(), NewEventActivity_.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    } else {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                        dismissProgress();
                        Intent returnIntent = new Intent();
                        getActivity().setResult(Activity.RESULT_CANCELED,returnIntent);
                        getActivity().finish();

                    }
                }
            });

        }

    }

    private Integer isCapeira() {
        if(rdCapoeira.isChecked())
            return 0;
        else
            return 1;
    }

    @Click
    public void cityChoice(){
        startActivityForResult(new Intent(getActivity(), CityActivity_.class), 5);
    }

    public boolean validateFields() {

        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = (String) getText(R.string.choose_city);

        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (address.isEmpty()) {
            setError(editAddress, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        int editCityVisb = cityChoice.getVisibility();
        if (editCityVisb == View.VISIBLE) {
            if (editCity.getText().equals(city)) {
                Toast.makeText(getActivity(), "Escolha uma cidade", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {

            if (editTrueCity.getText().toString().isEmpty()) {
                setError(editTrueCity, getString(R.string.msg_erro_campo_vazio));
                return false;
            }
            if (editCountry.getText().toString().isEmpty()) {
                setError(editCountry, getString(R.string.msg_erro_campo_vazio));
                return false;
            }

        }
        if (!dataInicialSetada){
            Log.d("INICIAL VAZIO", "SIM");
            Toast.makeText(getActivity(), "Escolha a data inicial do evento", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;

    }

    @Click
    public void btPhoto() {
        galleyView();
    }

    public void galleyView(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (isReadStorageAllowed()) {
                PhotoUtil.getCroppedImageFromGalleryFrag(this);

            } else {
                requestStoragePermission();
            }
        } else {
            PhotoUtil.getCroppedImageFromGalleryFrag(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 19) {
            Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
            Log.d("URI", uri + "");
            if (uri != null) {
                bmp = PhotoUtil.resizeBitmap(getActivity(), uri);
                photo.setImageBitmap(bmp);
                photo.setBackgroundResource(android.R.color.transparent);

            }
        }
        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                editCity.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Você não escolheu um filtro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)){
            //Caso o usuario tenha negado anteriormente a permissão
        }
        //Pedidndo a permissão
        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == STORAGE_PERMISSION_CODE) {
            //Caso a permissão tenha sido aceita
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleyView();
            } else {
                //Caso a permissão tenha sido recusada
                Toast.makeText(getActivity(), "Permissão negada", Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean isReadStorageAllowed() {
        //Testando se a permissão já foi aceita
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //Caso sim
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //Caso não
        return false;
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

    @Override
    public void onClickListener(View view, int position) {

    }
}