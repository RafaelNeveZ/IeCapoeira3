package br.com.iecapoeira.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;

import br.com.iecapoeira.R;
import br.com.iecapoeira.font.Font;


/**
 * Created by Rafael Nunes on 20/08/2015.
 */
public class TextViewPlus extends TextView {

    public TextViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.TextViewPlus,
                0, 0);

        try {
            String fontName = a.getString(R.styleable.TextViewPlus_fontNameTextViewPlus);
            if(fontName != null && !"".equals(fontName)) {
                Font.changeFont(this, fontName);
            }
        } finally {
            a.recycle();
        }
    }
}
