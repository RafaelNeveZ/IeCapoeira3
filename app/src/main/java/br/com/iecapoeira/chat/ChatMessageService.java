package br.com.iecapoeira.chat;


import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.androidannotations.annotations.EService;
import org.androidannotations.annotations.SystemService;

import java.io.FileOutputStream;
import java.sql.SQLException;

import br.com.hemobile.MyApplication;
import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.actv.ChatActivity_;
import br.com.iecapoeira.chat.model.ChatMessage;
import br.com.iecapoeira.chat.model.TableUserMessage;
import br.com.iecapoeira.model.UserDetails;

@EService
public class ChatMessageService extends IntentService {
    public static final String ACTION_MESSAGE_SENT = "br.com.singleline.app.action.MESSAGE_SENT";
    public static final String ACTION_MESSAGE_RECEIVED = "br.com.singleline.app.action.MESSAGE_RECEIVED";
    public static final String ACTION_MESSAGE_UPDATED = "br.com.singleline.app.action.MESSAGE_UPDATED";

    private static final String EXTRA_CHAT_MESSAGE = "br.com.singleline.app.extra.CHAT_MESSAGE";
    private static final String EXTRA_SHOW_NOTIFICATION = "br.com.singleline.app.extra.SHOW_NOTIFICATION";

    private static String currentChatId = null;

    @SystemService
    NotificationManager mNotificationManager;

    @SystemService
    Vibrator vibrator;

    public static void setCurrentChatId(String currentChatId) {
        ChatMessageService.currentChatId = currentChatId;
    }

    public static void notifyMessageSent(Context context, ChatMessage message) {
        Intent intent = new Intent(context, ChatMessageService_.class);
        intent.setAction(ACTION_MESSAGE_SENT);
        intent.putExtra(EXTRA_CHAT_MESSAGE, message);
        context.startService(intent);
    }
    public static void notifyMessageReceived(Context context, ChatMessage message) {
        notifyMessageReceived(context, message, true);
    }

    public static void notifyMessageReceived(Context context, ChatMessage message, boolean showNotification) {
        Intent intent = new Intent(context, ChatMessageService_.class);
        intent.setAction(ACTION_MESSAGE_RECEIVED);
        intent.putExtra(EXTRA_CHAT_MESSAGE, message);
        intent.putExtra(EXTRA_SHOW_NOTIFICATION, showNotification);
        context.startService(intent);
    }

    public ChatMessageService() {
        super("ChatMessageService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && !MyApplication.isAppBlocked()) {
            final String action = intent.getAction();
            final ChatMessage param1 = (ChatMessage) intent.getSerializableExtra(EXTRA_CHAT_MESSAGE);
            final boolean showNotification = intent.getBooleanExtra(EXTRA_SHOW_NOTIFICATION, true);
            if (ACTION_MESSAGE_SENT.equals(action)) {
                handleMessageSent(param1);
            } else if (ACTION_MESSAGE_RECEIVED.equals(action)) {
                if (!isBlocked(param1.getSender())) {
                    if (param1.isPhoto()) {
                        handlePhotoReceived(param1);
                    } else {
                        handleMessageReceived(param1, showNotification);
                    }
                }
            }
        }
    }

    private boolean isBlocked(String sender) {
        ParseQuery<UserDetails> query = ParseQuery.getQuery(UserDetails.class);
        try {
            return query.fromPin("blocked").find().contains(UserDetails.createWithoutData(UserDetails.class, sender));
        } catch (Exception e) {
        }
        return false;
    }

    private void handleMessageSent(ChatMessage message) {

    }

    private void handleMessageReceived(ChatMessage message, boolean showNotification) {
        try {
            message.create();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            boolean increment = false;
            String chatId = message.getReceiver();
            if ((currentChatId == null || !currentChatId.equals(chatId)) && showNotification) {
                // Mostra notificação se não estiver na tela do chat
                showNotification(message);
                increment = true;
            }

            updateTableUserMessage(message, chatId, increment);

            Intent intent = new Intent(ACTION_MESSAGE_RECEIVED);
            intent.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handlePhotoReceived(final ChatMessage message) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Photos");
        query.getInBackground(message.getText(), new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject obj, ParseException e) {
                if (e == null) {
                    ParseFile p = (ParseFile) obj.get("file");
                    p.getDataInBackground(new GetDataCallback() {

                        @Override
                        public void done(byte[] file, ParseException e) {
                            if (e == null) {
                                try {
                                    Uri photo = PhotoUtil.getOutputMediaFileUri();
                                    FileOutputStream fos = new FileOutputStream(photo.getPath());
                                    fos.write(file);
                                    fos.close();
                                    message.setText(photo.getPath());
                                    message.update();
                                    Intent intent = new Intent(ACTION_MESSAGE_UPDATED);
                                    intent.putExtra("message", message);
                                    LocalBroadcastManager.getInstance(ChatMessageService.this).sendBroadcast(intent);
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }
        });
        message.setText("Foto Recebida!");
        handleMessageReceived(message, true);
    }

    private void showNotification(final ChatMessage message) {
        String chatName = getChatName(message.getReceiver());
        if (chatName.isEmpty()) {

        } else {
            sendNotification(chatName, message);
        }
    }

    private void sendNotification(String chatName, ChatMessage message) {
        if (message.getSender().equals(ChatMessage.SYSTEM_SENDER)) return;

        Intent notificationIntent = new Intent(ChatMessageService.this, ChatActivity_.class);
        notificationIntent.putExtra(ChatActivity.EXTRA_ID, message.getReceiver());
        notificationIntent.putExtra(ChatActivity.EXTRA_PUSH, true);
        notificationIntent.putExtra(ChatActivity.EXTRA_CHAT_NAME, chatName);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(ChatMessageService.this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(ChatMessageService.this).
                setSmallIcon(R.drawable.ic_logo).
                setContentTitle(chatName).
                setContentIntent(contentIntent).
                setContentText(message.getText()).build();

        mNotificationManager.notify(message.getReceiver().hashCode(), notification);
        vibrator.vibrate(350);
    }

    private String getChatName(String receiver) {
        try {
            int indexSala = Integer.parseInt(receiver.split("\\|")[0]);
            final int[] item = {R.string.sala1, R.string.sala2,
                    R.string.sala3, R.string.sala4,
                    R.string.sala5, R.string.sala6};
            return getString(item[indexSala]);
        } catch (Exception ex) {}
        return "";
    }

    public static void updateTableUserMessage(ChatMessage message, String senderId, boolean incrementUnreadMessage) {
        TableUserMessage tum = new TableUserMessage(senderId, message.isPhoto() ? MyApplication.getInstance().getString(R.string.msg_foto_recebida) : message.getText(),message.getDate());
        try {
            tum.create();
        } catch (SQLException e) {
            try {
                TableUserMessage tum2 = tum.getDao().queryForId(tum.getUserId());
                tum2.setDateLastMessage(tum.getDateLastMessage());
                tum2.setLastMessage(tum.getLastMessage());
                if (incrementUnreadMessage) {
                    tum2.incrementQtdUnreadMessage();
                }
                tum2.incrementQtdMessage();
                tum2.update();
            } catch (SQLException e1) {
                e.printStackTrace();
            }
        }
    }

}
