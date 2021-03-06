package br.com.iecapoeira.actv;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.ImageUtil;

/**
 * Created by Rafael on 10/08/16.
 */
@EActivity(R.layout.actv_class_schedule_detail)
@OptionsMenu(R.menu.class_detail)
public class ClassScheduleDetailActivity extends AppCompatActivity {

    public static ParseObject model;
    public boolean IsNotAdmin = false;
    @ViewById
    Toolbar toolbar;


    @ViewById
    ImageView ivTeacher;

    @ViewById
    TextView tvTeacher, tvDescription, tvDays, tvTime, tvPlace;

    @OptionsMenuItem(R.id.delete)
    MenuItem delete;
    boolean owner = false;


    private ProgressDialog progressDialog;


    @OptionsMenuItem(R.id.edit)
    MenuItem edit;

    private final Context context=this;

    @AfterViews
    public void init() {
        if (model == null) {
            return;
        }

        if(model.get("Photo")!=null) {
            ParseFile image = (ParseFile) model.get("Photo");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivTeacher.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }
        tvTeacher.setText(model.get(Aula.GRADUACAO)+" "+model.getString(Aula.MESTRE));
        tvDescription.setText(model.getString(Aula.SOBREAULA));
        tvDays.setText(model.getString(Aula.DIASSEMANA));
        tvPlace.setText(model.getString(Aula.ENDERECO)+".\n"+model.get(Aula.CIDADE)+". "+model.get(Aula.ESTADO)+". "+model.get(Aula.PAIS)+".");
        tvTime.setText(model.getString(Aula.HORARIO_COMECO)+ " às "+ model.getString(Aula.HORARIO_FIM));
    }


    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_voltar);
        toolbar.setTitle(getString(R.string.class_schedule));
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.delete){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.titulo_delete_class)
                    .setPositiveButton(R.string.menu_delete, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            model.deleteInBackground(new DeleteCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if(e==null) {
                                        dismissProgress();
                                        finish();
                                    }else{
                                        dismissProgress();
                                        Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null).show();

        }
        if (item.getItemId() == R.id.edit)
            startActivityForResult(new Intent(this, EditNewClassActivity_.class), 10);

        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
            if (resultCode == Activity.RESULT_FIRST_USER) {

            }
        }
    }

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(this, getString(R.string.aguarde), text, true, false);
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




    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        edit.setVisible(false);
        delete.setVisible(false);
        showProgress("Carregando dados");
        ParseRelation<ParseObject> relation1 = model.getRelation("aulaOwner");
        ParseQuery<ParseObject> qry = relation1.getQuery();
        qry.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
        qry.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> users, ParseException e) {
                if(users.size()==0 && e==null){
                    owner =false;
                }else if(e==null) {
                    owner =true;
                }else{
                }
                if ((Boolean) ParseUser.getCurrentUser().get("Admin") || owner) {
                    Log.d("TAG", "ADM");
                    edit.setVisible(true);
                    delete.setVisible(true);
                } else {
                    Log.d("TAG", "Não é ADM");

                }
                dismissProgress();
            }
        });



        return true;

    }


}
