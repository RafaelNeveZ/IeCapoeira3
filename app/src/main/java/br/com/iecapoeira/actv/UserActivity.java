package br.com.iecapoeira.actv;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.LongClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Parceiro;

@EActivity(R.layout.activity_user)
@OptionsMenu(R.menu.new_event)
public class UserActivity extends AppCompatActivity {
    @ViewById
    EditText editEmail,editAssociation, editNickname;

   /* @ViewById
    Button btLogout;*/
    private ParseUser actualUser;
    private final Context context = this;
    private ProgressDialog progressDialog;
    private boolean mudouEmail=false;
    public static String safe="";
    public List<String> localList;
    String email, asso, nick;

    @AfterViews
    public void init() {

        setlist();

        actualUser = ParseUser.getCurrentUser();
        email = actualUser.getEmail();
        asso = actualUser.get("Associacao").toString();
        nick = actualUser.get("nickname").toString();
        editEmail.setText(actualUser.getEmail());
        editAssociation.setText(actualUser.get("Associacao").toString());
        editNickname.setText(actualUser.get("nickname").toString());
    }

    public void setlist(){
        localList=new ArrayList<>();
        showProgress("Carregando dados");
        ParseQuery query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> allusers, ParseException e) {
                if(e==null){
                    for (ParseUser templist: allusers
                            ) {
                        Log.d("list",templist.getEmail());
                        localList.add(templist.getEmail());
                    }
                    dismissProgress();

                }else{
                    Toast.makeText(context, "Ocorreu um erro desconhecido", Toast.LENGTH_LONG).show();
                    dismissProgress();
                }
            }
        });

    }


    @OptionsItem
    public void newEvent(){

        if(validateFields()){
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.choose_email);
            dialog.setTitle("Atualizar cadastro");
            dialog.show();
            final ParseUser editedUser = ParseUser.getCurrentUser();
            TextView text = (TextView) dialog.findViewById(R.id.confirm_logout);
            Button btY = (Button) dialog.findViewById(R.id.yes);
            Button btN = (Button) dialog.findViewById(R.id.no);
            btN.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            btY.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgress("Atualizando Cadastro...");
                    Log.e("TAG", editedUser.getObjectId());
                            if (ParseUser.getCurrentUser() != null) {
                              /*  if (!(editEmail.getText().toString().equals("" + actualUser.get("username")))) {*/
                                  /*  if (!(localList.contains(editEmail.getText().toString()))) {*/
                                        Log.d("mudei","mudei");
                                        editedUser.put("username", editEmail.getText().toString());
                                        editedUser.put("email", editEmail.getText().toString());
                       /*                 mudouEmail = true;*/
                                 /*   } else {
                                        Toast.makeText(context, "Email já em uso, escolha outro.", Toast.LENGTH_LONG).show();
                                    }
                                }
                                if(!mudouEmail)
                                actualUser.put("emailVerified", true);*/
                                editedUser.put("Associacao", editAssociation.getText().toString());
                                editedUser.put("nickname", editNickname.getText().toString());
                                editedUser.saveInBackground(new SaveCallback() {
                                    @Override
                                    public void done(ParseException e) {
                                        if (e == null) {
                                          /*  if (mudouEmail) {
                                                dismissProgress();
                                                *//*Toast.makeText(context, "Verifique seu email e entre novamente", Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(context, LoginActivity_.class));
                                                finish();
                                                mudouEmail = false;*//*
                                                dialog.dismiss();
                                            } else {*/
                                                actualUser = editedUser;
                                                Toast.makeText(context, "Informações cadastradas", Toast.LENGTH_LONG).show();
                                                dialog.dismiss();
                                                dismissProgress();
                                            /*}*/
                                        } else {
                                            actualUser.put("nickname", nick);
                                            actualUser.put("Associacao", asso);
                                            actualUser.put("email", email);
                                            actualUser.put("username", email);
                                            dismissProgress();
                                            dialog.dismiss();
                                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dismissProgress();
                                dialog.dismiss();
                                Toast.makeText(context,"Erro", Toast.LENGTH_LONG).show();
                            }
                     /*   }
                    });*/
                }
            });
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

   /* @Click
    public void btLogout() {
        showProgress("Saindo...");
        ParseUser.logOut();
        startActivity(new Intent(this, LoginActivity_.class));
        dismissProgress();
        finish();
    }*/

    public boolean validateFields(){


        String apelido = editNickname.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String ass = editAssociation.getText().toString().trim();

        if (email.isEmpty()) {
            setError(editEmail, getString(R.string.msg_erro_campo_vazio));
            return false;
        }else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(editEmail.getText().toString()).matches()){
            editEmail.setError(getString(R.string.error_valid_email));
            return false;
        }
        if (ass.isEmpty()) {
            setError(editAssociation, getString(R.string.msg_erro_campo_vazio));
            return false;
        }
        if (apelido.isEmpty()) {
            setError(editNickname, getString(R.string.msg_erro_campo_vazio));
            return false;
        }

        return true;
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
