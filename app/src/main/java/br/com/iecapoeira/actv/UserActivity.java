package br.com.iecapoeira.actv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;

@EActivity(R.layout.activity_user)
@OptionsMenu(R.menu.new_event)
public class UserActivity extends AppCompatActivity {
    @ViewById
    EditText editEmail,editAssociation, editNickname;

    @ViewById
    Button btLogout;
    private ParseUser actualUser;
    private final Context context = this;
    private ProgressDialog progressDialog;

    @AfterViews
    public void init() {
        actualUser = ParseUser.getCurrentUser();
        editEmail.setText(actualUser.getEmail());
        editAssociation.setText(actualUser.get("Associacao").toString());
        editNickname.setText(actualUser.get("nickname").toString());
    }



    @OptionsItem
    public void newEvent(){

        if(validateFields()){
            showProgress("Atualizando Cadastro...");
            Log.e("TAG",actualUser.getObjectId());
            ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
            query.getInBackground(actualUser.getObjectId(), new GetCallback<ParseUser>() {
                public void done(ParseUser actualUser, ParseException e) {
                    if (e == null) {
                        actualUser.put("email",editEmail.getText().toString());
                        actualUser.put("Associacao",editAssociation.getText().toString());
                        actualUser.put("nickname",editNickname.getText().toString());
                        actualUser.saveInBackground();
                        dismissProgress();
                    }else{
                        dismissProgress();
                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
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

    @Click
    public void btLogout() {
        showProgress("Saindo...");
        ParseUser.logOut();
        startActivity(new Intent(this, LoginActivity_.class));
        dismissProgress();
        finish();
    }

    public boolean validateFields(){


        String apelido = editNickname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String ass = editAssociation.getText().toString().trim();

        if (email.isEmpty()) {
            setError(editEmail, getString(R.string.msg_erro_campo_vazio));
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()){
            editEmail.setError(getString(R.string.error_valid_email));
            return false;
        }
        if (ass.isEmpty()) {
            setError(editAssociation, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (apelido.isEmpty()) {
            setError(editNickname, getString(R.string.msg_erro_campo_vazio));
            return false;
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

}
