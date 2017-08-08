package br.com.iecapoeira.actv;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Edital;
import br.com.iecapoeira.model.Parceiro;

@EActivity(R.layout.activity_new_edital)
@OptionsMenu(R.menu.new_event)
public class NewEditalActivity extends AppCompatActivity {

    @ViewById
    EditText editTitle;

    @ViewById
    EditText editLink;
    private Dialog progressDialog;
    private final Context context=this;
    @AfterViews
    public void init() {

    }

    @OptionsItem
    public void newEvent() {


        if(validate()) {
            showProgress("Atualizando editais..");

            ParseObject newEdital = ParseObject.create("Edital");
            newEdital.put(Edital.TITLE,editTitle.getText().toString().trim());
            newEdital.put(Edital.LINK,editLink.getText().toString().trim());

            newEdital.saveInBackground(new SaveCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        dismissProgress();
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    } else {
                        Log.d("ERRO:", e.getMessage());
                        dismissProgress();
                    }
                }
            });
        }
    }


    public boolean validate(){
        final String title = editTitle.getText().toString().trim();
        final String link = editLink.getText().toString().trim();
        if (title.isEmpty()) {
            setError(editTitle, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (link.isEmpty()) {
            setError(editLink, getString(R.string.msg_erro_campo_vazio));
            return false ;
        }

        return true;
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
