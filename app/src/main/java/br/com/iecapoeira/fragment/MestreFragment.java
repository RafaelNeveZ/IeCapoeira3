package br.com.iecapoeira.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.actv.MestreActivity;
import br.com.iecapoeira.actv.MestreActivity_;
import br.com.iecapoeira.actv.SimpleTabsActivity;
import br.com.iecapoeira.adapter.MestreAdapter;
import br.com.iecapoeira.model.Mestre;
import br.com.iecapoeira.widget.RecyclerViewOnClickListenerHack;


public class MestreFragment extends Fragment  {

    private RecyclerView mRecyclerView;
    private List<Mestre> mList;



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mestre, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                MestreAdapter adapter = (MestreAdapter) mRecyclerView.getAdapter();

                if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 3) {
                    List<Mestre> listAux = ((SimpleTabsActivity) getActivity()).getSetMestreList(7);

                    for (int i = 0; i < listAux.size(); i++) {
                        adapter.addListItem(listAux.get(i), mList.size());
                    }
                }
            }
        });


        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        mList = ((SimpleTabsActivity) getActivity()).getSetMestreList(7);
        MestreAdapter adapter = new MestreAdapter(getActivity(), mList);
        //adapter.setRecyclerViewOnClickListenerHack(this);
        mRecyclerView.setAdapter(adapter);


        return view;
    }


    public void onClickListener(View view, int position) {
        Toast.makeText(getActivity(), "Position: "+position, Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(getActivity(), MestreActivity_.class);
        // myIntent.putExtra("key", value); //Optional parameters
        MestreFragment.this.startActivity(myIntent);

    }
}