package br.com.iecapoeira.font;

import android.graphics.Typeface;
import android.util.Log;
import android.view.View;

import br.com.iecapoeira.widget.ButtonPlus;
import br.com.iecapoeira.widget.EditTextPlus;
import br.com.iecapoeira.widget.TextViewPlus;


/**
 * Created by Rafael Nunes on 20/08/2015.
 */
// As fontes estão em src/main/assets/fonts
public class Font {

    // Substituir a tag pelos respectivos nomes das fontes: Roboto-Bold, Roboto-Regular, etc
    public static final String FONT_PATH = "fonts/{font}.ttf";
    public static final String FONT_TAG = "{font}";

    public static void changeFont(View view, String fontName) {
        if(view == null) {
            Log.w("Font", "view é nula");
            return;
        }
        if(fontName == null || "".equals(fontName)) {
            return;
        }

        try {
            Typeface typeFace = Typeface.createFromAsset(view.getContext().getAssets(), getFontPath(fontName));
            if (view instanceof TextViewPlus) {
                ((TextViewPlus) view).setTypeface(typeFace);
            } else if (view instanceof EditTextPlus) {
                ((EditTextPlus) view).setTypeface(typeFace);
            } else if (view instanceof ButtonPlus) {
                ((ButtonPlus) view).setTypeface(typeFace);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFontPath(String fontName) {
        String fontPath = FONT_PATH.replace(FONT_TAG, fontName);
        return fontPath;
    }
}
