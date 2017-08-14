package br.com.iecapoeira.actv;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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
import com.parse.ParseException;
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

    public static Aula model;
    public boolean IsNotAdmin = false;
    @ViewById
    Toolbar toolbar;


    @ViewById
    ImageView ivTeacher;

    @ViewById
    TextView tvTeacher, tvDescription, tvDays, tvTime, tvPlace;

    @OptionsMenuItem(R.id.delete)
    MenuItem delete;

    /*@OptionsMenuItem(R.id.singup)
    MenuItem singup;*/

    private ProgressDialog progressDialog;


    @OptionsMenuItem(R.id.edit)
    MenuItem edit;

    private final Context context=this;

    @AfterViews
    public void init() {
        //setHeader();
        if (model == null) {
            return;
        }

        if(model.get(Aula.FOTO)!=null) {
            byte[] decodedString = Base64.decode(model.get(Aula.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivTeacher.setImageBitmap(decodedByte);
        }
        tvTeacher.setText(model.getMestre());
        tvDescription.setText(model.getSobreAula());
        tvDays.setText(model.getDiasSemana());
        Log.e("TAG",model.getDiasSemana() +" " + model.getEndereco());
        tvPlace.setText(model.getEndereco());
        tvTime.setText(model.getHorarioStart()+ " às "+ model.getHorarioEnd());
    }


    public void setHeader() {
        toolbar.setNavigationIcon(R.drawable.logo_voltar);
        toolbar.setTitle(getString(R.string.class_schedule));
        setSupportActionBar(toolbar);
    }

    // makes the back on action bar works as back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == R.id.delete){
           /* showProgress("Deletando aula...");
            ParseQuery<Aula> query = ParseQuery.getQuery("Aulas");
            query.getInBackground(model.getObjectId(), new GetCallback<Aula>() {
                public void done(Aula thisClass, ParseException e) {
                    if (e == null) {*/
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

              /*      }else{*/

                 /*   }
                }
            });*/

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


       // checkClass();
        if ((Boolean) ParseUser.getCurrentUser().get("Admin")) {
            Log.d("TAG", "ADM");

            menu.getItem(1).setEnabled(true);
            menu.getItem(0).setEnabled(true);

        } else {
            Log.d("TAG", "Não é ADM");

            menu.getItem(1).setEnabled(false);
            menu.getItem(0).setEnabled(true);
        }


        return true;

    }

    /*public  void checkClass(){
        ParseQuery<Aula> query = ParseQuery.getQuery("Aulas");
        query.whereEqualTo(Event.OBJECTID, model.getObjectId());
        query.findInBackground(new FindCallback<Aula>() {
            @Override
            public void done(final List<Aula> models, ParseException e) {
                ParseRelation<ParseObject> relation1 = models.get(0).getRelation("aulaGo");
                ParseQuery<ParseObject> qry = relation1.getQuery();
                qry.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                qry.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> users, ParseException e) {
                        if(users.size()==0 && e==null){
                            singup.setIcon(R.drawable.ic_eventlist_cell_confirm_select);
                        }else if(e==null) {
                            singup.setIcon(R.drawable.ic_eventlist_cell_confirm_unselect);
                        }else{
                           // Toast.makeText(context,e.getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }*/




    /*@OptionsItem
    public void singup() {
            showProgress("Aguarde um momento...");
            ParseQuery<Aula> query = ParseQuery.getQuery("Aulas");
            query.whereEqualTo(Event.OBJECTID, model.getObjectId());
            query.findInBackground(new FindCallback<Aula>() {
                @Override
                public void done(final List<Aula> models, ParseException e) {
                    ParseRelation<ParseObject> relation1 = models.get(0).getRelation("aulaGo");
                    ParseQuery<ParseObject> qry = relation1.getQuery();
                    qry.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
                    qry.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> users, ParseException e) {
                            if(users.size()==0){
                                ParseRelation<ParseObject> relation = models.get(0).getRelation("aulaGo");
                                relation.add(ParseUser.getCurrentUser());
                                models.get(0).saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            ParseRelation<Aula> relation = ParseUser.getCurrentUser().getRelation("aulaGo");
                                            relation.add(models.get(0));
                                            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    Log.d("OK", "COLOCADO");
                                                    singup.setIcon(R.drawable.ic_eventlist_cell_confirm_unselect);
                                                    dismissProgress();
                                                }
                                            });
                                        } else {
                                            Log.d("OK", e.getMessage());
                                            dismissProgress();
                                        }
                                    }
                                });

                            }else{

                                ParseRelation<ParseObject> relation = models.get(0).getRelation("aulaGo");
                                relation.remove(ParseUser.getCurrentUser());
                                models.get(0).saveInBackground(new SaveCallback() {
                                    public void done(ParseException e) {
                                        if (e == null) {
                                            ParseRelation<Aula> relation = ParseUser.getCurrentUser().getRelation("aulaGo");
                                            relation.remove(models.get(0));
                                            ParseUser.getCurrentUser().saveInBackground();
                                            Log.d("OK", "APAGADO");
                                            singup.setIcon(R.drawable.ic_eventlist_cell_confirm_select);
                                            dismissProgress();
                                        } else {
                                            Log.d("OK", e.getMessage());
                                            dismissProgress();
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
            });

    }*/

}
