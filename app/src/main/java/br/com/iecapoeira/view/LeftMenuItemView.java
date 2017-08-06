package br.com.iecapoeira.view;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.widget.ImageView;
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
@EViewGroup(R.layout.item_left_menu)
public class LeftMenuItemView extends LinearLayout {

    DashboardItem model;

    @ViewById
    LinearLayout llItem;

    @ViewById
    TextViewPlus tvIcon;

    @ViewById
    TextView tvTitle;

    @ViewById
    ImageView imgView;

    public LeftMenuItemView(Context context) {
        super(context);
    }

    public LeftMenuItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(DashboardItem model) {
        this.model = model;

        llItem.setBackgroundColor(model.backgroundColor);
        if(this.model.iconCode!=null)
        tvIcon.setText(model.iconCode);
        if(this.model.image!=null)

        imgView.setImageBitmap(model.image);
        tvTitle.setText(model.title);
        tvTitle.setTextColor(model.titleColor);

    }
}
