package br.com.hemobile.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import br.com.hemobile.MyApplication;

public class PhotoUtil {

    public static final int PICK_IMAGE = 9;
    public static final int PICK_CROPPED_IMAGE = 19;
    private static final String PHOTO_DIR_NAME = MyApplication.getAppName();
    private static final String GOOGLE_PHOTOS_PACKAGE_NAME = "com.google.android.apps.photos";
    private static final String TEMP_PHOTO_FILE = "temp.jpg";

    public static Bitmap resizeBitmap(String pathName) {
		Options opts = new Options();
		opts.inSampleSize = 1;
		opts.inJustDecodeBounds = true;
		Bitmap bitmap = null;
		boolean rightSize = false;
		while (!rightSize) {
			opts.inSampleSize = opts.inSampleSize*2;
			bitmap = BitmapFactory.decodeFile(pathName,opts);
			rightSize = opts.outHeight < 1000 && opts.outWidth < 2048;
		}
		opts.inJustDecodeBounds = false;
		bitmap = BitmapFactory.decodeFile(pathName,opts);
		return bitmap;
	}

    public static Bitmap resizeBitmap(Activity actv, Uri uriImage) {


        try {
            Options opts = new Options();
            opts.inSampleSize = 1;
            opts.inJustDecodeBounds = true;
            Bitmap bitmap = null;
            boolean rightSize = false;
            while (!rightSize) {
                opts.inSampleSize = opts.inSampleSize * 2;
                bitmap = BitmapFactory.decodeStream(actv.getContentResolver().openInputStream(uriImage),null, opts);
                rightSize = opts.outHeight < 1000 && opts.outWidth < 2048;
            }
            opts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeStream(actv.getContentResolver().openInputStream(uriImage), null, opts);
            return bitmap;
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap resizeBitmapThumb(Activity actv, Uri uriImage) {


        try {
            Options opts = new Options();
            opts.inSampleSize = 1;
            opts.inJustDecodeBounds = true;
            Bitmap resized = null;
            Bitmap bitmap = BitmapFactory.decodeStream(actv.getContentResolver().openInputStream(uriImage));
            if(bitmap !=null) {
                resized = ThumbnailUtils.extractThumbnail(bitmap, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
            }
            return resized;
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        }

        return null;
    }

    public static byte[] getBytes(Bitmap bmp) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }
	public static Uri getOutputMediaFileUri(){
	      return Uri.fromFile(getOutputMediaFile());
	}
	
	@SuppressLint("SimpleDateFormat")
	public static File getOutputMediaFile() {
		File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),PHOTO_DIR_NAME);
		
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d("MyCameraApp", "failed to create directory");
				return null;
			}
		}
		
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		File mediaFile = new File(mediaStorageDir.getPath() + File.separator
				+ "IMG_" + timeStamp + ".jpg");
		return mediaFile;
	}

	public static void showPhotoOnGallery(Context c, String path) {
		Intent i = new Intent();
		i.setAction(Intent.ACTION_VIEW);
		File file = new File(path);
		if (file.exists()) {
			Uri uri = Uri.fromFile(file);
			i.setDataAndType(uri, "image/jpeg");
			c.startActivity(i);
		} else {
			String msg;
			if (path.contains("/")){
				msg = "Esta foto já não está mais armazenada no seu aparelho.";
			} else {
				msg = "Baixando...";
			}
			Toast.makeText(c, msg, Toast.LENGTH_LONG).show();
		}
	}

	public static void setImageOnView(ImageView imageView, String path, int resNotFound, int resDownloading) {
		File file = new File(path);
		if (file.exists()) {
			imageView.setImageBitmap(resizeBitmap(path));
		} else {
			if (path.contains("/")) {
				imageView.setImageResource(resNotFound);
			} else {
				imageView.setImageResource(resDownloading);
			}
		}
	}

    public static void getImageFromGallery(Activity actv) {
        if (actv == null) return;
        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        actv.startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE);
    }


    public static Uri onGalleryResult(int requestCode, Intent data) {
        switch (requestCode) {
            case PICK_CROPPED_IMAGE:
      /*          if (data != null) {
                    Log.d("ra","pick croped");
                    Uri uri = getTempUri();
                    return uri;
                }*/
            case PICK_IMAGE:
                if (data != null && data.getData() != null) {
                    Log.d("ra","croped");
                    Uri _uri = data.getData();
                    return _uri;
                }
        }
        return null;
    }

    public static void getCroppedImageFromGallery(Activity actv) {
        if (actv != null && isPackageInstalled(GOOGLE_PHOTOS_PACKAGE_NAME,actv.getPackageManager())) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale",true);
            List<ResolveInfo> resolveInfoList = actv.getPackageManager().queryIntentActivities(intent, 0);
            for (int i = 0; i < resolveInfoList.size(); i++) {
                if (resolveInfoList.get(i) != null) {
                    String packageName = resolveInfoList.get(i).activityInfo.packageName;
                    if (GOOGLE_PHOTOS_PACKAGE_NAME.equals(packageName)) {
                        intent.setComponent(new ComponentName(packageName, resolveInfoList.get(i).activityInfo.name));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        actv.startActivityForResult(intent, 19);
                    }
                }
            }
        }else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("crop", "true");
            photoPickerIntent.putExtra("aspectX", 4);
            photoPickerIntent.putExtra("aspectY", 3);
            photoPickerIntent.putExtra("scale",true);

            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            actv.startActivityForResult(photoPickerIntent, PICK_CROPPED_IMAGE);
        }
        /*Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setPackage(GOOGLE_PHOTOS_PACKAGE_NAME);
        photoPickerIntent.setType("image*//*");
        photoPickerIntent.putExtra("crop", "true");
        photoPickerIntent.putExtra("aspectX", 4);
        photoPickerIntent.putExtra("aspectY", 3);
        photoPickerIntent.putExtra("scale",true);
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        actv.startActivityForResult(photoPickerIntent, PICK_CROPPED_IMAGE);*/
    }
    public static void launchGooglePhotosPicker(Fragment fragment) {
        if (fragment != null && isPackageInstalled(GOOGLE_PHOTOS_PACKAGE_NAME,fragment.getActivity().getPackageManager())) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale",true);
            List<ResolveInfo> resolveInfoList = fragment.getActivity().getPackageManager().queryIntentActivities(intent, 0);
            for (int i = 0; i < resolveInfoList.size(); i++) {
                if (resolveInfoList.get(i) != null) {
                    String packageName = resolveInfoList.get(i).activityInfo.packageName;
                    if (GOOGLE_PHOTOS_PACKAGE_NAME.equals(packageName)) {
                        intent.setComponent(new ComponentName(packageName, resolveInfoList.get(i).activityInfo.name));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        fragment.startActivityForResult(intent, 19);
                    }
                }
            }
        }else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("crop", "true");
            photoPickerIntent.putExtra("aspectX", 4);
            photoPickerIntent.putExtra("aspectY", 3);
            photoPickerIntent.putExtra("scale",true);

            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            fragment.startActivityForResult(photoPickerIntent, PICK_CROPPED_IMAGE);
        }
    }
    private static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public static void getCroppedImageFromGalleryFrag(Fragment fragment) {
        if (fragment != null && isPackageInstalled(GOOGLE_PHOTOS_PACKAGE_NAME,fragment.getActivity().getPackageManager())) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            intent.putExtra("crop", "true");
            intent.putExtra("aspectX", 4);
            intent.putExtra("aspectY", 3);
            intent.putExtra("scale",true);
            List<ResolveInfo> resolveInfoList = fragment.getActivity().getPackageManager().queryIntentActivities(intent, 0);
            for (int i = 0; i < resolveInfoList.size(); i++) {
                if (resolveInfoList.get(i) != null) {
                    String packageName = resolveInfoList.get(i).activityInfo.packageName;
                    if (GOOGLE_PHOTOS_PACKAGE_NAME.equals(packageName)) {
                        intent.setComponent(new ComponentName(packageName, resolveInfoList.get(i).activityInfo.name));
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
                        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                        fragment.startActivityForResult(intent, 19);
                    }
                }
            }
        }else{
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            photoPickerIntent.setType("image/*");
            photoPickerIntent.putExtra("crop", "true");
            photoPickerIntent.putExtra("aspectX", 4);
            photoPickerIntent.putExtra("aspectY", 3);
            photoPickerIntent.putExtra("scale",true);

            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
            photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
            fragment.startActivityForResult(photoPickerIntent, PICK_CROPPED_IMAGE);
        }
       /* Intent photoPickerIntent = new Intent(Intent.ACTION_PICK
                );
        photoPickerIntent.setPackage(GOOGLE_PHOTOS_PACKAGE_NAME);
        photoPickerIntent.setType("image*//*");
        photoPickerIntent.putExtra("crop", "true");
        photoPickerIntent.putExtra("aspectX", 4);
        photoPickerIntent.putExtra("aspectY", 3);
        photoPickerIntent.putExtra("scale",true);
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, getTempUri());
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        fragment.startActivityForResult(photoPickerIntent, PICK_CROPPED_IMAGE);*/
    }

    public static void getImageFromGalleryFrag(Fragment fragment) {
        if (fragment == null) return;
        Intent intent = new Intent();
//        intent.setAction(Intent.ACTION_PICK);
//        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        fragment.startActivityForResult(Intent.createChooser(intent, "Selecione uma imagem"), PICK_IMAGE);
    }

    private static Uri getTempUri() {
        return Uri.fromFile(getTempFile());
    }

    private static File getTempFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory(),TEMP_PHOTO_FILE);
            try {
                file.createNewFile();
            } catch (IOException e) {}
            return file;
        } else {
            return null;
        }
    }
}