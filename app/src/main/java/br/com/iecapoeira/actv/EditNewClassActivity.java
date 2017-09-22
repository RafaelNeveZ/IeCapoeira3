package br.com.iecapoeira.actv;

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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;

@EActivity(R.layout.activity_new_class)
@OptionsMenu(R.menu.new_event)
public class EditNewClassActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private int STORAGE_PERMISSION_CODE = 23;

    @ViewById
    ImageButton btPhoto;

    @ViewById
    ImageView photo;

    @ViewById
    EditText  editGraduation, editDesc,editAddress, editState, editCountry;

    @ViewById
    TextView editCity, editName;

    @ViewById
    Button btHour;


    @ViewById
    LinearLayout cityChoice;

    @ViewById
    Button btFinalHour;

    @ViewById
    Button addOtherClass;

    @ViewById
    RadioButton rdAngola, rdRegional;

    @ViewById
    CheckBox checkCountry;

    @ViewById
    EditText editTrueCity;


    @ViewById
    CheckBox rdBtSeg, rdBtTer,rdBtQua,rdBtQui,rdBtSex,rdBtSab,rdBtDom;

    private ProgressDialog progressDialog;
    private int horaInicial, minutoInicial, horafinal,minutofinal;
    private ParseObject mod;
    private int selHour, selMinute;
    private final Context context = this;
    private Bitmap bmp;
    private byte[] byteArray;
    public boolean isInit = true;
    public boolean dontLeave = false;
    public ParseObject newClass;
    private String my64foto=null;

    @AfterViews
    public void init() {
        int editCityVisb = cityChoice.getVisibility();
        addOtherClass.setVisibility(View.GONE);
        editCity.setText(getString(R.string.choose_city));
        mod =  ClassScheduleDetailActivity_.model;
        editName.setText(mod.get(Aula.MESTRE).toString());
        editGraduation.setText(mod.get(Aula.GRADUACAO).toString());
        editDesc.setText(mod.get(Aula.SOBREAULA).toString());
        editAddress.setText(mod.get(Aula.ENDERECO).toString());
        editState.setText(mod.get(Aula.ESTADO).toString());

        if(!mod.get(Aula.PAIS).toString().equals("Brasil")){
            checkCountry.setChecked(false);
            cityChoice.setVisibility(View.GONE);
            editCountry.setVisibility(View.VISIBLE);
            editCountry.setText(mod.get(Aula.PAIS).toString());
            editTrueCity.setVisibility(View.VISIBLE);
            editTrueCity.setText(mod.get(Aula.CIDADE).toString());
        }else {
            editCity.setText(mod.get(Aula.CIDADE).toString());
        }




        btHour.setText(mod.get(Aula.HORARIO_COMECO).toString());
        btFinalHour.setText(mod.get(Aula.HORARIO_FIM).toString());
        if(mod.get(Aula.FOTO)!=null) {
            byte[] decodedString = Base64.decode(mod.get(Aula.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            photo.setImageBitmap(decodedByte);
        }
        checkDays();
        String regional="Regional";

        if(regional.equals(mod.get(Aula.TIPOCAPOEIRA)))
            rdRegional.setChecked(true);
        else
            rdAngola.setChecked(true);

        Calendar c = Calendar.getInstance();
        setupTime( (c.get(Calendar.HOUR_OF_DAY) + 1) % 23, 0);
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

    private  void checkDays(){
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("segunda"))
            rdBtSeg.setChecked(true);
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("terça"))
            rdBtTer.setChecked(true);
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("quarta"))
            rdBtQua.setChecked(true);
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("quinta"))
            rdBtQui.setChecked(true);
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("sexta"))
            rdBtSex.setChecked(true);
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("sábado"))
            rdBtSab.setChecked(true);
        if(mod.get(Aula.DIASSEMANA).toString().toLowerCase().contains("domingo"))
            rdBtDom.setChecked(true);
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
        btFinalHour.setText(String.format("%02d:%02d", hour+1, minute));

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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (isReadStorageAllowed()) {
                PhotoUtil.getCroppedImageFromGallery(this);

            } else {
                requestStoragePermission();
            }
        } else {
            PhotoUtil.getCroppedImageFromGallery(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 19) {
            Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
            if (uri != null) {

                bmp = PhotoUtil.resizeBitmap(this, uri);

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();
                my64foto = Base64.encodeToString(byteArray, Base64.DEFAULT);
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

    /*@Click
    public void addOtherClass() {
        if(!dontLeave) {
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
                if(my64foto!=null)
                    newClass.put(Aula.FOTO,my64foto);
                newClass.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            dismissProgress();
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_FIRST_USER,returnIntent);
                            finish();
                        } else {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            dismissProgress();
                        }
                    }
                });
                Log.e("TAG",putDays());
            }
        }
        dontLeave = false;

    }*/

    @Click
    public void btFinalHour() {
        isInit = false;
        TimePickerDialog tpd = new TimePickerDialog(this, this, selHour, selMinute, true);
        tpd.show();
    }

    @OptionsItem
    public void newEvent() {
        if(validateFields()) {
            showProgress("Editando aula...");
            /*ParseQuery<ParseObject> query = ParseQuery.getQuery("Aulas");
            query.getInBackground(mod.getObjectId(), new GetCallback<ParseObject>() {
                public void done(ParseObject mod, ParseException e) {
                    if (e == null) {*/
                        mod.put(Aula.MESTRE, editName.getText().toString());
                        mod.put(Aula.TIPOCAPOEIRA,isRegional());
                        mod.put(Aula.GRADUACAO, editGraduation.getText().toString());
                        mod.put(Aula.SOBREAULA, editDesc.getText().toString());
                        mod.put(Aula.ENDERECO, editAddress.getText().toString());
                        int editCityVisb = cityChoice.getVisibility();
                        if(editCityVisb == View.VISIBLE) {
                            mod.put(Aula.PAIS, "Brasil");
                            mod.put(Aula.CIDADE, editCity.getText().toString());
                        }else{
                            mod.put(Aula.PAIS, editCountry.getText().toString());
                            mod.put(Aula.CIDADE, editTrueCity.getText().toString());
                        }

                        mod.put(Aula.ESTADO, editState.getText().toString());
                        mod.put(Aula.HORARIO_COMECO,btHour.getText().toString());
                        mod.put(Aula.HORARIO_FIM,btFinalHour.getText().toString());
                        mod.put(Aula.DIASSEMANA,putDays());
                        if(my64foto!=null)
                            mod.put(Aula.FOTO,my64foto);
                        mod.saveInBackground(new SaveCallback() {
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

                   /* }else{
                        dismissProgress();
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }*/
//                }
           /* });*/
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

    private  String putDays() {
        String days = "";
        String[] aux = new String[8];
        int auxCount = 0;
        String last = "";

        if (rdBtSeg.isChecked()) {
            aux[auxCount] = getText(R.string.segunda).toString();
            auxCount++;
            last = aux[auxCount];

        }
        if (rdBtTer.isChecked()) {
            aux[auxCount] = getText(R.string.terca).toString();
            auxCount++;
            last = aux[auxCount];
        }
        if (rdBtQua.isChecked()) {
            aux[auxCount] = getText(R.string.quarta).toString();
            auxCount++;
            last = aux[auxCount];

        }
        if (rdBtQui.isChecked()) {
            aux[auxCount] = getText(R.string.quinta).toString();
            auxCount++;
            last = aux[auxCount];

        }
        if (rdBtSex.isChecked()) {
            aux[auxCount] = getText(R.string.sexta).toString();
            auxCount++;
            last = aux[auxCount];

        }
        if (rdBtSab.isChecked()) {
            aux[auxCount] = getText(R.string.sabado).toString();
            auxCount++;
            last = aux[auxCount];

        }
        if (rdBtDom.isChecked()) {
            aux[auxCount] = getText(R.string.domingo).toString();
            auxCount++;
            last = aux[auxCount];
        }

        if (auxCount == 0)
            return "error";

        for (int i = 0; i < auxCount; i++) {
            days += aux[i];
            if (i + 1 < auxCount && i != auxCount - 2) {
                days += ", ";
            }

            if (i == auxCount - 2) {
                days = days + " e ";
            }
            if (i == auxCount - 1) {
                days = days + ".";
            }

            Log.d("DAYS", days);

        }
        String daysAux = days.toLowerCase();
        String s1 = daysAux.substring(0, 1).toUpperCase();
        String finalDays = s1 + daysAux.substring(1);
        Log.d("DAYS", finalDays);
        return finalDays;
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


     /*   if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            dontLeave = true;
            return false;
        }*/
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

        int editCityVisb = cityChoice.getVisibility();
        if(editCityVisb == View.VISIBLE) {
            if (editCity.getText().equals(city)) {
                Toast.makeText(this, "Escolha uma cidade", Toast.LENGTH_SHORT).show();
                dontLeave =true;
                return false;
            }
        }else{

            if (editTrueCity.getText().toString().isEmpty()) {
                setError(editTrueCity, getString(R.string.msg_erro_campo_vazio));
                dontLeave =true;
                return false;
            }
            if (editCountry.getText().toString().isEmpty()) {
                setError(editCountry, getString(R.string.msg_erro_campo_vazio));
                dontLeave =true;
                return false;
            }

        }
        if (address.isEmpty()) {
            setError(editAddress, getString(R.string.msg_erro_campo_vazio));
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
