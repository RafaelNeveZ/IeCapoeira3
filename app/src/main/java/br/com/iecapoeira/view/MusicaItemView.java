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
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.hemobile.ItemView;
import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.MyMusicaListFragment;
import br.com.iecapoeira.fragment.MyMusicaListFragment_;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.utils.HETextUtil;
import br.com.iecapoeira.widget.TextViewPlus;

@EViewGroup(R.layout.item_musica)
public class MusicaItemView extends ItemView<ParseObject> {


    public  static boolean  sacaninha = false;

    @ViewById
    TextView tvNome;

    @ViewById
    TextViewPlus  tvAdd;




    private ParseObject obj;



    private Context con;
    private ProgressDialog progressDialog;

    public MusicaItemView(Context context) {
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
        this.obj = obj;
            tvNome.setText(obj.getString("title"));

        if(!sacaninha){
            tvAdd.setText(R.string.icon_add);
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //TODO addplaylist
                    showProgress("Adicionando à playlist");
                    final ParseRelation<ParseObject> relation = ParseUser.getCurrentUser().getRelation("favoritos");
                    ParseQuery query = relation.getQuery();
                    query.whereEqualTo("objectId",obj.getObjectId());
                    query.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size() == 0) {
                                dismissProgress();
                                relation.add(obj);
                                ParseUser.getCurrentUser().saveInBackground();
                                Toast.makeText(con, "Adicionado", Toast.LENGTH_LONG).show();
                            } else {
                                dismissProgress();
                                Toast.makeText(con, "Esta música já está na playlist", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            });
        }else{
            tvAdd.setText(R.string.icon_remove);
            tvAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showProgress("Removendo da playlist");
                    ParseRelation<ParseObject> relation = ParseUser.getCurrentUser().getRelation("favoritos");
                    relation.remove(obj);

                    ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if(e==null){
                                Toast.makeText(con, "Deletado", Toast.LENGTH_LONG).show();
                                dismissProgress();
                            }else{
                                Toast.makeText(con, "Ocorreu um erro desconhecido", Toast.LENGTH_LONG).show();
                                dismissProgress();
                            }
                        }
                    });

                }
            });

        }
    }

    @UiThread
    public void showProgress(String text) {
        try {
            progressDialog = ProgressDialog.show(con, "aguarde", text, true, false);
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

 /*  public void subscribe(){
       ParseQuery<Event> query = ParseQuery.getQuery("Event");
       query.whereEqualTo(Event.OBJECTID,obj.getObjectId());
       query.findInBackground(new FindCallback<Event>() {
           @Override
           public void done(List<Event> models, ParseException e) {
               ParseRelation<ParseObject> relation = models.get(0).getRelation("eventGo");
               relation.add(ParseUser.getCurrentUser());
               models.get(0).saveInBackground(new SaveCallback() {
                   public void done(ParseException e) {
                       if (e == null) {
                           setImageBtGoing(true);
                           go=false;
                       } else {

                       }
                   }
               });
           }
       });
   }

   public  void unSubscribe(){
       ParseQuery<Event> query = ParseQuery.getQuery("Event");
       query.whereEqualTo(Event.OBJECTID,obj.getObjectId());
       query.findInBackground(new FindCallback<Event>() {
           @Override
           public void done(List<Event> models, ParseException e) {
               ParseRelation<ParseObject> relation = models.get(0).getRelation("eventGo");
               relation.remove(ParseUser.getCurrentUser());
               models.get(0).saveInBackground(new SaveCallback() {
                   public void done(ParseException e) {
                       if (e == null) {
                           setImageBtGoing(false);
                           go=true;
                       } else {

                       }
                   }
               });
           }
       });
   }*/
    /*@Click
    public void btGoing() {

        if(go) {
            subscribe();

        }else {
            unSubscribe();
        }

    }*/

    @Background
    void changeGoingOnParse(Boolean userGoing) {
      /*  try {
            obj.setUserGoing(IEApplication.getUserDetails(), userGoing);
        } catch (ParseException e) {
            e.printStackTrace();
            setImageBtGoing(!userGoing);
        }*/
    }




}