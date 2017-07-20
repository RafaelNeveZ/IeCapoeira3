package br.com.iecapoeira.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import br.com.hemobile.util.PhotoUtil;

@ParseClassName("UserDetails")
public class UserDetails extends ParseObject {

    private static final int FEMALE = 0;
    private static final int MALE = 1;
    public static final String USER_DETAILS = "userDetails";
    public static final String FACEBOOK_ID = "fbId";
    public static final String EMAIL = "email";
    public static final String NAME = "name";
    private Bitmap profilePicture;

    public UserDetails() {

    }

    public String getName() {
        return get("name").toString();
    }

    public void setName(String name) {
        put("name", name);
    }

    public void setEmail(String email) {
        put(EMAIL, email);
    }

    public int getAge() {
        int age = 0;
        try {
            String s = get("bday").toString();
            // s = MM/dd/yyyy or null
            final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

            Calendar birtday = Calendar.getInstance();
            birtday.setTime(sdf.parse(s));
            Calendar today = Calendar.getInstance();
            age = today.get(Calendar.YEAR) - birtday.get(Calendar.YEAR);
            if (today.get(Calendar.DAY_OF_YEAR) < birtday.get(Calendar.DAY_OF_YEAR)) {
                age--;
            }
        } catch (Exception e) {
        }
        return age;
    }

    public void setBirthday(String bday) {
        put("bday", bday);
    }

    public Bitmap getProfilePicture(final GetDataCallback callback) {
        ParseFile pf = null;
        try {
            pf = (ParseFile) get("profile_pic");
        } catch (Exception e) {
            fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    getProfilePicture(callback);
                }
            });
            return null;
        }
        if (profilePicture == null)
            if (callback == null) {
                pf.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        try {
                            profilePicture = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        } catch (Exception e1) {
                        }
                    }
                });
            } else {
                pf.getDataInBackground(callback);
            }
        return profilePicture;
    }

    public void setProfilePicture(Bitmap profilePicture) throws ParseException {
        this.profilePicture = profilePicture;
        ParseFile parseFile = new ParseFile("profilePic.jpg", PhotoUtil.getBytes(profilePicture));
        parseFile.save();
        put("profile_pic", parseFile);
    }

    public void setGender(String gender) {
        put("gender", gender.equalsIgnoreCase("male") ? MALE : FEMALE);
    }

    public String getGender() {
        try {
            return getInt("gender") == MALE ? "Homem" : "Mulher";
        } catch (Exception e) {
            return "";
        }
    }

    public String getCity() {
        try {
            return get("city").toString();
        } catch (Exception e) {
            return "";
        }
    }

    public void setCity(String city) {
        put("city", city);
    }
}