package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import br.com.iecapoeira.R;
import br.com.iecapoeira.adapter.SearchableAdapter;
import br.com.iecapoeira.fragment.EventListFragment;

@EActivity(R.layout.activity_city)
public class CityChoiceActivity extends AppCompatActivity {

    @ViewById
    ListView myList;

    @ViewById
    EditText etSearch;



    String filtro="";


    ArrayList<HashMap<String, String>> formList;
    ArrayList<String> listaCidade;

    @AfterViews
    public void init() {
        startJSON();
        final SearchableAdapter mSearchableAdapter = new SearchableAdapter(
                this,
                listaCidade );

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


        myList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> myAdapter, View view, int myItemInt, long mylng) {
                        filtro =(String) (myList.getItemAtPosition(myItemInt));
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra("result",filtro);
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });

    }



    public void startJSON() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("City");

            formList = new ArrayList<HashMap<String, String>>();
            HashMap<String, String> m_li;
            listaCidade = new ArrayList<String>();


            for (int i = 0; i < m_jArry.length(); i++) {

                JSONObject jo_inside = m_jArry.getJSONObject(i);
//                String ID = jo_inside.getString("ID");
                String CIDADE = jo_inside.getString("Nome");
                listaCidade.add(CIDADE);
//                String ESTADO_ID = jo_inside.getString("Estado");
                //Add your values in your `ArrayList` as below:
                m_li = new HashMap<String, String>();
//                m_li.put("ID", ID);
                m_li.put("Nome", CIDADE);
//                m_li.put("Estado", ESTADO_ID);
                formList.add(m_li);

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        BufferedReader reader = null;
        try {
            InputStream is = this.getAssets().open("Cidades.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            super.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    }
