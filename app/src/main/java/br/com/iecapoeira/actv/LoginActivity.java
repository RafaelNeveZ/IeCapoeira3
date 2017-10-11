package br.com.iecapoeira.actv;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;

import br.com.hemobile.BaseActivity;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Parceiro;
import br.com.iecapoeira.model.UserDetails;


@EActivity(R.layout.actv_login)
public class LoginActivity extends BaseActivity {

    @ViewById
//    EditText etAssociacao, etApelido, etEmail, etPassword;
    EditText  etEmail, etPassword;

    @ViewById
    TextView textForgot , textResend;
    private int Whoisworng=0;
    private final Context context=this;

    private static final List<String> PERMISSIONS = Arrays.asList("email",
            "user_birthday",
            "user_activities",
            "user_location",
            "user_photos");

    @AfterViews
    public void init() {
      if (ParseUser.getCurrentUser() != null) {
          startActivity(new Intent(getActivity(), DashboardActivity_.class));
          finish();
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateFields() {
        boolean result = true;
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.error_missing_field));
            result = false;
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            etEmail.setError(getString(R.string.error_valid_email));
            result = false;
        }

        if (etPassword.getText().toString().isEmpty()) {
            etPassword.setError(getString(R.string.error_missing_field));
            result = false;
        }
        return result;
    }
    @Click
    public void btSingup() {
        goToMainList();
    }
    @Click
    public void btLogin() {
        if (validateFields()) {
            showProgress("Entrando...");
            ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
            query.whereEqualTo("username",etEmail.getText().toString());
            query.findInBackground( new FindCallback<ParseUser>() {
                public void done(List<ParseUser> newUser, ParseException e) {
                    if (newUser.size() != 0){
                            if (e == null) {
                                newUser.get(0).setUsername(etEmail.getText().toString());
                                newUser.get(0).setPassword(etPassword.getText().toString());
                                newUser.get(0).setEmail(etEmail.getText().toString());
                                newUser.get(0).logInInBackground(newUser.get(0).getUsername(), etPassword.getText().toString(), new LogInCallback() {
                                    @Override
                                    public void done(ParseUser parseUser, ParseException e) {
                                        if (parseUser != null) {
                                            startActivity(new Intent(getActivity(), DashboardActivity_.class));
                                            dismissProgress();
                                            finish();
                                        } else {
                                            dismissProgress();
                                            Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } else {
                                dismissProgress();
                                Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                            }

                    }else{
                        dismissProgress();
                        Toast.makeText(LoginActivity.this, "Invalid username/password.", Toast.LENGTH_LONG).show();
                    }
                }
            });

        }
    }

    @Click
    public void textForgot() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.forgot_dialog);
        dialog.setTitle("Recuperar senha");
        dialog.show();
        final EditText edit = (EditText)dialog.findViewById(R.id.edit_email_reset);
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
                dialog.dismiss();
                ParseUser.requestPasswordResetInBackground(edit.getText().toString(),new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Toast.makeText(LoginActivity.this, "O Email foi enviado", Toast.LENGTH_LONG).show();
                            dismissProgress();
                        }else{
                            Toast.makeText(LoginActivity.this, "O Email inválido", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });


    }
    @Click
    public void textResend() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.resend_dialog);
        dialog.setTitle("Reenviar email de ativação");
        dialog.show();
        final EditText editem = (EditText)dialog.findViewById(R.id.edit_email_send);
        final EditText editse = (EditText)dialog.findViewById(R.id.edit_email_pass);
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

               if (validateDialogFields(editem.getText().toString(),editse.getText().toString())) {
                   dialog.dismiss();
                   showProgress("Reenviando...");
                   ParseQuery<ParseUser> query = ParseQuery.getUserQuery();
                   query.whereEqualTo("email", editem.getText().toString());
                   query.findInBackground(new FindCallback<ParseUser>() {
                       public void done(List<ParseUser> newUser, ParseException e) {
                           if (newUser.size() != 0){
                               if (!((Boolean) newUser.get(0).get("emailVerified"))) {
                                   if (e == null) {
                                       newUser.get(0).logInInBackground(editem.getText().toString(), editse.getText().toString(), new LogInCallback() {

                                           @Override
                                           public void done(ParseUser actualUser, ParseException e) {
                                               if (actualUser != null) {
                                                   actualUser.put("username", editem.getText().toString());
                                                   actualUser.put("email", editem.getText().toString());
                                                   actualUser.saveInBackground(new SaveCallback() {
                                                       @Override
                                                       public void done(ParseException e) {
                                                           ParseUser.logOut();
                                                           Toast.makeText(LoginActivity.this, "Email enviado", Toast.LENGTH_LONG).show();
                                                           dismissProgress();
                                                       }
                                                   });
                                               } else {
                                                   dismissProgress();
                                                   Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                               }
                                           }
                                       });
                                   } else {
                                       dismissProgress();
                                       Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                                   }

                               } else {
                                   Toast.makeText(LoginActivity.this, "Conta já ativada", Toast.LENGTH_LONG).show();
                                   dismissProgress();
                               }
                       }else{
                               Toast.makeText(LoginActivity.this, "Usuário não encontrado", Toast.LENGTH_LONG).show();
                               dismissProgress();
                           }
                   }
                   });
               }else if(Whoisworng==1){
                   editem.setError(getString(R.string.error_missing_field));
               }else if(Whoisworng==2){
                   editse.setError(getString(R.string.error_missing_field));
               }
           }
        });
       }

    private boolean validateDialogFields(String email, String senha) {
       Whoisworng=0;
        boolean result = true;
        if (email.isEmpty()) {
            Whoisworng=1;
            result = false;
            return result;
        }
        if (senha.isEmpty()) {
            Whoisworng=2;
            result = false;
            return result;
        }
        return result;
    }

