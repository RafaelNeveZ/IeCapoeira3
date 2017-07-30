package br.com.iecapoeira.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.DashboardItem;

/**
 * Created by rafae on 30/07/2017.
 */
@EViewGroup(R.layout.item_new_event)
public class NewEventItem  extends LinearLayout {

    @ViewById
    LinearLayout llItem;

    @ViewById
    Button btDate,btHour,btFinalHour;

    public NewEventItem(Context context) {
        super(context);
    }

    public NewEventItem(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind() {

    }



}
