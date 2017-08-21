package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseObject;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Mestre;

@EViewGroup(R.layout.item_mestre)
public class MestreItemView extends ItemView<Mestre> {



    @ViewById
    ImageView ivFoto;

    @ViewById
    TextView tvNome;

    private Mestre obj;



    private Context con;
    private ProgressDialog progressDialog;

    public MestreItemView(Context context) {
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
    public void bind(final Mestre obj, int positionj) {
        this.obj = obj;
        tvNome.setText(this.obj.getNome());
        ivFoto.setImageResource(this.obj.getPhoto());
    }


}