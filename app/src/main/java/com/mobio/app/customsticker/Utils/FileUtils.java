package com.mobio.app.customsticker.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Abner on 15/9/22.
 * QQ 230877476
 * Email nimengbo@gmail.com
 * github https://github.com/nimengbo
 */
public class FileUtils {

    private static FileUtils instance = null;

    private static Context mContext;

    private static final String APP_DIR = "ByUser";

    private static final String TEMP_DIR = "Doodle/.TEMP";

    public static FileUtils getInstance(Context context) {
        if (instance == null) {
            synchronized (FileUtils.class) {
                if (instance == null) {
                    mContext = context.getApplicationContext();
                    instance = new FileUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 保存图像到本地
     *
     * @param bm
     * @return
     */
    public static String saveBitmapToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File file = FileUtils.getInstance(context).createTempFile("IMG_", ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    public static String saveBitmapWithoutCropToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File file = FileUtils.getInstance(context).createWithoutCropFile("IMG_", ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    public static String saveDoodleToLocal(Bitmap bm, Context context) {
        String path = null;
        try {
            File file = FileUtils.getInstance(context).createTempDoodleFile("IMG_", ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
            path = file.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return path;
    }

    /**
     * @param prefix
     * @param extension
     * @return
     * @throws IOException
     */
    public File createTempFile(String prefix, String extension)
            throws IOException {
        File file = new File(getAppDirPath() + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;

        //fullcopy
        /*File file = new File(getAppDirPath() + ".TEMP/" + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;*/
    }

    public File createWithoutCropFile(String prefix, String extension)
            throws IOException {
        File file = new File(getWithoutCropAppDirPath() + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;

        //fullcopy
        /*File file = new File(getAppDirPath() + ".TEMP/" + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;*/
    }

    public File createTempDoodleFile(String prefix, String extension)
            throws IOException {
        File file = new File(getDoodleDirPath() + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;

        //fullcopy
        /*File file = new File(getAppDirPath() + ".TEMP/" + prefix
                + System.currentTimeMillis() + extension);
        file.createNewFile();
        return file;*/
    }

    /**
     * 得到当前应用程序内容目录,外部存储空间不可用时返回null
     *
     * @return
     */
    public String getAppDirPath() {
        String path = null;
        if (getLocalPath() != null) {
            path = getLocalPath() + APP_DIR + "/";
        }
        return path;
    }

    public String getWithoutCropAppDirPath() {
        String path = null;
        if (getWithoutcropLocalPath() != null) {
            path = getWithoutcropLocalPath()  + "/";
        }
        return path;
    }

    public String getDoodleDirPath() {
        String path = null;
        if (getLocalPathDoodle() != null) {
            path = getLocalPathDoodle() + "/";
        }
        return path;
    }

    /**
     * 得到当前app的目录
     *
     * @return
     */
    private static String getLocalPath() {
        String sdPath = null;
        //sdPath = mContext.getFilesDir().getAbsolutePath() + "/";
        sdPath = Environment.getExternalStorageDirectory ()+ "/CustomSticker";
        return sdPath;
    }

    private static String getWithoutcropLocalPath() {
        String sdPath = null;
        //sdPath = mContext.getFilesDir().getAbsolutePath() + "/";
        sdPath = Environment.getExternalStorageDirectory ()+ "/CustomStickerWithoutCrop";
        return sdPath;
    }

    private static String getLocalPathDoodle() {
        String sdPath = null;
        //sdPath = mContext.getFilesDir().getAbsolutePath() + "/";
        sdPath = Environment.getExternalStorageDirectory ()+ "/CustomStickerDoodle";
        return sdPath;
    }

    /**
     * 检查sd卡是否就绪并且可读写
     *
     * @return
     */
    public boolean isSDCanWrite() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)
                && Environment.getExternalStorageDirectory().canWrite()
                && Environment.getExternalStorageDirectory().canRead()) {
            return true;
        } else {
            return false;
        }
    }

    private FileUtils() {
        // 创建应用内容目录
        if (isSDCanWrite()) {
            creatSDDir(APP_DIR);
            createSDDir();
            createSDDirWithoutCrop();
        }
    }

    /**
     * 在SD卡根目录上创建目录
     *
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(getLocalPath() + dirName);
        dir.mkdirs();
        return dir;
    }

    public File createSDDir() {
        File dir = new File(getLocalPathDoodle());
        dir.mkdirs();
        return dir;
    }

    public File createSDDirWithoutCrop() {
        File dir = new File(getWithoutcropLocalPath());
        dir.mkdirs();
        return dir;
    }
}
