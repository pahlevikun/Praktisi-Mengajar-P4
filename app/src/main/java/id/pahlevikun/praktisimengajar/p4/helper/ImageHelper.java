package id.pahlevikun.praktisimengajar.p4.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ImageHelper {

    public static boolean isFileExist(String path) {
        try {
            File file = new File(path);
            return file.exists() && !file.isDirectory();
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isBitmapValid(Bitmap bitmap) {
        if (bitmap == null) return false;
        Bitmap emptyBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        return !bitmap.sameAs(emptyBitmap);
    }

    public static ImageData createImageFile(Context context, StorageType type) {
        String timestamp = new SimpleDateFormat(
                "yyyyMMdd_HHmmssSSS",
                Locale.US
        ).format(new Date());
        File file;
        if (type == StorageType.TEMP) {
            try {
                file = File.createTempFile(
                        timestamp,
                        ".jpeg"
                );
            } catch (IOException exception) {
                throw new IllegalStateException("Unexpected value: " + type);
            }
        } else {
            switch (type) {
                case CACHE: {
                    file = getCachedDir(context);
                    break;
                }
                case STORAGE: {
                    file = getStorageDir(context);
                    break;
                }
                default:
                    throw new IllegalStateException("Unexpected value: " + type);
            }
            file = new File(file.getPath() + File.separator + timestamp + ".jpeg");
        }
        return new ImageData(
                file.getAbsolutePath(),
                file
        );
    }

    public static File getCachedDir(Context context) {
        String state = Environment.getExternalStorageState();

        if (state == null || state.startsWith(Environment.MEDIA_MOUNTED)) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                return file;
            }
        }
        return context.getCacheDir();
    }

    public static File getStorageDir(Context context) {
        File dir;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + File.separator + "PraktisiMengajar" + File.separator + ".nomedia");
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + File.separator + "PraktisiMengajar" + File.separator + ".nomedia");
        }
        if (!dir.exists()) {
            boolean success = dir.mkdirs();
            if (!success) dir = getCachedDir(context);
        }
        return dir;
    }

    public static class ImageData implements Serializable {
        private final String imagePath;
        private final File imageFile;

        public ImageData(String imagePath, File imageFile) {
            this.imagePath = imagePath;
            this.imageFile = imageFile;
        }

        public String getImagePath() {
            return imagePath;
        }

        public File getImageFile() {
            return imageFile;
        }
    }

    public enum StorageType {
        CACHE, STORAGE, TEMP
    }
}
