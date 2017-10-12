package br.com.iecapoeira.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
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
import br.com.iecapoeira.widget.TextViewPlus;

@EViewGroup(R.layout.item_video)
public class VideoItemView extends ItemView<ParseObject> {


    @ViewById
    TextView tvNome;

    @ViewById
    TextViewPlus  tvAdd;


    private ParseObject obj;



    private Context con;
    private ProgressDialog progressDialog;

    public VideoItemView(Context context) {
        super(context);
        con=context;
    }

    @Override
    public void bind(final ParseObject obj, int positionj) {
        this.obj = obj;
            tvNome.setText(obj.getString("title"));
    }

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(con, "Aguarde...", text, true, false);
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