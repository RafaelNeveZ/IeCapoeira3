package br.com.iecapoeira.chat;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

import com.pubnub.api.Pubnub;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.sharedpreferences.Pref;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.model.SubscribeHolder;

@EService
public class PubnubService extends Service {

    PowerManager.WakeLock wl = null;

    @Bean
    SubscribeHolder holder;

    @Pref
    TimePref_ pref;

    public void onCreate() {
        super.onCreate();
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, getString(R.string.app_name));
        if (wl != null) {
            wl.acquire();
        }

        try {
            Log.i("PubnubService", "PubNub Starting");
            Pubnub pubnub = PubnubPusher.initAndGetPubnub();
            Log.i("PubnubService", "PubNub Started");
            final long end = System.currentTimeMillis() * 10000;

            String[] channels = holder.getChannels();
            for (int i = 0; i < channels.length; i++) {
                PubnubPusher.subscribe(channels[i]);
            }
        } catch (Exception e) {
            Log.i("PubnubService", "PubNub Not Started");
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (wl != null) {
            wl.release();
            wl = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public static void startPubnubService() {
        MyApplication instance = MyApplication.getInstance();

        try {
            PubnubPusher.initAndGetPubnub();
        } catch (Exception e) {}

        AlarmManager am = (AlarmManager) instance.getSystemService(Context.ALARM_SERVICE);
        Log.d("PubnubService", "-- startPushService --");

        Intent intent = new Intent(instance, BootReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(instance, 0,
                intent, PendingIntent.FLAG_CANCEL_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                (1 * 60 * 1000), pendingIntent); //Verifica a cada um minuto se o serviço está on
    }
}