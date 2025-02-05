package meow.softer.mydiary.shared.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;

import meow.softer.mydiary.shared.FileManager;

import java.io.IOException;

public class ExifUtil {
    //EXIF:Exchangeable Image File可交换图像文件
    public static Bitmap rotateBitmap(String src, Bitmap bitmap) {
        try {
            int orientation = getExifOrientation(src);
            return rotate(bitmap, orientation);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    public static Bitmap rotateBitmap(Context context, Uri uri, Bitmap bitmap) {
        String path = FileManager.getRealPathFromURI(context, uri);
        if (path == null) {
            return bitmap;
        }
        return rotateBitmap(path, bitmap);
    }

    private static Bitmap rotate(Bitmap bitmap, int orientation) {
        if (orientation == 1) {
            return bitmap;
        }

        Matrix matrix = new Matrix();
        switch (orientation) {
            case 2:
                matrix.setScale(-1, 1);
                break;
            case 3:
                matrix.setRotate(180);
                break;
            case 4:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case 5:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case 6:
                matrix.setRotate(90);
                break;
            case 7:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case 8:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }

        try {
            Bitmap oriented = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            bitmap.recycle();
            return oriented;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private static int getExifOrientation(String src) throws IOException {
        int orientation = 1;

        try {
            ExifInterface exifInterface = new ExifInterface(src);
            orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
        } catch (SecurityException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return orientation;
    }
}
