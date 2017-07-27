package br.com.iecapoeira.actv;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;
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
import br.com.iecapoeira.model.UserDetails;


@EActivity(R.layout.actv_login)
public class LoginActivity extends BaseActivity {

    @ViewById
//    EditText etAssociacao, etApelido, etEmail, etPassword;
    EditText  etEmail, etPassword;

    @ViewById
    TextView textForgot;

    private static final List<String> PERMISSIONS = Arrays.asList("email",
            "user_birthday",
            "user_activities",
            "user_location",
            "user_photos");

    @AfterViews
    public void init() {
        printKeyHash();

        if (ParseUser.getCurrentUser() != null) {
            goToMainList();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
    }

    private boolean validateFields() {
        boolean result = true;
//        if (etAssociacao.getText().toString().isEmpty()) {
//            etAssociacao.setError(getString(R.string.error_missing_field));
//            result = false;
//        }
        if (etEmail.getText().toString().isEmpty()) {
            etEmail.setError(getString(R.string.error_missing_field));
            result = false;
        } else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches()){
            etEmail.setError(getString(R.string.error_valid_email));
            result = false;
        }
//        if (etApelido.getText().toString().isEmpty()) {
//            etApelido.setError(getString(R.string.error_missing_field));
//            result = false;
//        }
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
            ParseUser newUser = new ParseUser();
//            newUser.setUsername(etApelido.getText().toString());
            newUser.setPassword(etPassword.getText().toString());
            newUser.setEmail(etEmail.getText().toString());
//            newUser.put("Associacao", etAssociacao.getText().toString());
            UserDetails details = new UserDetails();
            details.setEmail(etEmail.getText().toString());
//            details.setName(etApelido.getText().toString());
            final UserDetails finalDetails = details;
            final ParseUser finalUser = newUser;
            details.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e != null) {
                        readParseException(e);
                        return;
                    }

                    try {
                        finalDetails.pin();
                    } catch (ParseException e1) {
                        e1.printStackTrace();
                    }

                    finalUser.put(UserDetails.USER_DETAILS, finalDetails);
                    finalUser.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e != null) {
                                readParseException(e);
                                return;
                            }
                            dismissProgress();
                            goToMainList();
                        }
                    });
                }
            });
            newUser.signUpInBackground(new SignUpCallback() {
                public void done(ParseException e) {
                    if (e == null) {
                        startActivity(new Intent(getActivity(), DashboardActivity_.class));
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    @Click
    public void textForgot() {
        Toast.makeText(LoginActivity.this, "Esqueci minha senha", Toast.LENGTH_LONG).show();

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