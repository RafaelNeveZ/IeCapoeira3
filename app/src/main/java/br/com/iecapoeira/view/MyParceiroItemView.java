package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.utils.HETextUtil;

@EViewGroup(R.layout.item_partner)
public class MyParceiroItemView extends ItemView<ParseObject> {


    private ParseObject obj;

    @ViewById
    TextView nome;
    @ViewById
    ImageView ivLogo;
    private ProgressDialog progressDialog;

    public MyParceiroItemView(Context context) {
        super(context);
    }


    @Override
    public void bind(ParseObject obj, int positionj) {
        this.obj = obj;
        nome.setText((String)obj.get(Parceiro.NAME));
        if(obj.get("Photo")!=null) {
            ParseFile image = (ParseFile) obj.get("Photo");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivLogo.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }

    }




}