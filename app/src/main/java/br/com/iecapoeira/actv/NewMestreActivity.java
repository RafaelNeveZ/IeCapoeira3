package br.com.iecapoeira.actv;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Parceiro;


@EActivity(R.layout.activity_new_mestre)
@OptionsMenu(R.menu.new_event)
public class NewMestreActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 23;

    @ViewById
    EditText editName, editHistory;

    @ViewById
    RadioButton btAngola, btRegional;

    @ViewById
    ImageButton btPhoto;

    @ViewById
    ImageView photo;

    private final Context context=this;
    boolean sponsorChecked;
    boolean partner ;
    private Bitmap bmp=null;
    private ProgressDialog progressDialog;
    public String my64foto;
    @AfterViews
    void init() {

    }

    @Click
    public void btPhoto() {
        galleyView();
    }

    public void galleyView(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (isReadStorageAllowed()) {
                PhotoUtil.getImageFromGallery(this);

            } else {
                requestStoragePermission();
            }
        } else {
            PhotoUtil.getImageFromGallery(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri = PhotoUtil.onGalleryResult(requestCode, data);
        Log.d("URI", uri + "");
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

    @OptionsItem
    public void newEvent() {

        if(validarCampos()){

            showProgress("Atualizando mestres..");

            ParseObject newMestre = ParseObject.create("Mestre");
            newMestre.put("nome",editName.getText().toString());

            if(bmp!=null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile(editName.getText().toString()+"_foto", image);
                newMestre.put("foto",file);
            }

            if(btAngola.isChecked())
                newMestre.put("tipo",0);
            else
                newMestre.put("tipo",1);

            newMestre.put("historia",editHistory.getText().toString());

            newMestre.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        dismissProgress();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    } else {
                        Log.d("ERRO:", e.getMessage());
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        dismissProgress();
                    }
                }
            });

        }

    }

    public boolean validarCampos(){

        String name = editName.getText().toString().trim();
        String history = editHistory.getText().toString().trim();

        Bitmap icon = bmp;

        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            return false ;
        }
        if (history.isEmpty()) {
            setError(editHistory, getString(R.string.msg_erro_campo_vazio));
            return false ;
        }
        if (icon == null) {
            Toast.makeText(this, getString(R.string.imagem_erro), Toast.LENGTH_LONG).show();
            return false ;
        }


        return true;

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