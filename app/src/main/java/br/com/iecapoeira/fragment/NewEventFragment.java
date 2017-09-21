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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.CityActivity_;
import br.com.iecapoeira.adapter.NewEventAdapter;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.EventDate;
import br.com.iecapoeira.model.NewEvent;
import br.com.iecapoeira.utils.OnButtonClicked;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

import static android.icu.text.DateTimePatternGenerator.DAY;

@EFragment(R.layout.actv_new_event)
@OptionsMenu(R.menu.new_event)
public class NewEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, RecyclerViewOnClickListenerHack {

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

   /* @ViewById
    RecyclerView rcNew;*/

    @ViewById
    CheckBox checkCountry;

    @ViewById
    EditText editTrueCity;

    @ViewById
    RadioButton rdCultural, rdCapoeira;
    private boolean dataInicialSetada = false;
    private boolean dataFinalSetada = false;
    private ProgressDialog progressDialog;

    private int selDay, selMonth, selYear,noChangeMonth, noChangeYear, noChangeDay;
    private int selHour, selMinute;
    private Bitmap bmp;
    private int horaInicial, minutoInicial, horafinal,minutofinal;
    private final static String TAG = "TAG";
  //  public List<ParseObject> listNE;
  //  private int eventDaysCount = 0;
  //  private NewEventAdapter adapter;
    public final  Context context= getContext();
    public int Day=0,Month=0,Year=0;
    public String newDate;
    private int rightPosition;
    private String my64foto=null;
    private String Pais="";
    private boolean isInitDate=true;
    Date initalDate, endDate, startTime, endTime, noChangeDate;
    private boolean dontPutEndDate;
    //  private JSONObject jasonFinal;

