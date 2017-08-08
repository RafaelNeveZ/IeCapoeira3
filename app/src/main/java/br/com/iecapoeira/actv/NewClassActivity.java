package br.com.iecapoeira.actv;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
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

    private int STORAGE_PERMISSION_CODE = 23;

    @ViewById
    ImageButton btPhoto;

    @ViewById
    ImageView photo;

    @ViewById
    EditText editName, editGraduation, editDesc,editAddress, editState, editCountry;

    @ViewById
    TextView editCity;

    @ViewById
    Button btHour;

    @ViewById
    Button btFinalHour;

    @ViewById
    Button addOtherClass;

    @ViewById
    RadioButton rdAngola, rdRegional;

    @ViewById
    CheckBox rdBtSeg, rdBtTer,rdBtQua,rdBtQui,rdBtSex,rdBtSab,rdBtDom;

    private ProgressDialog progressDialog;
    private int horaInicial, minutoInicial, horafinal,minutofinal;

    private int selHour, selMinute;
    private final Context context = this;
    private Bitmap bmp;
    private byte[] byteArray;
    public boolean isInit = true;
    public boolean dontLeave = false;
    public ParseObject newClass;

    @AfterViews
    public void init() {
        Calendar c = Calendar.getInstance();
        setupTime( (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
    }

    private void setupTime(int hour, int minute) {
        Calendar c = Calendar.getInstance();
        selMinute = minute;
        selHour = hour;
        horaInicial = hour;
        minutoInicial = minute;
        horafinal = hour+1;
        minutofinal= minute;
        c.set(2000, 1, 1, hour, minute);
        btHour.setText(String.format("%02d:%02d", hour, minute));
        btFinalHour.setText(String.format("%02d:%02d", hour, minute));

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
            btHour.setText(String.format("%02d:%02d", hour, minute));
            horaInicial = hour;
            minutoInicial = minute;

        }else{
            int horaInitInSec = horaInicial*3600 + minutoInicial*60;
            int horaFimInSec = hour*3600 + minute*60;
            if(horaFimInSec <= horaInitInSec) {
                showAlert(getString(R.string.erro_time_title), getString(R.string.erro_valid_time));
            }
            btFinalHour.setText(String.format("%02d:%02d", hour, minute));
            horafinal = hour;
            minutofinal = minute;
        }
    }

    public void showAlert(String title, String msg){
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
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
        if (requestCode == 19) {
            Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
            if (uri != null) {

                bmp = PhotoUtil.resizeBitmap(this, uri);
                float width = bmp.getWidth();
                float height = bmp.getHeight();
                int size = bmp.getRowBytes() * bmp.getHeight();
                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                bmp.copyPixelsToBuffer(byteBuffer);
                byteArray = byteBuffer.array();
                photo.setImageBitmap(bmp);
                photo.setBackgroundResource(android.R.color.transparent);
            }
        }
        if (requestCode == 5) {
            if(resultCode == Activity.RESULT_OK){
                String result=data.getStringExtra("result");
                editCity.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Você não escolheu a cidade", Toast.LENGTH_SHORT).show();
            }
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
        if(validateFields()) {
            showProgress("Criando aula...");

            ParseObject newClass = ParseObject.create("Aulas");
         // newClass.put("foto",byteArray);
            newClass.put("mestre", editName.getText().toString());
            newClass.put("estilo",isRegional());
            newClass.put("graduacao", editGraduation.getText().toString());
            newClass.put("sobre", editDesc.getText().toString());
            newClass.put("endereco", editAddress.getText().toString());
            newClass.put("cidade", editCity.getText().toString());
            newClass.put("estado", editState.getText().toString());
            newClass.put("pais", editCountry.getText().toString());
            newClass.put("horario",btHour.getText().toString());
            newClass.put("horarioFinal",btFinalHour.getText().toString());
            newClass.put("data",putDays());
            newClass.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        dismissProgress();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        dismissProgress();
                    }
                }
            });
             Log.e("TAG",putDays());
        }


      //    showProgress(getString(R.string.aguarde));

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

    private  String putDays(){
        String days="";
        String[] aux = new String[7];
        int auxCount =0;
        if(rdBtSeg.isChecked()) {
            aux[auxCount] = getText(R.string.segunda).toString();
            auxCount++;
        }
        if(rdBtTer.isChecked()){
            aux[auxCount] = getText(R.string.terca).toString();
            auxCount++;
        }
        if(rdBtQua.isChecked()){
            aux[auxCount] = getText(R.string.quarta).toString();
            auxCount++;
        }
        if(rdBtQui.isChecked()){
            aux[auxCount] = getText(R.string.quinta).toString();
            auxCount++;
        }
        if(rdBtSex.isChecked()){
            aux[auxCount] = getText(R.string.sexta).toString();
            auxCount++;
        }
        if(rdBtSab.isChecked()){
            aux[auxCount] = getText(R.string.sabado).toString();
            auxCount++;
        }
        if(rdBtDom.isChecked()){
            aux[auxCount] = getText(R.string.domingo).toString();
            auxCount++;
        }

        for(int i = 0; i<auxCount;i++){
            days+= aux[i];
            if (i + 1 < auxCount) {
                days += ", ";
            }
            if (i == auxCount - 1) {
                days += ".";
            }
        }



        if(auxCount==0)
            return "error";

        return days.toLowerCase();
    }

    private String isRegional() {
        if(rdRegional.isChecked())
        return (String) getText(R.string.regional);
        else
            return (String) getText(R.string.angola);
    }

    public boolean validateFields(){
        String name = editName.getText().toString().trim();

        String graduation = editGraduation.getText().toString().trim();
        String description = editDesc.getText().toString().trim();
        String address = editAddress.getText().toString().trim();
        String city = (String) getText(R.string.choose_city);
        String country = editCountry.getText().toString().trim();
        String state = editState.getText().toString().trim();


        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return false;
        }
       /* if (style.isEmpty()) {
        //    setError(editEstilo, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }*/
        if (graduation.isEmpty()) {
            setError(editGraduation, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return false;
        }
        if (address.isEmpty()) {
            setError(editAddress, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return false;
        }
        if (editCity.getText().equals(city)) {
            Toast.makeText(this, "Você não escolheu a cidade", Toast.LENGTH_SHORT).show();
            dontLeave = true;
            return false;
        }
        /*if (state.isEmpty()) {
            setError(editState, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return;
        }*/
        if (country.isEmpty()) {
            setError(editCountry, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return false;
        }

        if(putDays().equals("error")) {
            Toast.makeText(this, "Selecione os dias", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
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

    @Click
    public void editCity(){
        startActivityForResult(new Intent(context, CityActivity_.class), 5);
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
