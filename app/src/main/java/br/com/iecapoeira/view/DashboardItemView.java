package br.com.iecapoeira.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.DashboardItem;
import br.com.iecapoeira.widget.TextViewPlus;

/**
 * Created by Rafael on 11/08/16.
 */
@EViewGroup(R.layout.item_dashboard)
public class DashboardItemView extends LinearLayout {

    DashboardItem model;

    @ViewById
    LinearLayout llItem;

    @ViewById
    TextViewPlus tvIcon;

    @ViewById
    TextView tvTitle;

    public DashboardItemView(Context context) {
        super(context);
    }

    public DashboardItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(DashboardItem model) {
        this.model = model;

        tvIcon.setText(model.iconCode);
        tvTitle.setText(model.title);
        llItem.setBackgroundColor(model.backgroundColor);
    }
}
