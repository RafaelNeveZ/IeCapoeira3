package br.com.iecapoeira.actv;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import br.com.iecapoeira.R;
import br.com.iecapoeira.fragment.MestreFragment;
import br.com.iecapoeira.model.Mestre;

//import br.com.thiengo.tcmaterialdesign.domain.Car;
//import br.com.thiengo.tcmaterialdesign.fragments.CarFragment;

public class SimpleTabsActivity extends ActionBarActivity {
    private static String TAG = "LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv_mestre);
        getSupportActionBar().setIcon(R.drawable.ic_logo);

        // FRAGMENT
        MestreFragment frag = (MestreFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if(frag == null) {
            frag = new MestreFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }
    }


    public List<Mestre> getSetMestreList(int qtd){
        String[] models = new String[]{"Mestre Canjiquinha", "Mestre Paulo dos Anjos", "Mestre Caiçara", "Mestre Traíra", "Mestre Waldemar da Liberdade", "Mestre Cobrinha Verde", "Mestre Eziquiel","Mestre Pastinha,","Mestre Bimba","Mestre João Pequeno" };
        int[] photos = new int[]{R.drawable.mestre_canjiquinha, R.drawable.mestre_paulo_dos_anjos,R.drawable.mestre_caicara, R.drawable.mestre_traira,R.drawable.mestre_waldemar_da_lierdade,R.drawable.mestre_cobrinha_verde, R.drawable.mestre_eziquiel,
        R.drawable.mestre_pastinha,R.drawable.mestre_bimba,R.drawable.mestre_joao_pequeno};
        List<Mestre> listAux = new ArrayList<>();

        for(int i = 0; i < qtd; i++){
            //Mestre m = new Mestre( models[i % models.length],photos[i % models.length] );
            //listAux.add(m);
        }
        return(listAux);
    }

}