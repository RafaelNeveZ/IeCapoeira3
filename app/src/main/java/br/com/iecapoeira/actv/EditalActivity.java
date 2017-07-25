package br.com.iecapoeira.actv;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;

import br.com.iecapoeira.R;

@EActivity(R.layout.activity_edital)
@OptionsMenu(R.menu.new_remove_sponso)
public class EditalActivity extends AppCompatActivity {

    @ViewById
    ListView myList;

    ArrayList<String> listaEdital = new ArrayList<String>();
    ArrayAdapter<String> arrayAdapter;


    @AfterViews
    public void init() {
        listaEdital.add("Edital 1");
        listaEdital.add("Edital 2");
        listaEdital.add("Edital 3");

       arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                listaEdital );



        myList.setAdapter(arrayAdapter);
        myList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> myAdapter, View view, int myItemInt, long mylng) {
                        String linkChegou = (String) (myList.getItemAtPosition(myItemInt));
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://" + linkChegou));
                        startActivity(intent);
                    }
                });
    }

    @OptionsItem
    public void add() {
        startActivityForResult(new Intent(this, NewEditalActivity_.class), 10);
    }

    @OptionsItem
    public void remove() {

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 10) {
            if(resultCode == Activity.RESULT_OK){
                String title = data.getStringExtra("result");
                String link=data.getStringExtra("link");
                Toast.makeText(this, title, Toast.LENGTH_SHORT).show();
                arrayAdapter.add(link);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
