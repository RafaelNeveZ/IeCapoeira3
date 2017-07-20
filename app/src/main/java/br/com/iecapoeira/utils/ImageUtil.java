package br.com.iecapoeira.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import br.com.iecapoeira.R;

/**
 * Created by Rafael on 10/08/16.
 */
public class ImageUtil {

    public static void setBitmapIntoImageView(ParseObject object, String key, final ImageView imageView, final int imageSize) {
        ParseFile fileObject = (ParseFile) object.get(key);
        fileObject.getDataInBackground(new GetDataCallback() {

            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // First decode with inJustDecodeBounds = true to check dimensions
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeByteArray(data, 0, data.length, options);

                    // Calculate inSampleSize
                    options.inSampleSize = calculateInSampleSize(options, imageSize, imageSize);

                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    imageView.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length, options));

                } else {
                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeResource(imageView.getResources(), R.id.iv_teacher, options);

                    // Calculate inSampleSize
                    options.inSampleSize = calculateInSampleSize(options, imageSize, imageSize);

                    // Decode bitmap with inSampleSize set
                    options.inJustDecodeBounds = false;
                    imageView.setImageBitmap(BitmapFactory.decodeResource(imageView.getResources(), R.id.iv_teacher, options));
                }
            }
        });
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
