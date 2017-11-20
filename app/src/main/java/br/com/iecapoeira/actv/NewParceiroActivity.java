package br.com.iecapoeira.actv;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import com.parse.Parse;
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
import java.io.IOException;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Parceiro;


@EActivity(R.layout.activity_new_parceiro)
@OptionsMenu(R.menu.new_event)
public class NewParceiroActivity extends AppCompatActivity {

    private int STORAGE_PERMISSION_CODE = 23;

    @ViewById
    EditText editName;

    @ViewById
    CheckBox cbPartner, cbSponsor;

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
    private Uri imgUri = null;

    @AfterViews
    void init() {

    }

    @Click
    public void btPhoto() {
        galleyView();
    }

    public void galleyView(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
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

            try {
                Bitmap thumb =  PhotoUtil.resizeBitmapThumb(this, uri);
                imgUri = uri;
                photo.setImageBitmap(thumb);
                bmp = MediaStore.Images.Media.getBitmap(this.getContentResolver(),uri);
                photo.setBackgroundResource(android.R.color.transparent);
            } catch (IOException e) {
                e.printStackTrace();
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

    @OptionsItem
    public void newEvent() {

        if(validarCampos()){

            showProgress("Atualizando patrocinadores e parceiros..");

            ParseObject newParc = ParseObject.create("Parceiro");
            newParc.put(Parceiro.NAME,editName.getText().toString());

            if(bmp!=null){
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] image = stream.toByteArray();
                ParseFile file = new ParseFile(editName.getText().toString()+"_foto", image);
                newParc.put("Photo",file);
            }

            if(sponsorChecked)
                newParc.put(Parceiro.PART,true);
            else
                newParc.put(Parceiro.PART,false);

            if(partner)
                newParc.put(Parceiro.PARC,true);
            else
                newParc.put(Parceiro.PARC,false);

            newParc.saveInBackground(new SaveCallback() {
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
        Bitmap icon = bmp;

        sponsorChecked = cbSponsor.isChecked();
        partner = cbPartner.isChecked();
        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            return false ;
        }
        if (icon == null) {
            Toast.makeText(this, getString(R.string.imagem_erro), Toast.LENGTH_LONG).show();
            return false ;
        }

        if (!sponsorChecked && !partner) {
            Toast.makeText(this, "Defina categoria", Toast.LENGTH_LONG).show();
            return false;
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

    public void photoClick(View view) {

        if(imgUri!=null){
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(imgUri,"image/*");
            startActivity(intent);
        }

    }
}
