package br.com.iecapoeira.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.androidannotations.annotations.RootContext;

/**
 * Created by pedrohenriqueborges on 11/24/14.
 */
public class HENetworkUtil {



    public static boolean isOnline(Activity actv) {
        ConnectivityManager cm =
                (ConnectivityManager) actv.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