//    @Click
//    public void btLoginFacebook() {
//        final ParseUser currentUser = ParseUser.getCurrentUser();
//
//        // http://parseplatform.github.io/docs/android/guide/#facebook-users
//        if (currentUser == null || !ParseFacebookUtils.isLinked(currentUser)) {
//            ParseFacebookUtils.logInWithReadPermissionsInBackground(this, PERMISSIONS, new LogInCallback() {
//                @Override
//                public void done(ParseUser user, ParseException err) {
//                    if (user == null) {
//                        Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
//                    } else {
//                        if (user.isNew()) {
//                            Log.d("MyApp", "User signed up and logged in through Facebook!");
//                        } else {
//                            Log.d("MyApp", "User logged in through Facebook!");
//                        }
//                        startActivity(new Intent(getActivity(), DashboardActivity_.class));
//                        finish();
//                    }
//                }
//            });
//        }else {
////            getFacebookData();
//        }
//    }

    private void readParseException(ParseException err) {
        String strError = getString(R.string.msg_erro_desconhecido);
        if (err != null) {
            switch (err.getCode()) {
                case ParseException.USERNAME_TAKEN:
                case ParseException.EMAIL_TAKEN:
                    strError = getString(R.string.msg_erro_usuario_ja_existe);
                    break;
                case ParseException.MISSING_OBJECT_ID:
                    break;
                case ParseException.CONNECTION_FAILED:
                    strError = getString(R.string.msg_erro_sem_conexao);
                    break;
                case ParseException.INVALID_ACL:
                    strError = getString(R.string.msg_erro_acl);
                    break;
                case ParseException.INVALID_EMAIL_ADDRESS:
                    strError = getString(R.string.msg_erro_email_invalido);
                    break;

            }
        }
        Toast.makeText(getActivity(), strError, Toast.LENGTH_LONG).show();
        dismissProgress();

    }

   /* private void getFacebookData() {
        changeProgressMessage(getString(R.string.buscando_dados_facebook));
        PackageInstaller.Session session = ParseFacebookUtils.getSession();
        Bundle params = new Bundle();
        params.putString("fields", "email, age_range, location, picture.width(600).height(600),first_name, last_name, gender");
        new LoginClient.Request(session, "/me", params, HttpMethod.GET, new LoginClient.Request.Callback() {
            @Override
            public void onCompleted(Response response) {
                LoginResponse loginResponse = response.getGraphObjectAs(LoginResponse.class);
                handleFacebookDataInBackground(loginResponse);
            }
        }).executeAsync();
    }*/

//    @Background
//    public void handleFacebookDataInBackground(LoginResponse response) {
//        final ParseUser currentUser = ParseUser.getCurrentUser();
//
//        UserDetails details = (UserDetails) currentUser.get(UserDetails.USER_DETAILS);
//
//        if (details == null)
//            details = ParseObject.create(UserDetails.class);
//
//        currentUser.put(UserDetails.FACEBOOK_ID, response.getId());
//        String email = response.getEmail();
//        if (email != null) {
//            currentUser.put(UserDetails.EMAIL, email);
//        }
//
//        details.setName(response.getFirstName() + " " + response.getLastName());
//        details.setGender(response.getGender());
//
//        //get City
//        try {
//            String location = response.getLocation().getString(UserDetails.NAME);
//            details.setCity(location.split(",")[0].toLowerCase());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Bitmap bitmap = getRemoteImage(getPictureURLFromResponse(response));
//        try {
//            details.setProfilePicture(bitmap);
//            final UserDetails finalDetails = details;
//            details.saveInBackground(new SaveCallback() {
//                @Override
//                public void done(ParseException e) {
//                    if (e != null) {
//                        readParseException(e);
//                        return;
//                    }
//
//                    try {
//                        finalDetails.pin();
//                    } catch (ParseException e1) {
//                        e1.printStackTrace();
//                    }
//
//                    currentUser.put(UserDetails.USER_DETAILS, finalDetails);
//                    currentUser.saveInBackground(new SaveCallback() {
//                        @Override
//                        public void done(ParseException e) {
//                            if (e != null) {
//                                readParseException(e);
//                                return;
//                            }
//                            dismissProgress();
//                            goToMainList();
//                        }
//                    });
//                }
//            });
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//    }

    private void goToMainList() {
        startActivity(new Intent(getActivity(), CadastroActivity_.class));
        finish();
    }

//    public String getPictureURLFromResponse(LoginResponse response) {
//        JSONObject jsonObject = response.getPicture();
//        try {
//            return ((JSONObject) jsonObject.get("data")).get("url").toString();
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

//    public Bitmap getRemoteImage(final String s) {
//        try {
//            URL aURL = new URL(s);
//            final URLConnection conn = aURL.openConnection();
//            conn.connect();
//            final BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
//            final Bitmap bm = BitmapFactory.decodeStream(bis);
//            bis.close();
//            return bm;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public String printKeyHash() {
        // http://stackoverflow.com/questions/23674131/android-facebook-integration-invalid-key-hash
        PackageInfo packageInfo;
        String key = null;
        try {

            //getting application package name, as defined in manifest
            String packageName = getPackageName();

            //Retrieving package info
            packageInfo = getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);

            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

//    //   public interface LoginResponse extends GraphObject {
//    public interface LoginResponse {
//        public String getFirstName();
//
//        public String getLastName();
//
//        public String getId();
//
//        public JSONObject getPicture();
//
//        public String getEmail();
//
//        public String getGender();
//
//        public JSONObject getAgeRange();
//
//        public JSONObject getLocation();
//    }

}