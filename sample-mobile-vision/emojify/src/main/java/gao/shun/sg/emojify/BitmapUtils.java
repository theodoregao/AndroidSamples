package gao.shun.sg.emojify;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Theodore on 2017/10/23.
 */

public class BitmapUtils {

    private static final String FILE_PROVIDER_AUTHORITY = "gao.shun.sg.emojify.fileprovider";

    public static File createTempImageFile(Context context) throws IOException {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timestamp + "_";
        File storageDir = context.getExternalCacheDir();
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public static boolean deleteImageFile(Context context, String imagePath) {
        File imageFile = new File(imagePath);
        boolean deleted = imageFile.delete();

        if (!deleted) {
            String errorMessage = context.getString(R.string.error);
            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
        }

        return deleted;
    }

    public static Bitmap resamplePic(Context context, String imagePath) {
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int photoH = options.outHeight;
        int photoW = options.outWidth;
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath);
    }

    private static void galleryAddPic(Context context, String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imagePath);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    public static String saveImage(Context context, Bitmap image) {
        String savedImagePath = null;
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timestamp + ".jpg";
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Emojify");
        boolean success = true;
        if (!storageDir.exists()) success = storageDir.mkdirs();

        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream out = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, out);
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            galleryAddPic(context, savedImagePath);
            String savedMessage = context.getString(R.string.saved_message, savedImagePath);
            Toast.makeText(context, savedMessage, Toast.LENGTH_SHORT).show();
        }

        return savedImagePath;
    }

    public static void shareImage(Context context, String imagePath) {
        File imageFile = new File(imagePath);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri photoUri = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, imageFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoUri);
        context.startActivity(shareIntent);
    }

    public static void rotate(String filePath) throws IOException {
        Bitmap cameraBitmap = null;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inPurgeable = true;
        bmOptions.inBitmap = cameraBitmap;
        bmOptions.inMutable = true;

        cameraBitmap = BitmapFactory.decodeFile(filePath,bmOptions);
        // Your image file path
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        cameraBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);


        ExifInterface exif = new ExifInterface(filePath);
        float rotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        System.out.println(rotation);

        float rotationInDegrees = exifToDegrees(rotation);
        System.out.println(rotationInDegrees);

        Matrix matrix = new Matrix();
        matrix.postRotate(rotationInDegrees);

        Bitmap rotatedBitmap = Bitmap.createBitmap(cameraBitmap , 0, 0, cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix, true);
        FileOutputStream fos=new FileOutputStream(filePath);
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.write(bos.toByteArray());
        cameraBitmap.recycle();
        System.gc();
        fos.flush();
        fos.close();
    }

    private static float exifToDegrees(float exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) { return 90; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {  return 180; }
        else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {  return 270; }
        return 0;
    }
}
