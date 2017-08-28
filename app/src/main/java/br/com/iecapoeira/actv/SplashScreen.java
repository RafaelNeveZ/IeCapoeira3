package br.com.iecapoeira.actv;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.iecapoeira.R;

/**
 * Created by Felipe Berbert on 04/10/2016.
 */
@EActivity(R.layout.actv_splash)
public class SplashScreen extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGTH = 2000;



    @AfterViews
    void init() {
        final MediaPlayer mp = MediaPlayer.create(this,R.raw.splash_sound);
        mp.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(SplashScreen.this, LoginActivity_.class);
                SplashScreen.this.startActivity(mainIntent);
                SplashScreen.this.finish();
            }
        }, SPLASH_DISPLAY_LENGTH);
    }

}
