package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.RadioButton;
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
import br.com.iecapoeira.fragment.EventListFragment;
import br.com.iecapoeira.fragment.MusicaFragment;

@EActivity(R.layout.activity_new_music)
@OptionsMenu(R.menu.new_event)
public class NewMusicActivity extends AppCompatActivity {
    @ViewById
    EditText editTitle;

    @ViewById
    EditText editLink;

    @ViewById
    RadioButton rdRegional, rdAngola;

    private final Context context=this;
    @AfterViews
    public void init() {

    }

    @OptionsItem
    public void newEvent() {
        final String title = editTitle.getText().toString().trim();
        final String youtube = editLink.getText().toString().trim();
        if (title.isEmpty()) {
            setError(editTitle, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        if (youtube.isEmpty()) {
            setError(editLink, getString(R.string.msg_erro_campo_vazio));
            return;
        }
        ParseObject music = ParseObject.create("Music");
        music.put("title",title);
        music.put("link",youtube);
        music.put("type",(rdRegional.isChecked() ? MusicaFragment.REGIONAL : MusicaFragment.ANGOLA));
        music.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null) {
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("title", title);
                    returnIntent.putExtra("link", youtube);
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }else{
                    Toast.makeText(context, "Ocorreu um erro", Toast.LENGTH_LONG).show();
                }
            }
        });


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
