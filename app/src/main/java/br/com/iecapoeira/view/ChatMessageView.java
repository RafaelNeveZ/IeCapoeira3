package br.com.iecapoeira.view;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import java.text.SimpleDateFormat;

import br.com.hemobile.ItemView;
import br.com.hemobile.util.PhotoUtil;
import br.com.iecapoeira.R;
import br.com.iecapoeira.chat.model.ChatMessage;

@EViewGroup(R.layout.item_chat)
public class ChatMessageView extends ItemView<ChatMessage> {

    @ViewById
    View root;

    @ViewById
    TextView text;

    @ViewById
    TextView textName;

    @ViewById
    TextView textData;

    @ViewById
    ImageView preview;

    private String sender;

    public ChatMessageView(Context context) {
        super(context);
    }

    @Override
    public void bind(ChatMessage chatMessage, int position) {
        if (chatMessage.isPhoto()) {
            text.setVisibility(GONE);
            preview.setVisibility(VISIBLE);
            PhotoUtil.setImageOnView(preview, chatMessage.getText(), android.R.drawable.stat_sys_warning, android.R.drawable.stat_sys_download);
        } else {
            text.setVisibility(VISIBLE);
            preview.setVisibility(GONE);
            text.setText(chatMessage.getText());
        }

        SimpleDateFormat simpDate;
        simpDate = new SimpleDateFormat("HH:mm");

        //textData.setText(DateUtils.formatDateTime(getContext(),chatMessage.getDate().getTime(),DateUtils.FORMAT_24HOUR));

        try {
            textData.setText(simpDate.format(chatMessage.getDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        LayoutParams layoutParams = (LayoutParams) root.getLayoutParams();
        //todo resources
        if (chatMessage.getSender().equals(sender)) {
            textName.setVisibility(GONE);
            root.setBackgroundResource(R.drawable.img_right_bubble);
            layoutParams.addRule(ALIGN_PARENT_RIGHT);
            text.setGravity(Gravity.LEFT);
//            preview.setBackgroundResource(0);
        } else {
            textName.setVisibility(VISIBLE);
            textName.setText(chatMessage.getSender());
            root.setBackgroundResource(R.drawable.img_left_bubble);
            layoutParams.addRule(ALIGN_PARENT_RIGHT, 0);
            text.setGravity(Gravity.LEFT);
//            preview.setBackgroundResource(R.color.bg_screen_verde);
        }
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}