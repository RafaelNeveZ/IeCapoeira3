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
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.CityChoiceActivity_;
import br.com.iecapoeira.actv.CityFiterActivity_;
import br.com.iecapoeira.actv.NewEventActivity_;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EFragment(R.layout.actv_new_event)
@OptionsMenu(R.menu.new_event)
public class NewEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, RecyclerViewOnClickListenerHack {


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
    private String uriImg;
    private Uri imgUri = null;

    @AfterViews
    public void init(){
        if(!isAdmin()){
            tipoEvento.setVisibility(View.GONE);
            getActivity().setTitle("Evento de Capoeira");
        }


//
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);

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
        initalDate = new Date(year,month,day);
        endDate = new Date(year,month,day+1);
        startTime = new Date();
        endTime = new Date();
        noChangeDate= new Date();
        Calendar c = Calendar.getInstance();
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
        btHour.setText(((horaInicial<10)?"0"+horaInicial:horaInicial)+":"+((minutoInicial<10)?"0"+minutoInicial:minutoInicial));
        btFinalHour.setText(((horafinal<10)?"0"+horafinal:horafinal)+":"+((minutofinal<10)?"0"+minutofinal:minutofinal));
        startTime.setHours(hour);
        startTime.setMinutes(minute);
        endTime.setHours(horafinal);
        endTime.setMinutes(minute);

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

                    initalDate.setYear(myDate.getYear());
                    initalDate.setMonth(myDate.getMonth());
                    initalDate.setDate(myDate.getDate());
                    btDate.setText(((myDate.getDate()<10)?"0"+myDate.getDate(): myDate.getDate()) + "/" + (((myDate.getMonth()+1)<10)?"0"+(myDate.getMonth()+1): (myDate.getMonth()+1))  + "/" + myDate.getYear());
                    btFinalDate.setEnabled(true);
                    dataInicialSetada=true;
                }else{
                    if(endDate.getTime() > myDate.getTime()) {
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
                final ParseObject newEvent = ParseObject.create("Event");
                final ParseObject eventGo = ParseObject.create("EventGo");
                newEvent.put(Event.NAME, editName.getText().toString());
                newEvent.put(Event.DESCRIPTION, editDesc.getText().toString());
                newEvent.put(Event.ADDRESS, editAddress.getText().toString());

                newEvent.put(Event.STATE, editState.getText().toString());

                if (editCityVisb == View.VISIBLE) {
                    newEvent.put(Event.COUNTRY, "Brasil");
                    newEvent.put(Event.CITY, editCity.getText().toString());
                } else {
                    newEvent.put(Event.COUNTRY, editCountry.getText().toString());
                    newEvent.put(Event.CITY, editTrueCity.getText().toString());
                }

                ParseRelation<ParseObject> owner = newEvent.getRelation("eventOwner");
                owner.add(ParseUser.getCurrentUser());
                ParseRelation<ParseObject> go = newEvent.getRelation("eventGo");
                go.add(ParseUser.getCurrentUser());

                if(isAdmin()) {
                    newEvent.put(Event.TYPE, isCapeira());
                }else{
                    newEvent.put(Event.TYPE, 0);
                }

               /* if (my64foto != null)
                newEvent.put(Event.FOTO, my64foto);*/

                newEvent.put("startTime",startTime);
                newEvent.put("endTime",endTime);
                initalDate.setYear(initalDate.getYear()-1900);
                newEvent.put("startDate",initalDate);

                if(bmp!=null){
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    byte[] image = stream.toByteArray();
                    ParseFile file = new ParseFile(editName.getText().toString()+"_foto", image);
                    newEvent.put("Photo",file);
                }


                if(dataFinalSetada) {
                    newEvent.put("hasMoreDays",true);
                    endDate.setYear(endDate.getYear()-1900);
                    newEvent.put("endDate", endDate);
                }else {
                    newEvent.put("hasMoreDays",false);
                }


                newEvent.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            dismissProgress();
                            if(!notOther){
                                Intent returnIntent = new Intent();
                                Toast.makeText(getActivity(), "Evento criado com sucesso", Toast.LENGTH_LONG).show();
                                getActivity().setResult(Activity.RESULT_OK,returnIntent);
                                getActivity().finish();
                            }else{
                                Toast.makeText(getActivity(), "Evento criado com sucesso", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), NewEventActivity_.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        } else {
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_LONG).show();
                            dismissProgress();
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
        startActivityForResult(new Intent(getActivity(), CityChoiceActivity_.class), 25);
    }

    public boolean validateFields() {

        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = (String) getText(R.string.choose_city);

        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            notOther=false;
            return false;
        }

        int editCityVisb = cityChoice.getVisibility();
        if (editCityVisb == View.VISIBLE) {
            if (editCity.getText().equals(city)) {
                notOther=false;
                Toast.makeText(getActivity(), "Escolha uma cidade", Toast.LENGTH_SHORT).show();
                return false;
            }
        } else {

            if (editTrueCity.getText().toString().isEmpty()) {
                notOther=false;
                setError(editTrueCity, getString(R.string.msg_erro_campo_vazio));
                return false;
            }
            if (editCountry.getText().toString().isEmpty()){
                notOther=false;
                setError(editCountry, getString(R.string.msg_erro_campo_vazio));
                return false;
            }

        }
        if (address.isEmpty()) {
            notOther=false;
            setError(editAddress, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (!dataInicialSetada){
            notOther=false;
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
              // PhotoUtil.launchGooglePhotosPicker(this);



            } else {
                requestStoragePermission();
            }
        } else {
            PhotoUtil.getCroppedImageFromGalleryFrag(this);
         //  PhotoUtil.launchGooglePhotosPicker(this);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG",requestCode+"");

        if (requestCode == 19) {
            Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
            Log.d("TAG",uri+"");
            if (uri != null) {
                Bitmap thumb =  PhotoUtil.resizeBitmapThumb(getActivity(), uri);
                imgUri = uri;
                photo.setImageBitmap(thumb);
                bmp = PhotoUtil.resizeBitmap(getActivity(), uri);
                photo.setBackgroundResource(android.R.color.transparent);

            }
        }

        if (requestCode == 25) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                editCity.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Você não escolheu a cidade", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
    public void onClickListener(View v, int position) {

    }

    @Click
    public void photo() {

        if(imgUri!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(imgUri,"image/*");
            startActivity(intent);

        }

    }
}