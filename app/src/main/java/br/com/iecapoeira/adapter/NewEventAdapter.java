package br.com.iecapoeira.adapter;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;

/**
 * Created by rafae on 24/07/2017.
 */

public class NewEventAdapter extends BaseAdapter {

    private final List<Aula> cursos;
    private final Activity act;

    public NewEventAdapter(List<Aula> cursos, Activity act) {
        this.cursos = cursos;
        this.act = act;
    }


    @Override
    public int getCount() {
        return cursos.size();
    }

    @Override
    public Object getItem(int position) {
        return cursos.get(position);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {

        view = act.getLayoutInflater()
                .inflate(R.layout.item_class_schedule, null, false);
        Aula curso = cursos.get(position);

        //pegando as referências das Views
        TextView nome = (TextView)
                view.findViewById(R.id.tv_teacher);
        TextView descricao = (TextView)
                view.findViewById(R.id.tv_description);
        ImageView imagem = (ImageView)
                view.findViewById(R.id.iv_teacher);
        View vTop = (View)
                view.findViewById(R.id.view_top);

        //populando as Views
        nome.setText("ola");
        descricao.setText("mundo");
        imagem.setImageResource(R.drawable.ic_delete_white_24dp);


        return view;
    }


}