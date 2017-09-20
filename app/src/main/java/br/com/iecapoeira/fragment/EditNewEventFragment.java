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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
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
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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
import java.util.List;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.CityActivity_;
import br.com.iecapoeira.adapter.NewEventAdapter;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.EventDate;
import br.com.iecapoeira.utils.OnButtonClicked;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;

@EFragment(R.layout.actv_new_event)
@OptionsMenu(R.menu.new_event)
public class EditNewEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener, RecyclerViewOnClickListenerHack {

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

 /*   @ViewById
    RecyclerView rcNew;*/

    @ViewById
    CheckBox checkCountry;

    @ViewById
    EditText editTrueCity;

    @ViewById
    RadioButton rdCultural, rdCapoeira;

    private ProgressDialog progressDialog;

    private int selDay, selMonth, selYear,noChangeMonth, noChangeYear, noChangeDay;
    private int selHour, selMinute;
    private Bitmap bmp;
    private int horaInicial, minutoInicial, horafinal,minutofinal;
    private final static String TAG = "TAG";
   // public List<ParseObject> listNE;
    private int eventDaysCount = 0;
   // private NewEventAdapter adapter;
    public final  Context context= getContext();
    public int Day=0,Month=0,Year=0;
    public String newDate;
    private int rightPosition;
    private String my64foto=null;
    private String Pais="";
   // private JSONObject jasonFinal;
    public static Event event;

    @AfterViews
    public void init(){
//        setHeader();
        int editCityVisb = cityChoice.getVisibility();
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
                /*ParseRelation<ParseUser> relation = newEvent.getRelation(Event.OWNER);
                relation.add(ParseUser.getCurrentUser());*/
        String cap="Capoeira";

        if(cap.equals(event.get(Event.TYPE)))
            rdCapoeira.setChecked(true);
        else
            rdCultural.setChecked(true);


        if(event.get(Event.FOTO)!=null) {
            byte[] decodedString = Base64.decode(event.get(Event.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photo.setImageBitmap(decodedByte);
        }

       /* jasonFinal=new JSONObject();
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
        rcNew.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcNew.setLayoutManager(llm);
        rcNew.setNestedScrollingEnabled(false);
        listNE = new ArrayList<ParseObject>();
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

    /*public List<ParseObject> setListNE() throws JSONException {

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

   /* @Click
    public void addOtherEvent() {
    *//*    listNE = setListNE() ;*//*
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
        }
    }*/

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
        setJustDate(year,monthOfYear,dayOfMonth);

    }


    private void setupTime(int year, int month, int day, int hour, int minute) {

        Log.d("DAY",day+"");
        Log.d("MONTH",month+"");
        Log.d("YEAR",year+"");
        Calendar c = Calendar.getInstance();
        selDay = day;
        noChangeMonth = month;
        noChangeDay = day;
        noChangeYear = year;
        selMonth = month-1;
        selYear = year;
        selMinute = minute;
        selHour = hour;
        horaInicial = hour;
        minutoInicial = minute;
        horafinal = hour+1;
        minutofinal= minute;
        //String pattern = getString(R.string.date_pattern);
       // SimpleDateFormat sdf = new SimpleDateFormat(pattern);
       // c.set(year, month, day, hour, minute);

    }

    private void setJustTime(int hour, int minute, boolean isInicial){
        Calendar c = Calendar.getInstance();
        selMinute = minute;
        selHour = hour;
        JSONObject json = new JSONObject();
        JSONObject manJson = new JSONObject();
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        c.set(0,0,0,hour, minute);
        if (isInicial){
            int horaInitInSec = hour*3600 + minute*60;
           /* int horaFimInSec = (Integer) listNE.get(rightPosition).get(EventDate.HOUREND)*3600 + (Integer) listNE.get(rightPosition).get(EventDate.MIMEND)*60;
            if( horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }*/
            try {
                /*listNE.get(rightPosition).put(EventDate.HOURINIT,hour);
                listNE.get(rightPosition).put(EventDate.MIMINIT,minute);*/
            }catch (Exception e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            horaInicial = hour;
            minutoInicial = minute;

        }else{
            //int horaInitInSec = (Integer) listNE.get(rightPosition).get(EventDate.HOURINIT)*3600 + (Integer) listNE.get(rightPosition).get(EventDate.MIMINIT)*60;
            int horaFimInSec = hour*3600 + minute*60;
           /* if(horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }*/
            try {
               /* listNE.get(rightPosition).put(EventDate.HOUREND,hour);
                listNE.get(rightPosition).put(EventDate.MIMEND,minute);*/
            }catch (Exception e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            horafinal = hour;
            minutofinal = minute;
        }
 /*       adapter.notifyDataSetChanged();*/
    }

    private void setJustDate(int year, int month, int day){
        Calendar c = Calendar.getInstance();

        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        c.set(year, month, day);
        long dateRecivedinMills = c.getTimeInMillis();
        if(System.currentTimeMillis() < dateRecivedinMills){
            selDay = day;
            selMonth = month;
            selYear = year;
            JSONObject json = new JSONObject();
            JSONObject manJson = new JSONObject();
            try {
               /* listNE.get(rightPosition).put(EventDate.DAY,day);
                listNE.get(rightPosition).put(EventDate.MONTH,month+1);
                listNE.get(rightPosition).put(EventDate.YEAR,year);
                adapter.notifyDataSetChanged();*/
            }catch (Exception e){
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
            /*adapter.notifyDataSetChanged();*/
        }else{
            showAlert(getString(R.string.erro_date_title), getString(R.string.erro_valid_date));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            getActivity().onBackPressed();
            return true;
        }
        return false;
    }

    @OptionsItem
    public void newEvent() {

            if(validateFields()) {

                showProgress("Criando evento...");



                int editCityVisb = cityChoice.getVisibility();
                /*final ParseObject event = ParseObject.create("Event");*/

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
                /*ParseRelation<ParseUser> relation = newEvent.getRelation(Event.OWNER);
                relation.add(ParseUser.getCurrentUser());*/
                event.put(Event.OWNER, ParseUser.getCurrentUser().getUsername());
                event.put(Event.TYPE, isCapeira());
                if (my64foto != null)
                    event.put(Event.FOTO, my64foto);
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
                    event.put("eventDate", jasonFinal);
                }*/


                Calendar date = Calendar.getInstance();
                date.set(selYear, selMonth, selDay, selHour, selMinute);
                event.put(Event.DATE,date.getTime());
                event.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            dismissProgress();
                            Intent returnIntent = new Intent();
                            getActivity().setResult(Activity.RESULT_OK,returnIntent);
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
        /*if (listNE.size()==0){
            Toast.makeText(getActivity(), "Escolha pelo menos uma data", Toast.LENGTH_SHORT).show();
            return false;
        }*/
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





    public void putTime() {
        TimePickerDialog tpd = new TimePickerDialog(getActivity(), this, selHour, selMinute, true);
        tpd.show();
    }


    public void putDate() {
        DatePickerDialog dpd = new DatePickerDialog(getActivity(), this, selYear, selMonth, selDay);
        dpd.show();
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
}