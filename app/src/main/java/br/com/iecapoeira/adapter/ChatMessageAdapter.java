package br.com.iecapoeira.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

import br.com.hemobile.GenericBaseAdapter;
import br.com.hemobile.ItemView;
import br.com.iecapoeira.R;
import br.com.iecapoeira.chat.model.ChatMessage;
import br.com.iecapoeira.view.ChatMessageView;
import br.com.iecapoeira.view.ChatMessageView_;


@EBean
public class ChatMessageAdapter extends GenericBaseAdapter<ChatMessage> {

    private static final int VIEW_TYPE_SYSTEM_MESSAGE = 0;
    private static final int VIEW_TYPE_MESSAGE = 1;

    @RootContext
    Context c;

    private String sender;

    @Override
    public ItemView<ChatMessage> buildItemView() {
        ChatMessageView build = ChatMessageView_.build(c);
        build.setSender(sender);
        return build;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == VIEW_TYPE_MESSAGE)
            return super.getView(position, convertView, parent);
        else
            return getSystemMessage(convertView, position);
    }

    private View getSystemMessage(View convertView, int position) {
        TextView textView;
        if (convertView == null) {
            textView = new TextView(c);
            FrameLayout frameLayout = new FrameLayout(c);
            convertView = frameLayout;
            textView.setBackgroundResource(R.drawable.shape);
            textView.setTextColor(c.getResources().getColor(R.color.white));
            frameLayout.addView(textView, 0, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        } else {
            textView = (TextView) ((FrameLayout) convertView).getChildAt(0);
        }

        ChatMessage obj = getItem(position);
        String text[] = obj.getText().split("\\|");
        textView.setText(String.format(c.getString(R.string.msg_x_x_na_sala), text[1], Boolean.valueOf(text[0]) ? c.getString(R.string.msg_entrou) : c.getString(R.string.msg_saiu)));
        return convertView;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).getSender().equals(ChatMessage.SYSTEM_SENDER)) {
            return VIEW_TYPE_SYSTEM_MESSAGE;
        } else {
            return VIEW_TYPE_MESSAGE;
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
