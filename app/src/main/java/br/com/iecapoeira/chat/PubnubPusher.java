package br.com.iecapoeira.chat;

import android.util.Log;

import com.google.gson.GsonBuilder;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.chat.model.ChatMessage;

public class PubnubPusher {

	private static final String SUBSCRIBE_KEY = "sub-c-760c8636-4b35-11e4-a650-02ee2ddab7fe";
    private static final String PUBLISH_KEY = "pub-c-0195330c-949c-4bea-89e8-fb09a2901885";
	private static final String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	private static Pubnub pubnub;

    final static Callback callback = new Callback() {
        @Override
        public void connectCallback(String channel, Object message) {
            Log.d("PUBNUB_CALLBACK", "-- connect -- " + channel + " " + message);
        }

        @Override
        public void disconnectCallback(String channel, Object message) {
            Log.d("PUBNUB_CALLBACK", "-- disconnect -- " + channel + " " + message);
        }

        @Override
        public void successCallback(String channel, Object message) {
            ChatMessage chatMessage = parseResponse(message, ChatMessage.class);
            if(chatMessage != null) {
                handleMessage(chatMessage, true);
            }
            Log.d("PUBNUB_CALLBACK", "-- success -- " + channel + " " + chatMessage);
        }

        @Override
        public void errorCallback(String channel, Object message) {
            Log.d("PUBNUB_CALLBACK", "-- error -- " + channel + " " + message);
        }
    };

	public static Pubnub initAndGetPubnub() throws Exception {
		if (pubnub != null) return pubnub;
        pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY);
        return pubnub;
	}

    public static void subscribe(String channel) throws Exception {
        if (pubnub == null) {
            initAndGetPubnub();
        }
        pubnub.subscribe(channel, callback);
    }

    public static void getHistory(String channel, long from, long end, Callback historyCallback) throws Exception {
        if (pubnub == null) {
            initAndGetPubnub();
        }
        Log.d("PUBNUB_CALLBACK", "-- history -- " + from + "/" + end);
        pubnub.history(channel, from, end, historyCallback);
    }

    public static void getHistory(String channel, int count, Callback historyCallback) throws Exception {
        if (pubnub == null) {
            initAndGetPubnub();
        }
        pubnub.history(channel, count, historyCallback);
    }

	public static void publish(String channel, ChatMessage message, Callback callback) throws Exception {
		if (pubnub == null) {
			initAndGetPubnub();
		}

		JSONObject jso = new JSONObject();
		jso.put("obj", toJsonString(message));
		
		pubnub.publish(channel, jso, callback);
	}

	private static String toJsonString(Object obj) {
		GsonBuilder builder = new GsonBuilder().setDateFormat(DATE_PATTERN);
		return builder.create().toJson(obj);
	}

	private static <T> T fromJsonString(String obj, Class<T> c) {
		GsonBuilder builder = new GsonBuilder().setDateFormat(DATE_PATTERN);
		return builder.create().fromJson(obj, c);
	}

	public static <T> T parseResponse(Object message, Class<T> c) {
		String jsonMessage = "";
		try {
			jsonMessage = ((JSONObject)message).getString("obj");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		T ret = fromJsonString(jsonMessage, c);
		return ret;
	}

	public static void handleMessage(ChatMessage chatMessage, boolean showNotification) {
		MyApplication application = MyApplication.getInstance();
		ChatMessageService.notifyMessageReceived(application, chatMessage, showNotification);
	}
}