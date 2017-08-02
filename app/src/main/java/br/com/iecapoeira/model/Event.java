package br.com.iecapoeira.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;

import java.util.Date;

import br.com.hemobile.util.PhotoUtil;

@ParseClassName("Event")
public class Event extends ParseObject {

    public static final String DATE = "date";
    public static final String ADDRESS = "address";
    public static final String STATE = "state";
    public static final String NAME = "name";
    public static final String CITY = "city";
    public static final String PICTURE = "picture";
    public static final String DESCRIPTION = "description";
    public static final String COUNTRY = "country";
    public static final String OWNER = "owner";
    public static final String GOING = "going";
    public static final String TYPE = "type";

    private Bitmap bitmap;

    public Event() {

    }
    public  String getType(){return  get(TYPE).toString();}

    public String getName() {
        return get(NAME).toString();
    }

    public void setName(String name) {
        put(NAME,name);
    }

    public String getCity() {
        return get(CITY).toString();
    }

    public void setCity(String city) {
        put(CITY,city.toLowerCase());
    }

    public Bitmap getProfilePicture(final GetDataCallback callback) {
        ParseFile pf = null;
        try {
            pf = (ParseFile) get(PICTURE);
        } catch (Exception e) {
            fetchInBackground(new GetCallback<ParseObject>() {
                @Override
                public void done(ParseObject object, ParseException e) {
                    getProfilePicture(callback);
                }
            });
            return null;
        }
        if (pf == null) return null;
        if (bitmap == null)
            if (callback == null) {
                pf.getDataInBackground(new GetDataCallback() {
                    @Override
                    public void done(byte[] bytes, ParseException e) {
                        try {
                            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                        } catch (Exception e1) {}
                    }
                });
            } else {
                pf.getDataInBackground(callback);
            }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) throws ParseException {
        this.bitmap = bitmap;
        ParseFile parseFile = new ParseFile("picture.jpg", PhotoUtil.getBytes(bitmap));
        parseFile.save();
        put(PICTURE, parseFile);
    }

    public void setDescription(String description) {
        put(DESCRIPTION,description);
    }

    public String getDescription() {
        return get(DESCRIPTION).toString();
    }

    public void setCountry(String country) {
        put(COUNTRY,country);
    }

    public String getCountry() {
        return get(COUNTRY).toString();
    }

    public void setState(String state) {
        put(STATE,state);
    }

    public String getState() {
        return get(STATE).toString();
    }

    public void setAddress(String address) {
        put(ADDRESS,address);
    }

    public String getAddress() {
        return get(ADDRESS).toString();
    }

    public void setDate(Date date) {
        put(DATE, date);
    }

    public Date getDate() {
        return getDate(DATE);
    }

    public void setOwner(UserDetails user) {
        put(OWNER, user);
    }

    public UserDetails getOwner() {
        return (UserDetails) get(OWNER);
    }

    private void addGoing(UserDetails user) throws ParseException {
        ParseRelation<UserDetails> relation = getRelation(GOING);
        relation.add(user);
        save();
    }

    private void removeGoing(UserDetails user) throws ParseException {
        ParseRelation<UserDetails> relation = getRelation(GOING);
        relation.remove(user);
        save();
    }

    /**
     * Checa se o user está no evento.
     * Faz conexão com a internet!
     * @param user
     * @throws ParseException
     */
    public boolean isUserGoing(UserDetails user) throws ParseException {
        ParseRelation<UserDetails> relation = getRelation(GOING);
        ParseQuery<UserDetails> query = relation.getQuery();
        query.fromLocalDatastore();
        query.whereEqualTo("objectId", user.getObjectId());
        return query.getFirst() != null;
    }

    public void setUserGoing(UserDetails user, boolean isGoing) throws ParseException {
        if (isGoing) {
            addGoing(user);
        } else {
            removeGoing(user);
        }
    }

    public int getHowManyIsGoing() {
        int qtd = 0;
        try {
            qtd = getRelation(GOING).getQuery().count();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return qtd;
    }
}