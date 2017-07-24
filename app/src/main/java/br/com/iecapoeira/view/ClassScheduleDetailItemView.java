package br.com.iecapoeira.view;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.utils.ImageUtil;
import br.com.iecapoeira.widget.TextViewPlus;

/**
 * Created by rafae on 23/07/2017.
 */

@EViewGroup(R.layout.item_class_days)
public class ClassScheduleDetailItemView  extends LinearLayout {

    Aula model;

    @ViewById
    LinearLayout llItem;

    @ViewById
    TextView tvHour, tvTime, tvPlace;

    public ClassScheduleDetailItemView(Context context) {
        super(context);
    }

    public void bind(Aula model) {
        this.model = model;
        setDaysText();
        tvTime.setText("SEXTA");
        tvHour.setText(model.getHorarioStart()+ " às "+model.getHorarioEnd());
        tvPlace.setText(model.getNomeAcademia());
    }

    public void setDaysText() {
        String days = "";
        if (model.getDiasSemana() != null) {
            for(int i = 0; i < model.getDiasSemana().size(); i++) {
                try {
                    days = days + getResources().getStringArray(R.array.days)[i];
                    if (i + 1 < model.getDiasSemana().size()) {
                        days += ",";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        tvTime.setText(days);
    }

}
