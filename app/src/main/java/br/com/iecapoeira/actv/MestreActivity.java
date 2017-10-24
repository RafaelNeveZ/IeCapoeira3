package br.com.iecapoeira.actv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Mestre;

@EActivity(R.layout.act_mestrefilter)
public class MestreActivity extends AppCompatActivity {

    @ViewById
    Toolbar toolbar;

    @ViewById
    TextView textoMestre, tvNome;

    @ViewById
    ImageView imgMestre;


    public static  ParseObject mestre;

    @AfterViews
    void ini(){
        setHeader();
        prepareContent();
    }

    private void prepareContent(){
        tvNome.setText((String)mestre.get("nome"));
        textoMestre.setText((String) mestre.get("historia"));
        if(mestre.get("foto")!=null) {
            ParseFile image = (ParseFile) mestre.get("foto");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        imgMestre.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }else {

        }
    }

    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_menu);
        toolbar.setTitle(getString(R.string.title_mestres));
        setSupportActionBar(toolbar);
    }
  /*  // makes the back on action bar works as back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }*/
}
