package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.SearchableAdapter;
import br.com.iecapoeira.fragment.EventListFragment;
import br.com.iecapoeira.model.Event;

@EActivity(R.layout.activity_user_go)
public class UserGoActivity extends AppCompatActivity {

    @ViewById
    ListView myList;

    @ViewById
    EditText etSearch;

    public static Event thatEvent;

    String filtro="";

    private  final Context context=this;
    ArrayList<HashMap<String, String>> formList;
    ArrayList<String> listaUsers;





    @AfterViews
    public void init() {

        listaUsers= new ArrayList<>();
        ParseRelation<ParseUser> relation = thatEvent.getRelation(Event.EVENTGO);
        ParseQuery<ParseUser> query =relation.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if(e==null){
                    getList(users);
                    final SearchableAdapter mSearchableAdapter = new SearchableAdapter(
                            context,
                            listaUsers);

                    myList.setAdapter(mSearchableAdapter);

                    etSearch.addTextChangedListener(new TextWatcher() {

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            System.out.println("Text ["+s+"]");

                            mSearchableAdapter.getFilter().filter(s.toString());
                        }

                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count,
                                                      int after) {
                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                        }
                    });


                   /* myList.setOnItemClickListener(
                            new AdapterView.OnItemClickListener() {

                                @Override
                                public void onItemClick(AdapterView<?> myAdapter, View view, int myItemInt, long mylng) {
                                    filtro =(String) (myList.getItemAtPosition(myItemInt));
                                    Log.d("Cliquei no ", filtro);
                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result",filtro);
                                    setResult(Activity.RESULT_OK,returnIntent);
                                    finish();
                                }
                            });*/
                }else{

                }
            }
        });



    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return false;
    }

    public void getList(List<ParseUser> usersx) {

        for (ParseUser user: usersx
             ) {
            listaUsers.add(user.get("name") + " "+user.get("lastName"));
        }

    }


}
