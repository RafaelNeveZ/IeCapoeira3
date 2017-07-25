package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;

@EActivity(R.layout.activity_new_edital)
@OptionsMenu(R.menu.main)
public class NewEditalActivity extends AppCompatActivity {

    @ViewById
    EditText editTitle;

    @ViewById
    EditText editLink;
    @AfterViews
    public void init() {

    }

    @OptionsItem
    public void newEvent() {
        String title = editTitle.getText().toString().trim();
        String link = editLink.getText().toString().trim();
        if (title.isEmpty()) {
            setError(editTitle, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (link.isEmpty()) {
            setError(editLink, getString(R.string.msg_erro_campo_vazio));
            return;
        } Intent returnIntent = new Intent();
        returnIntent.putExtra("result",title);
        returnIntent.putExtra("link",link);
        setResult(Activity.RESULT_OK,returnIntent);
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
