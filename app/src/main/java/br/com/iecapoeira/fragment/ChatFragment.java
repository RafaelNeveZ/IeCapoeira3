package br.com.iecapoeira.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ListFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.SystemService;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.List;

import br.com.hemobile.BaseActivity;
import br.com.hemobile.MyApplication;
import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.IEApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.adapter.ChatMessageAdapter;
import br.com.iecapoeira.chat.ChatMessageService;
import br.com.iecapoeira.chat.PubnubPusher;
import br.com.iecapoeira.chat.TimePref_;
import br.com.iecapoeira.chat.model.ChatMessage;
import br.com.iecapoeira.chat.model.DBOpenHelper;
import br.com.iecapoeira.model.SubscribeHolder;

@EFragment(R.layout.frag_chat)
@OptionsMenu(R.menu.chat)
public class ChatFragment extends ListFragment {

    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 101;

    @FragmentArg
    String channel;

    @FragmentArg
    String chatName;

    @Bean
    ChatMessageAdapter adapter;

    @ViewById
    EditText editChat;

    @ViewById
    ImageButton btSend;

    @SystemService
    NotificationManager mNotificationManager;

    @Pref
    TimePref_ pref;

    @OptionsMenuItem
    MenuItem subscribe;

    @Bean
    SubscribeHolder subscribeHolder;

    private Uri fileUri;
    final private BroadcastReceiver broadcastReceiver = new MessageBroadcastReceiver();
    private String sender = "";

