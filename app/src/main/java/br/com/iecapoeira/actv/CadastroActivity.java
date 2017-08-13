package br.com.iecapoeira.actv;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogOutCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseSession;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
@EActivity(R.layout.activity_cadastro)
@OptionsMenu(R.menu.new_event)
public class CadastroActivity extends AppCompatActivity {
    @ViewById
    EditText editName, editSecondName, editEmail, editTel, editCity, editCountry, editAssociation, editNickname, editPass, editPassAgain;

    private ProgressDialog progressDialog;

    private final Context context = this;

    @OptionsItem
    public void newEvent() {

        if(validateFields()) {
            showProgress("Cadastrando...");
            ParseUser newUser = new ParseUser();
            newUser.setUsername(editEmail.getText().toString());
            newUser.setPassword(editPass.getText().toString());
            newUser.setEmail(editEmail.getText().toString());
            newUser.put("emailVerified",false);
            newUser.put("Admin",false);
            newUser.put("name",editName.getText().toString());
            newUser.put("lastName",editSecondName.getText().toString());
            newUser.put("phone", editTel.getText().toString());
            newUser.put("city",editCity.getText().toString());
            newUser.put("Associacao",editAssociation.getText().toString());
            newUser.put("country",editCountry.getText().toString());
            newUser.put("nickname", editNickname.getText().toString());
            newUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        Toast.makeText(context, "Verifique no seu email", Toast.LENGTH_LONG).show();
                        dismissProgress();
                        startActivity(new Intent(context, LoginActivity_.class));
                        finish();
                    } else {
                        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        dismissProgress();
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


    public boolean validateFields(){

        String name = editName.getText().toString().trim();
        String secondName = editSecondName.getText().toString().trim();
        String apelido = editNickname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String city = editCity.getText().toString().trim();
        String country = editCountry.getText().toString().trim();
        String tel = editTel.getText().toString().trim();
        String ass = editAssociation.getText().toString().trim();
        String pass = editPass.getText().toString().trim();
        String passAgain = editPassAgain.getText().toString().trim();

        if (name.isEmpty()) {
            setError(editName, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (secondName.isEmpty()) {
            setError(editSecondName, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (email.isEmpty()) {
            setError(editEmail, getString(R.string.msg_erro_campo_vazio));
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()){
            editEmail.setError(getString(R.string.error_valid_email));
            return false;
        }
        if (tel.isEmpty()) {
            setError(editTel, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        /*if (city.isEmpty()) {
            setError(editCity, getString(R.string.msg_erro_campo_vazio));
            return false;
        }*/
        if (country.isEmpty()) {
            setError(editCountry, getString(R.string.msg_erro_campo_vazio));
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
        if (pass.isEmpty()) {
            setError(editPass, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (passAgain.isEmpty()) {
            setError(editPassAgain, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (!pass.equals(passAgain)) {
            setError(editPassAgain, getString(R.string.msg_erro_senha_dif));
            return false;
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, LoginActivity_.class));
            finish();
            return true;
        }
        return false;
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
