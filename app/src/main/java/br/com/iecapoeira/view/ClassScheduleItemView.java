package br.com.iecapoeira.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;

import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;

import br.com.iecapoeira.R;
import br.com.iecapoeira.model.Aula;
import br.com.iecapoeira.utils.ImageUtil;

/**
 * Created by Rafael on 09/08/16.
 */

@EViewGroup(R.layout.item_class_schedule)
public class ClassScheduleItemView extends RelativeLayout {

    Aula model;

/*    @ViewById
    public View viewTop;*/

    @ViewById
    ImageView ivTeacher;

    @ViewById
    TextView tvTeacher, tvDescription;

    public ClassScheduleItemView(Context context) {
        super(context);
    }

    public ClassScheduleItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(Aula model) {
        this.model = model;
        tvTeacher.setText(model.getMestre());
        tvDescription.setText(model.getSobreAula().substring(0,19)+"...");
        if(model.get(Aula.FOTO)!=null) {
            byte[] decodedString = Base64.decode(model.get(Aula.FOTO).toString(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivTeacher.setImageBitmap(decodedByte);
        }
        if(model.get("Photo")!=null) {
            ParseFile image = (ParseFile) model.get("Photo");
            image.getDataInBackground(new GetDataCallback() {
                public void done(byte[] data, ParseException e) {
                    if (e == null) {
                        Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                        ivTeacher.setImageBitmap(bmp);
                    } else {
                        Log.d("test", "There was a problem downloading the data.");
                    }
                }
            });
        }
    }
}
