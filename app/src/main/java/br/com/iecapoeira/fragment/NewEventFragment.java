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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.CityActivity_;
import br.com.iecapoeira.adapter.NewEventAdapter;
import br.com.iecapoeira.model.Event;
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

    @ViewById
    RecyclerView rcNew;

    private ProgressDialog progressDialog;

    private int selDay, selMonth, selYear;
    private int selHour, selMinute;
    private Bitmap bmp;
    private int horaInicial, minutoInicial, horafinal,minutofinal;
    private final static String TAG = "TAG";
    public List<NewEvent> listNE;
    private int eventDaysCount = 0;
    private NewEventAdapter adapter;
    public final  Context context= getContext();
    public int Day=0,Month=0,Year=0;
    public String newDate;
    private int rightPosition;

    @AfterViews
    public void init() {
//        setHeader();
        Calendar c = Calendar.getInstance();
        setupTime(c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DAY_OF_MONTH), (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
        rcNew.setHasFixedSize(false);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcNew.setLayoutManager(llm);
        rcNew.setNestedScrollingEnabled(false);
        listNE = new ArrayList<NewEvent>();
        listNE = setListNE() ;
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
        rcNew.setAdapter(adapter);

       /* NewEventAdapter adapter = new NewEventAdapter(NewEventActivity.this, listNE);
        ListView PaysListView = (ListView)findViewById(R.id.listView);
        rcNew.setAdapter(adapter);*/

    }

    public List<NewEvent> setListNE(){

        List<NewEvent> listAux = new ArrayList<>();

        NewEvent aux2 = new NewEvent(selDay,selMonth,selYear,selHour,selMinute,selHour+1,selMinute);
        listAux.add(aux2);
        eventDaysCount++;

        return  (listAux);

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

        Calendar c = Calendar.getInstance();


        selDay = day;
        selMonth = month;
        selYear = year;
        selMinute = minute;
        selHour = hour;
        horaInicial = hour;
        minutoInicial = minute;
        horafinal = hour+1;
        minutofinal= minute;
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        c.set(year, month, day, hour, minute);

    }

    private void setJustTime(int hour, int minute, boolean isInicial){
        Calendar c = Calendar.getInstance();
        selMinute = minute;
        selHour = hour;
        c.set(0,0,0,hour, minute);
        if (isInicial){
            int horaInitInSec = hour*3600 + minute*60;
            int horaFimInSec = horafinal*3600 + minutofinal*60;
            if( horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }
            listNE.get(rightPosition).setselHour(hour);
            listNE.get(rightPosition).setselMinute(minute);
            horaInicial = hour;
            minutoInicial = minute;

        }else{
            int horaInitInSec = horaInicial*3600 + minutoInicial*60;
            int horaFimInSec = hour*3600 + minute*60;
            if(horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }
            listNE.get(rightPosition).setFinalHour(hour);
            listNE.get(rightPosition).setFinalMinute(minute);
          //  btFinalHour.setText(String.format("%02d:%02d", hour, minute));
            horafinal = hour;
            minutofinal = minute;
        }
        adapter.notifyDataSetChanged();
    }

    private void setJustDate(int year, int month, int day){
        Calendar c = Calendar.getInstance();
        selDay = day;
        selMonth = month;
        selYear = year;
        String pattern = getString(R.string.date_pattern);
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        c.set(year, month, day);
        long dateRecivedinMills = c.getTimeInMillis();
        if(System.currentTimeMillis() < dateRecivedinMills){
            Log.i("POS: "+rightPosition, " "+ listNE.get(rightPosition).getselDay());
            Log.i("POS: "+(rightPosition-1), " "+ listNE.get(rightPosition).getselDay());
            listNE.get(rightPosition).setselDay(day);
            listNE.get(rightPosition).setselMonth(month);
            listNE.get(rightPosition).setselYear(year);
            adapter.notifyDataSetChanged();
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

    @OptionsItem
    public void newEvent() {

            if(validateFields()){

                showProgress("Criando evento...");

                ParseObject newEvent = ParseObject.create("Event");
                // newClass.put("foto",byteArray);
                newEvent.put(Event.NAME, editName.getText().toString());
                newEvent.put(Event.DESCRIPTION, editDesc.getText().toString());
                newEvent.put(Event.ADDRESS, editAddress.getText().toString());
                newEvent.put(Event.CITY, editCity.getText().toString());
                newEvent.put(Event.STATE, editState.getText().toString());
                newEvent.put(Event.COUNTRY, editCountry.getText().toString());
                newEvent.put(Event.HOURINIT,"15:00");
                newEvent.put(Event.HOUREND,"18:00");
                newEvent.put(Event.TYPE,1);
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

        Intent returnIntent = new Intent();
        getActivity().setResult(Activity.RESULT_OK,returnIntent);
        getActivity().finish();

            }

    @Click
    public void editCity(){
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



    public boolean validateFields(){

        String name = editName.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = editCity.getText().toString().trim();
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
        /*if (city.isEmpty()) {
            setError(editCity, getString(R.string.msg_erro_campo_vazio));
            return false;
        }*/
        /*if (state.isEmpty()) {
            setError(editState, getString(R.string.msg_erro_campo_vazio));
            return;
        }*/
        if (country.isEmpty()) {
            setError(editCountry, getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        return true;

    }

    @Click
    public void btPhoto() {
        galleyView();
    }

    public void galleyView(){
        if(isReadStorageAllowed()) {
            PhotoUtil.getCroppedImageFromGallery(getActivity());

        }else{
            requestStoragePermission();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
        if (uri != null) {
            bmp = PhotoUtil.resizeBitmap(getActivity(), uri);
            photo.setImageBitmap(bmp);
            photo.setBackgroundResource(android.R.color.transparent);
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

    @Click
    public void addOtherEvent() {

        NewEvent aux = new NewEvent(selDay,selMonth,selYear,selHour,selMinute,selHour+1,selMinute);
        listNE.add(aux);
        adapter.notifyItemInserted(listNE.size() - 1);
        adapter.notifyDataSetChanged();
        eventDaysCount++;

    }




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