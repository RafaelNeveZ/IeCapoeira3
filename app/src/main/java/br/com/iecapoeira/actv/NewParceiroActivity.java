package br.com.iecapoeira.actv;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;


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

    private Bitmap bmp=null;

    @AfterViews
    void init() {

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

    @OptionsItem
    public void newEvent() {
        String name = editName.getText().toString().trim();
        Bitmap icon = bmp;
        boolean sponsorChecked = cbSponsor.isChecked();
        boolean partner = cbPartner.isChecked();
        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (icon == null) {
            Toast.makeText(this, getString(R.string.imagem_erro), Toast.LENGTH_LONG).show();
            return;
        }

        if (!sponsorChecked && !partner) {
            Toast.makeText(this, "defina categoria", Toast.LENGTH_LONG).show();
            return;
        }


        finish();


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
