package br.com.iecapoeira;

import com.facebook.FacebookSdk;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.androidannotations.annotations.EApplication;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.chat.model.DBOpenHelper;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.model.Event;
import br.com.iecapoeira.model.Playlist;
import br.com.iecapoeira.model.UserDetails;

@EApplication
public class IEApplication extends MyApplication {


    private static final String PARSE_CLIENT_KEY = "MjL2NeRH7NPLBZq3KVno7Hf21i3JCrP4WutTNqe7";
    private static final String PARSE_APP_ID = "NurseI82xmOgKWs9ApXuxveQAeoKT4IcT0Yai9aO";
    private static UserDetails userDetails;

    @Override
    public void onCreate() {
        super.onCreate();

        ParseObject.registerSubclass(UserDetails.class);
        ParseObject.registerSubclass(Event.class);
        ParseObject.registerSubclass(Aula.class);
        ParseObject.registerSubclass(Playlist.class);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, PARSE_APP_ID, PARSE_CLIENT_KEY);


        // Facebook
        FacebookSdk.sdkInitialize(getApplicationContext());
        ParseFacebookUtils.initialize(getApplicationContext());


//        PubnubService.startPubnubService();


    }

    public static DBOpenHelper getOpenHelper() {
        return OpenHelperManager.getHelper(getInstance(), DBOpenHelper.class);
    }

    public static ParseUser getUser(){
        return ParseUser.getCurrentUser();
    }

    public static UserDetails getUserDetails() {
        if (userDetails == null) {
            userDetails = (UserDetails) ParseUser.getCurrentUser().get(UserDetails.USER_DETAILS);
            try {
                return userDetails.fetchIfNeeded();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return userDetails;
    }
}
