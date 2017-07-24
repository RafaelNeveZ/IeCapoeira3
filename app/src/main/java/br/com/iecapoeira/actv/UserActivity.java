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

@EActivity(R.layout.activity_user)
@OptionsMenu(R.menu.new_event)
public class UserActivity extends AppCompatActivity {
    @ViewById
    EditText editEmail,editAssociation, editNickname;

    @OptionsItem
    public void newEvent() {

    String apelido = editNickname.getText().toString().trim();
    String email = editEmail.getText().toString().trim();
    String ass = editAssociation.getText().toString().trim();


        if (email.isEmpty()) {
        setError(editEmail, getString(R.string.msg_erro_campo_vazio));
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
    startActivity(new Intent(this, DashboardActivity_.class));
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
