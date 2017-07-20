package br.com.iecapoeira.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.EditText;

import br.com.iecapoeira.R;
import br.com.iecapoeira.font.Font;


/**
 * Created by Rafael Nunes on 20/08/2015.
 */
public class EditTextPlus extends EditText {

    public EditTextPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.EditTextPlus,
                0, 0);

        try {
            String fontName = a.getString(R.styleable.EditTextPlus_fontNameEditTextPlus);
            if(fontName != null && !"".equals(fontName)) {
                Font.changeFont(this, fontName);
            }
        } finally {
            a.recycle();
        }
    }
}
