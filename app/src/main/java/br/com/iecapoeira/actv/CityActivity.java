package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import br.com.iecapoeira.R;

@EActivity(R.layout.activity_city)
public class CityActivity extends AppCompatActivity {

    @ViewById
    ListView myList;



    String filtro="";


    ArrayList<HashMap<String, String>> formList;
    ArrayList<String> listaCidade;

    @AfterViews
    public void init() {
        startJSON();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listaCidade );

        myList.setAdapter(arrayAdapter);
        myList.setOnItemClickListener(
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
                });

    }



    public void startJSON() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONArray m_jArry = obj.getJSONArray("City");
            Log.d("TAG", m_jArry.length() + "");
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



}
