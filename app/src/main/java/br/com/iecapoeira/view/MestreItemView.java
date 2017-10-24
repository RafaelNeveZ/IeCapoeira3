package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Mestre;

@EViewGroup(R.layout.item_mestre)
public class MestreItemView extends ItemView<ParseObject> {



    @ViewById
    ImageView ivFoto;

    @ViewById
    TextView tvNome;

    private ParseObject obj;



    private Context con;
    private ProgressDialog progressDialog;

    public MestreItemView(Context context) {
        super(context);
        con=context;
    }

    @Override
    public void bind(final ParseObject obj, int positionj) {
        this.obj = obj;
        tvNome.setText((String) obj.get("nome"));
        if(obj.get("foto")!=null) {
            ParseFile image = (ParseFile) obj.get("foto");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivFoto.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }else{
        }
    }


}