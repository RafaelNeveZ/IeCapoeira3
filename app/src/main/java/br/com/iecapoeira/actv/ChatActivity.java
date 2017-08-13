package br.com.iecapoeira.actv;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;

import br.com.hemobile.BaseActivity;
import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.ChatFragment;
import br.com.iecapoeira.fragment.ChatFragment_;

@EActivity(R.layout.actv_chat)

public class ChatActivity extends AppCompatActivity {

    public static final String EXTRA_ID = "id";
    public static final String EXTRA_CHAT_NAME = "chatName";
    public static final String EXTRA_PUSH = "PUSH";
    private ChatFragment fragment;

    @AfterViews
    public void init() {
        Intent intent = getIntent();

        String id = intent.getStringExtra(EXTRA_ID);
        String chatName = intent.getStringExtra(EXTRA_CHAT_NAME);

        fragment = ChatFragment_.builder().channel(id).chatName(chatName).build();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content,fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getIntent().getBooleanExtra(EXTRA_PUSH, false)) {
            Intent intent = new Intent(this, MainActivity_.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }

    public void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
        }
    }
}