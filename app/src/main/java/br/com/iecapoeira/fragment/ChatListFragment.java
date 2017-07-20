package br.com.iecapoeira.fragment;

import android.content.Intent;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;

import br.com.hemobile.MyApplication;
import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.ChatActivity;
import br.com.iecapoeira.actv.ChatActivity_;

@EFragment
public class ChatListFragment extends ListFragment {

    private ListAdapter adapter;

    @AfterViews
    public void init() {
        adapter = new ListAdapter();
        setListAdapter(adapter);
        getListView().setDivider(null);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        if (MyApplication.hasInternetConnection()) {
            startActivity(new Intent(getActivity(), ChatActivity_.class).putExtra(ChatActivity.EXTRA_ID, position + "|chat-IE-room_" + position).putExtra(ChatActivity.EXTRA_CHAT_NAME, adapter.getItem(position).toString()));
        } else {
            Toast.makeText(getActivity(), R.string.msg_erro_no_internet, Toast.LENGTH_LONG).show();
        }
    }

    private class ListAdapter extends BaseAdapter {

        private int[] item = {R.string.sala1, R.string.sala2,
                                R.string.sala3, R.string.sala4,
                                R.string.sala5, R.string.sala6};

        @Override
        public int getCount() {
            return 6;
        }

        @Override
        public Object getItem(int i) {
            return getActivity().getString(item[i]);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.item_sala, null);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.text);
            textView.setText(getItem(position).toString());
            return convertView;
        }
    }
}