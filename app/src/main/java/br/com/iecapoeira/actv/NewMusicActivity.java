package br.com.iecapoeira.actv;

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

@EActivity(R.layout.activity_new_music)
@OptionsMenu(R.menu.main)
public class NewMusicActivity extends AppCompatActivity {
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
        String youtube = editLink.getText().toString().trim();
        if (title.isEmpty()) {
            setError(editTitle, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (youtube.isEmpty()) {
            setError(editLink, getString(R.string.msg_erro_campo_vazio));
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