    @AfterViews
    public void init() {

        try {
            sender = IEApplication.getUserDetails().getName();
        }catch (NullPointerException ex){
            try {
                sender = ParseUser.getCurrentUser().getUsername();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }


        adapter.setSender(sender);

        try {
            getActivity().setTitle(chatName);
        }
        catch (IllegalStateException ex){
            ex.printStackTrace();
        }


        editChat.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (v.length() > 0) btSend();
                return true;
            }
        });

        editChat.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable ed) {
                btSend.setEnabled(ed.toString().trim().length() > 0);
            }
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {} // Do nothing
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {} // Do nothing
        });

        btSend.setEnabled(editChat.getText().toString().trim().length() > 0);

        setupListView();
        try {
            startPubnub();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MyApplication.getInstance(), R.string.msg_erro_desconhecido_acesso_chat, Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        try {
            subscribe.setIcon(subscribeHolder.isSubscribed(channel) ? R.drawable.ic_chat_notification_desable : R.drawable.ic_chat_notification_enable);
        } catch (Exception e) {
            setIconLater();
        }
    }

    @UiThread(delay = 1000)
    public void setIconLater() {
        try {
            subscribe.setIcon(subscribeHolder.isSubscribed(channel) ? R.drawable.ic_chat_notification_desable : R.drawable.ic_chat_notification_enable);
        } catch (Exception e) {
        }
    }

   /* @OptionsItem
    public void menuDenunciar() {
        final EditText edit = new EditText(getActivity());
        AlertDialog dialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.titulo_denuncia)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String string = edit.getText().toString();
                        if (!string.isEmpty()) {
                            ParseObject denuncia = new ParseObject("Denuncia");
                            denuncia.put("user", IEApplication.getUserDetails());
                            denuncia.put("text", string);
                            denuncia.put("sala", channel);
                            denuncia.saveEventually();
                        }
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setView(edit).show();
    }*/

    @OptionsItem
    public void subscribe() {
        boolean isSubscribed = subscribeHolder.isSubscribed(channel);
        subscribeHolder.saveSubscribe(channel, !isSubscribed);
        subscribe.setIcon(!isSubscribed ? R.drawable.ic_chat_notification_desable : R.drawable.ic_chat_notification_enable);
        Toast.makeText(getActivity(),!isSubscribed ? R.string.msg_toast_notif_chat_sim : R.string.msg_toast_notif_chat_nao,Toast.LENGTH_LONG).show();
    }

    private void sendSystemMessage(boolean isSubscribed) {
        if (IEApplication.hasInternetConnection()) {
            try {
                ChatMessage chatMessage = new ChatMessage(ChatMessage.SYSTEM_SENDER, channel, String.valueOf(isSubscribed) + "|" + sender);
                chatMessage.create();
                chatMessage.setDate(new Date());
                PubnubPusher.publish(channel, chatMessage, new ChatCallback(chatMessage));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_sem_conexao, Toast.LENGTH_LONG).show();
        }
    }

    private void startPubnub() throws Exception {
        Pubnub pubnub = PubnubPusher.initAndGetPubnub();

        int count = 50;
        PubnubPusher.getHistory(channel, count, new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                try {
                    JSONArray array = ((JSONArray) message).getJSONArray(0);
                    int length = array.length();
                    for (int i = 0; i < length; i++) {
                        ChatMessage chatMessage = PubnubPusher.parseResponse(array.get(i), ChatMessage.class);
                        if (chatMessage != null) {
                            PubnubPusher.handleMessage(chatMessage, false);
                        }
                    }
                } catch (Exception e) {
                }
                update();
            }
        });

        pubnub.time(new Callback() {
            @Override
            public void successCallback(String channel, Object message) {
                String s = (String) message;
                long time = Long.parseLong(s.substring(1, s.length() - 1));
                pref.initialTime().put(time);
            }
        });

        PubnubPusher.subscribe(channel);
        if (!subscribeHolder.isSubscribed(channel)) {
            sendSystemMessage(true);
        }
    }

    private void setupListView() {
        ListView listView = getListView();
        listView.setDivider(null);
        listView.setStackFromBottom(true);
        int oitoDp = IEApplication.dpToPixels(8);
        listView.setDividerHeight(oitoDp/2);
        listView.setPadding(oitoDp, 0, oitoDp, 0);
    }

    private DBOpenHelper getHelper() {
        return IEApplication.getOpenHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return null;
    }

    @Click
    public void btPhoto() {
        takePicture();
    }

    @OptionsItem
    public void gallery() {
        getPictureFromGallery();
    }

    @Click
    public void btSend() {
        if (IEApplication.hasInternetConnection()) {
            try {
                ChatMessage chatMessage = new ChatMessage(sender, channel, editChat.getText().toString());
                chatMessage.create();
                chatMessage.setDate(new Date());
                PubnubPusher.publish(channel, chatMessage, new ChatCallback(chatMessage));
                editChat.setText("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_sem_conexao, Toast.LENGTH_LONG).show();
        }
    }

    @UiThread
    public void update() {
        try {
            List<ChatMessage> messagesList = getHelper().getMessages(channel);
            adapter.setList(messagesList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    protected void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = PhotoUtil.getOutputMediaFileUri(); // create a file to save the image
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

        startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
    }

    protected void getPictureFromGallery() {
        PhotoUtil.getCroppedImageFromGallery(getActivity());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       try {
           if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
               if (resultCode == Activity.RESULT_OK) {
                   handlePhotoTaken();
               } else if (resultCode == Activity.RESULT_CANCELED) {
                   // User cancelled the image capture
               } else {
                   // Image capture failed, advise user
               }
           } else if (requestCode == PhotoUtil.PICK_CROPPED_IMAGE) {
               fileUri = PhotoUtil.onGalleryResult(requestCode, data);
               if (fileUri != null)
                   handlePhotoTaken();
           }
       }catch (Exception e){
           e.printStackTrace();
       }
    }

    private void handlePhotoTaken() {
        String path = fileUri.getPath();

        Bitmap photo = PhotoUtil.resizeBitmap(path);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();

        final ChatMessage chatMessage = new ChatMessage(sender, channel, path, ChatMessage.TYPE_PHOTO);
        try {
            chatMessage.setDate(new Date());
            chatMessage.create();
            update();
        } catch (Exception e1) {}

        final ParseFile pf = new ParseFile("photo.jpg", byteArray);

        Toast.makeText(getActivity(), R.string.msg_enviando_foto, Toast.LENGTH_LONG).show();

        pf.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    final ParseObject po = ParseObject.create("Photos");
                    po.put("file", pf);
                    po.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                try {
                                    chatMessage.setText(po.getObjectId());
                                    PubnubPusher.publish(channel, chatMessage, new ChatCallback(chatMessage, false));
                                } catch (Exception e1) {
                                    showErroFoto();
                                }
                            } else {
                                showErroFoto();
                            }
                        }
                    });
                } else {
                    showErroFoto();
                }
            }

            private void showErroFoto() {
                try {
                    Toast.makeText(getActivity(), R.string.msg_erro_envio_foto, Toast.LENGTH_LONG).show();
                } catch (Exception ex) {}
            }
        });
    }

    class ChatCallback extends Callback {

        private ChatMessage chatMessage;
        private boolean update;

        public ChatCallback(ChatMessage chatMessage) {
            this.chatMessage = chatMessage;
            this.update = true;
        }

        public ChatCallback(ChatMessage chatMessage, boolean update) {
            this.chatMessage = chatMessage;
            this.update = update;
        }

        @Override
        public void successCallback(String channel, Object message) {
            try {
                if (update)
                    chatMessage.update();
                ChatMessageService.updateTableUserMessage(chatMessage, channel, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            update();
            Log.d("PUBNUB_CALLBACK", "-- success sending -- " + channel + " " + message);
        }

        @Override
        public void errorCallback(String channel, PubnubError pubnubError) {
            showToastError(this);
            Log.d("PUBNUB_CALLBACK", "-- error sending -- " + channel + " " + pubnubError);
        }

        public void repairMessageSent() {
            if (chatMessage.getSender().equals(ChatMessage.SYSTEM_SENDER)) return;
            try {
                editChat.setText(chatMessage.getText());
                chatMessage.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @UiThread
    void showToastError(ChatCallback callback) {
        try {
            Toast.makeText(getActivity(), R.string.msg_erro_envio_mensagem, Toast.LENGTH_SHORT).show();
            callback.repairMessageSent();
            update();
        } catch (Exception e) {
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        IEApplication.getOpenHelper().clearUnreadTableUserMessage(channel);
        mNotificationManager.cancel(channel.hashCode());
        ChatMessageService.setCurrentChatId(channel);

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(ChatMessageService.ACTION_MESSAGE_RECEIVED));
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, new IntentFilter(ChatMessageService.ACTION_MESSAGE_UPDATED));
        ((ChatActivity)getActivity()).hideKeyboard();
    }

    @Override
    public void onPause() {
        super.onPause();
        ChatMessageService.setCurrentChatId(null);
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
        ((ChatActivity)getActivity()).hideKeyboard();
    }

    @Override
    public void onStop() {
        super.onStop();

        if (!subscribeHolder.isSubscribed(channel)) {
            sendSystemMessage(false);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        ChatMessage message = adapter.getItem(position);
        if (message.isPhoto()) {
            PhotoUtil.showPhotoOnGallery(getActivity(), message.getText());
        }
    }

    private class MessageBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ChatMessage message = (ChatMessage) intent.getSerializableExtra("message");
            if (channel.equals(message.getReceiver())) {
                update();
            }
        }
    }
}