    @AfterViews
    public void init(){
//        setHeader();
    //    jasonFinal=new JSONObject();
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
    
        //   rcNew.setHasFixedSize(false);
     //   LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
     //   rcNew.setLayoutManager(llm);
     //   rcNew.setNestedScrollingEnabled(false);
        /*listNE = new ArrayList<ParseObject>();
        adapter = new NewEventAdapter(getActivity(), listNE, new OnButtonClicked() {
            @Override
            public void onBtnClick(int position, int choosed) {
                rightPosition = position;
                if (choosed == 0) {
                    putDate();
                }else if(choosed==1){
                    isInit= true;
                    putTime();
                }else{
                    isInit=false;
                    putTime();
                }
            }
        });
        adapter.setRecyclerViewOnClickListenerHack(this);
        rcNew.setAdapter(adapter);*/

       /* NewEventAdapter adapter = new NewEventAdapter(NewEventActivity.this, listNE);
        ListView PaysListView = (ListView)findViewById(R.id.listView);
        rcNew.setAdapter(adapter);*/


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

   /* public List<ParseObject> setListNE() throws JSONException {

        List<ParseObject> listAux = new ArrayList<>();
        ParseObject aux2 =  ParseObject.create("EventDate");

        JSONObject json = new JSONObject();
        JSONObject manJson = new JSONObject();
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            manJson.put("date",String.format("%02d/%02d/%02d",selDay,selMonth,selYear));
            manJson.put("startTime", String.format("%02d:%02d",selHour,selMinute));
            manJson.put("endTime",String.format("%02d:%02d",selHour+1,selMinute) );
            jasonFinal.put(""+eventDaysCount,manJson);
        }catch (Exception e){

        }


        aux2.put(EventDate.HOURINIT,selHour);
        aux2.put(EventDate.HOUREND,selHour+1);
        aux2.put(EventDate.MIMINIT,selMinute);
        aux2.put(EventDate.MIMEND,selMinute);
        aux2.put(EventDate.DAY,selDay);
        aux2.put(EventDate.MONTH,selMonth);
        aux2.put(EventDate.YEAR,selYear);
        // EventDate aux2 = new EventDate(selDay,selMonth,selYear,selHour,selMinute,selHour+1,selMinute);
  ;
        eventDaysCount++;

        return  (listAux);

    }*/

    @Click
    public void addOtherEvent() {
   /* *//*    listNE = setListNE() ;*//*
    if(listNE.size()<4) {
        List<ParseObject> listAux = new ArrayList<>();
        ParseObject aux2 = ParseObject.create("EventDate");


        *//*JSONObject json = new JSONObject();
        JSONObject manJson = new JSONObject();
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            manJson.put("date", String.format("%02d/%02d/%02d", selDay, selMonth, selYear));
            manJson.put("startTime", String.format("%02d:%02d", selHour, selMinute));
            manJson.put("endTime", String.format("%02d:%02d", selHour + 1, selMinute));
            jasonFinal.put(rightPosition + "", manJson);
        } catch (Exception e) {

        }*//*

        aux2.put(EventDate.HOURINIT, selHour);
        aux2.put(EventDate.HOUREND, selHour + 1);
        aux2.put(EventDate.MIMINIT, selMinute);
        aux2.put(EventDate.MIMEND, selMinute);
        aux2.put(EventDate.DAY, selDay);
        aux2.put(EventDate.MONTH, noChangeMonth);
        aux2.put(EventDate.YEAR, selYear);
        listNE.add(aux2);
       // adapter.notifyItemInserted(listNE.size() - 1);
        adapter.notifyDataSetChanged();
        eventDaysCount++;
    }else{
        Toast.makeText(getActivity(), "Limite máximo de dias", Toast.LENGTH_LONG).show();
        }*/
    }

    @Override
    public  void onDestroy(){
        super.onDestroy();

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
/*
    public void setHeader() {
//        toolbar.setNavigationIcon(R.drawable.logo_voltar);
//        toolbar.setTitle(getString(R.string.title_eventos));
//        setSupportActionBar(toolbar);

    }
    */
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
        startTime.setHours(horafinal);
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
          //  }else{
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
       //     }else{
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
        Log.d("MYDATE",myDate.getYear()+"");
        Log.d("MYDATE",myDate.getMonth()+"");
        Log.d("MYDATE",myDate.getDate()+"");
        Log.d("NOCHANGEDATE",noChangeDate.getYear()+"");
        Log.d("NOCHANGEDATE",noChangeDate.getMonth()+"");
        Log.d("NOCHANGEDATE",noChangeDate.getDate()+"");

        long dateRecivedinMills = cal.getTimeInMillis();
        if(sameday)
        Log.d("SAMEDAY","true");
        else
            Log.d("SAMEDAY","false");
        if(isInitDate){
            if(System.currentTimeMillis() < dateRecivedinMills || sameday) {
                if(!dataFinalSetada){
                    initalDate.setYear(myDate.getYear()-1900);
                    initalDate.setMonth(myDate.getMonth());
                    initalDate.setDate(myDate.getDate());
                    btDate.setText(((myDate.getDate()<10)?"0"+myDate.getDate(): myDate.getDate()) + "/" + (((myDate.getMonth()+1)<10)?"0"+(myDate.getMonth()+1): (myDate.getMonth()+1))  + "/" + myDate.getYear());
                    btFinalDate.setEnabled(true);
                    dataInicialSetada=true;
                }else{
                    if(endDate.getTime() > myDate.getTime()) {
                        initalDate.setYear(myDate.getYear()-1900);
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
            endDate.setYear(myDate.getYear()-1900);
            endDate.setMonth(myDate.getMonth());
            endDate.setDate(myDate.getDate());
            if(endDate.getTime() > initalDate.getTime()) {
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
                /*ParseRelation<ParseUser> relation = newEvent.getRelation(Event.OWNER);
                relation.add(ParseUser.getCurrentUser());*/
                newEvent.put(Event.OWNER, ParseUser.getCurrentUser().getUsername());
                newEvent.put(Event.TYPE, isCapeira());
                if (my64foto != null)
                    newEvent.put(Event.FOTO, my64foto);
                /*ParseRelation<ParseObject>relation=newEvent.getRelation("eventTime");
                relation.add(listNE.get(0));*/
                /*jasonFinal=new JSONObject();
                int i=0;
                for (ParseObject item:listNE
                     ) {

                    JSONObject manJson = new JSONObject();
                    String pattern = getString(R.string.date_pattern);
                    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                    try {
                        manJson.put("date",String.format("%02d/%02d/%02d",item.get(EventDate.DAY), item.get(EventDate.MONTH),item.get(EventDate.YEAR)));
                        manJson.put("startTime", String.format("%02d:%02d",item.get(EventDate.HOURINIT),item.get(EventDate.MIMINIT)));
                        manJson.put("endTime",String.format("%02d:%02d",item.get(EventDate.HOUREND),item.get(EventDate.MIMEND)) );
                        jasonFinal.put(i+"",manJson);
                        i++;
                    }catch (Exception e){

                    }
                    newEvent.put("eventDate", jasonFinal);
                }*/

                newEvent.put("startDate",initalDate);
                if(dataFinalSetada) {
                    newEvent.put("endDate", endDate);
                    Log.d("FINAL IGUAL", "SIM");
                }else {
                    Log.d("FINAL IGUAL", "NAO");
                    Date nonDate = new Date(0,0,0);
                    newEvent.put("endDate", nonDate);
                }

                Calendar date = Calendar.getInstance();
                date.set(selYear, selMonth, selDay, selHour, selMinute);
                newEvent.put(Event.DATE,date.getTime());
                newEvent.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            dismissProgress();
                            getActivity().finish();
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
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
        startActivityForResult(new Intent(getActivity(), CityActivity_.class), 5);
    }
   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 5) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(getActivity(), "Você não escolheu um filtro", Toast.LENGTH_SHORT).show();
            }
        }

    }*/


  /*     //showProgress(getString(R.string.aguarde));

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
        }*/



    public boolean validateFields() {

        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = (String) getText(R.string.choose_city);
        String country = editCountry.getText().toString().trim();
        String state = editState.getText().toString().trim();
        String description = editDesc.getText().toString().trim();

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
        /*if (city.isEmpty()) {
            setError(editCity, getString(R.string.msg_erro_campo_vazio));
            return false;
        }*/
        /*if (state.isEmpty()) {
            setError(editState, getString(R.string.msg_erro_campo_vazio));
            return;
        }*/


        return true;

    }

    @Click
    public void btPhoto() {
        galleyView();
    }

    public void galleyView(){
        if(isReadStorageAllowed()) {
            PhotoUtil.getCroppedImageFromGalleryFrag(this);

        }else{
            requestStoragePermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 19) {
            Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
            Log.d("URI", uri + "");
            if (uri != null) {
                bmp = PhotoUtil.resizeBitmap(getActivity(), uri);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();
                my64foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
                Log.d("STRING: ", my64foto);
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

   /* @Click
    public void btHour() {
        isInit = true;
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, selHour, selMinute, true);
        tpd.show();
    }*/









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
}