package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.widget.TextViewPlus;

@EViewGroup(R.layout.item_class_schedule)
public class ClassItemView extends ItemView<ParseObject> {



    ParseObject model;

    @ViewById
    public View viewTop;

    @ViewById
    ImageView ivTeacher;

    @ViewById
    TextView tvTeacher, tvDescription;



    private ParseObject obj;



    private Context con;
    private ProgressDialog progressDialog;

    public ClassItemView(Context context) {
        super(context);
        con=context;
    }

   /* private final GetDataCallback callback = new GetDataCallback() {
        @Override
        public void done(byte[] bytes, ParseException e) {
            setProfilePicture(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
        }
    };*/

    @Override
    public void bind(final ParseObject obj, int positionj) {
        this.model = obj;
        tvTeacher.setText(model.getString(Aula.MESTRE));
        if(model.getString(Aula.SOBREAULA).length()>30) {
            tvDescription.setText(model.getString(Aula.SOBREAULA).substring(0, 30) + "...");
        }else{
            tvDescription.setText(model.getString(Aula.SOBREAULA));
        }
        if(model.get(Aula.FOTO)!=null) {
            byte[] decodedString = Base64.decode(model.get(Aula.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivTeacher.setImageBitmap(decodedByte);
        }

    }

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(con, "Aguarde", text, true, false);
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

}