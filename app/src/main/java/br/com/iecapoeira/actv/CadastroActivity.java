package br.com.iecapoeira.actv;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;

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

    @OptionsItem
    public void newEvent() {


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
            startActivity(new Intent(this, DashboardActivity_.class));
            finish();
            return;
        }
        if (secondName.isEmpty()) {
            setError(editSecondName, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (email.isEmpty()) {
            setError(editEmail, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (tel.isEmpty()) {
            setError(editTel, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (city.isEmpty()) {
            setError(editCity, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (country.isEmpty()) {
            setError(editCountry, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (ass.isEmpty()) {
            setError(editAssociation, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (apelido.isEmpty()) {
            setError(editNickname, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (pass.isEmpty()) {
            setError(editPass, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (passAgain.isEmpty()) {
            setError(editPassAgain, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (!pass.equals(passAgain)) {
            setError(editPassAgain, getString(R.string.msg_erro_senha_dif));
            return;
        }



        startActivity(new Intent(this, DashboardActivity_.class));
        finish();
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
