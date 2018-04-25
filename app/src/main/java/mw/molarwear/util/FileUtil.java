package mw.molarwear.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import mw.molarwear.MolWearApp;
import mw.molarwear.R;
import mw.molarwear.data.classes.dental.molar.WearImageCacheMap;

import static java.io.File.separator;

/**
 * Utility class for tasks related to file I/O.
 *
 * @author <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
 * @author Sean Pesce
 * @author <a href="https://stackoverflow.com/users/1226354/vyacheslav-shylkin">Vyacheslav Shylkin</a>
 *
 * @see    Serializable
 */

public class FileUtil {

    public static class Cache {
        public static final WearImageCacheMap MOLAR_WEAR_IMAGES = new WearImageCacheMap();
    }

    public static final String FILE_EXT_CSV = ".csv";
    public static final String FILE_EXT_XML = ".xml";
    public static String FILE_EXT_SERIALIZED_DATA = ".mwsd";
    public static String FILE_EXT_JSON_DATA = ".json";

    public static final List<String> PROJECT_FILE_EXTENSIONS = new ArrayList<>();

    public static boolean USE_SYSTEM_FILE_CHOOSER = false;
    public static String FILE_CHOOSER_TITLE_DEFAULT = "Choose file";

    public static final int REQUEST_CODE_READ  = 42;
    public static final int REQUEST_CODE_WRITE = 43;
    public static final int REQUEST_CODE_CHOOSE_DIR = 451;
    public static final int REQUEST_CODE_EXT_STORAGE_PERMISSIONS = 928;
    public static final int REQUEST_EXPORT_CSV = 102;
    public static final int REQUEST_EXPORT_SERIALIZED = 1995;
    public static final int REQUEST_EXPORT_JSON = 1991;
    public static final int REQUEST_EXPORT_XML = 92891;

    // Text filter to disallow whitespace
    public static InputFilter WHITESPACE_FILTER = new InputFilter() {
        // Reference: https://stackoverflow.com/a/33993117/7891239
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dStart, int dEnd) {
            boolean edited = false;
            StringBuilder filtered = new StringBuilder();
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    filtered.append("_");
                    edited = true;
                } else {
                    filtered.append(source.charAt(i));
                }
            }
            return edited ? filtered.toString() : null;
        }};

    /**
     * Saves a serializable object using the default {@link Context}.MODE_PRIVATE flag.
     *
     *  <p>
     *      Source: <a href="https://stackoverflow.com/a/33896724/7891239">Stack Overflow</a> post by
     *       <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
     *  </p>
     *
     * @param objectToSave The object to save.
     * @param fileName     The name of the file.
     * @param <T>          The type of the object.
     *
     * @return true if the file was successfully saved; else false.
     *
     * @see   mw.molarwear.util.FileUtil#saveSerializable(Serializable, String, boolean, int)
     */
    public static <T extends Serializable> boolean saveSerializable(T objectToSave, String fileName) {
        return saveSerializable(objectToSave, fileName, true, Context.MODE_PRIVATE);
    }

    /**
     * Saves a serializable object.
     *
     *  <p>
     *      Source: <a href="https://stackoverflow.com/a/33896724/7891239">Stack Overflow</a> post by
     *       <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
     *  </p>
     *
     * @param objectToSave The object to save.
     * @param fileName     The name of the file.
     * @param flags        The output write flags; can be 0 or any combination of the following:
     *                     <ul>
     *                     <li>{@link Context}.MODE_PRIVATE</li>
     *                     <li>{@link Context}.MODE_WORLD_READABLE  (Deprecated)</li>
     *                     <li>{@link Context}.MODE_WORLD_WRITEABLE (Deprecated)</li>
     *                     <li>{@link Context}.MODE_APPEND</li>
     *                     </ul>
     * @param <T>          The type of the object.
     *
     * @return true if the file was successfully saved; else false.
     *
     * @see   mw.molarwear.util.FileUtil#saveSerializable(Serializable, String)
     */
    public static <T extends Serializable> boolean saveSerializable(T objectToSave, String fileName, boolean internal, int flags) {
        try {
            FileOutputStream fileOutputStream = internal ? AppUtil.CONTEXT.openFileOutput(fileName, flags) : new FileOutputStream(fileName);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(objectToSave);

            objectOutputStream.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static <T extends Serializable> boolean saveSerializableEx(T objectToSave, String fileName) {
        return saveSerializable(objectToSave, fileName, false, Context.MODE_PRIVATE);
    }

    public static <T> boolean saveJson(@NonNull T objectToSave, @NonNull String fileName) {
        return saveJson(objectToSave, fileName, false);
    }

    public static <T> boolean saveJson(@NonNull T objectToSave, @NonNull String fileName, boolean internal) {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            FileUtil.writeText(fileName, jsonMapper.writeValueAsString(objectToSave), false, internal);
        } catch (JsonGenerationException | JsonMappingException e) {
            return false;
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static <T> T readJson(@NonNull String fileName, @NonNull Class<T> objectType) {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            return jsonMapper.readValue(FileUtil.readText(fileName), objectType);
            //return jsonMapper.readValue(new File(fileName), objectType);
        } catch (JsonGenerationException | JsonMappingException e) {
            return null;
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Loads a serializable object from a file in internal storage.
     *
     *  <p>
     *      Source: <a href="https://stackoverflow.com/a/33896724/7891239">Stack Overflow</a> post by
     *       <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
     *  </p>
     *
     * @param fileName The filename.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    @SuppressWarnings("unchecked")
    public static<T extends Serializable> T readInternalSerializable(String fileName) {
        T objectToReturn = null;

        try {
            FileInputStream fileInputStream = AppUtil.CONTEXT.openFileInput(fileName);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectToReturn = (T) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return objectToReturn;
    }

    /**
     * Loads a serializable object from a file in external storage.
     *
     *  <p>
     *      Source: <a href="https://stackoverflow.com/a/33896724/7891239">Stack Overflow</a> post by
     *       <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
     *  </p>
     *
     * @param fileName The filename.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    @SuppressWarnings("unchecked")
    public static<T extends Serializable> T readExternalSerializable(String fileName) {
        T objectToReturn = null;

        try {
            FileInputStream fileInputStream = new FileInputStream (new File(fileName));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            objectToReturn = (T) objectInputStream.readObject();

            objectInputStream.close();
            fileInputStream.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return objectToReturn;
    }

    public static boolean writeText(@NonNull String fileName, @NonNull String text, boolean append, boolean internal) {
        try {
            FileOutputStream fileOutputStream = internal ?
                MolWearApp.getContext().openFileOutput(fileName, append ? Context.MODE_APPEND : Context.MODE_PRIVATE)
                : new FileOutputStream(fileName, append);
            PrintWriter writer = new PrintWriter(fileOutputStream);

            writer.println(text);

            writer.flush();
            writer.close();
            fileOutputStream.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Nullable
    public static String readText(@NonNull String fileName) {
        return readText(fileName, false);
    }

    @Nullable
    public static String readText(@NonNull String fileName, boolean internal) {
        StringBuilder text = new StringBuilder("");
        try {
            BufferedReader reader;
            if (!internal) {
                reader = new BufferedReader(new FileReader(fileName));
            } else {
                reader = new BufferedReader(new InputStreamReader(MolWearApp.getContext().openFileInput(fileName)));
            }
            String line = reader.readLine();
            while (line != null) {
                if (text.length() != 0) {
                    text.append('\n');
                }
                text.append(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            Log.e("FileUtil.readText", e.getMessage());
            return null;
        }
        return text.toString();
    }

    /**
     * Deletes the specified private file.
     *
     *  <p>
     *      Source: <a href="https://stackoverflow.com/a/33896724/7891239">Stack Overflow</a> post by
     *       <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
     *  </p>
     *
     * @param filename The name of the private file.
     *
     * @return true if the file was successfully deleted; else false.
     *
     * @see   Context#deleteFile(String)
     */
    public static boolean deletePrivate(String filename) {
        return AppUtil.CONTEXT.deleteFile(filename);
    }

    /**
     * Searches the given directory and (optionally) all subdirectories for files. If the optional
     *  file extension filter is provided, this function will only search for files with the given
     *  extension.
     *
     *  <p>
     *      Source: <a href="https://stackoverflow.com/a/9531063/7891239">Stack Overflow</a> post by
     *       <a href="https://stackoverflow.com/users/1226354/vyacheslav-shylkin">Vyacheslav Shylkin</a>
     *  </p>
     *
     * @param dir           the root folder to be searched.
     * @param searchSubDirs denotes whether or not to search subfolders. If false, only files located
     *                       immediately in the specified folder will be returned.
     * @param fileExtFilter optional file extension filter; if the filter string is not null or empty,
     *                       search results will contain only files ending in this filter string.
     *
     * @return list of {@link File} objects containing all files that match the specifications
     *          provided in the function parameters. If the specified file doesn't exist or is not a
     *          directory, an empty list is returned.
     *
     * @see File
     */
    public static ArrayList<File> getFilesInDir(File dir, boolean searchSubDirs, String fileExtFilter) {
        ArrayList<File> files = new ArrayList<>();
        Queue<File> subDirFiles = new LinkedList<>();
        if (dir != null && dir.exists() && dir.isDirectory()) {
            while (!subDirFiles.isEmpty()) {
                File file = subDirFiles.remove();
                if (file.isDirectory()) {
                    if (searchSubDirs) {
                        subDirFiles.addAll(Arrays.asList(file.listFiles()));
                    }
                } else if (fileExtFilter == null || fileExtFilter.isEmpty() || file.getName().endsWith(fileExtFilter)) {
                    files.add(file);
                }
            }
        }
        return files;
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=true and fileExtFilter=null.
     *
     * @see mw.molarwear.util.FileUtil#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInDir(File dir) {
        return getFilesInDir(dir, true, null);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with fileExtFilter=null.
     *
     * @see mw.molarwear.util.FileUtil#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInDir(File dir, boolean searchSubDirs) {
        return getFilesInDir(dir, searchSubDirs, null);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=true.
     *
     * @see mw.molarwear.util.FileUtil#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInDir(File dir, String fileExtFilter) {
        return getFilesInDir(dir, true, fileExtFilter);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=false and fileExtFilter=null.
     *
     * @see mw.molarwear.util.FileUtil#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInFolder(File dir) {
        return getFilesInDir(dir, false, null);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=false.
     *
     * @see mw.molarwear.util.FileUtil#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInFolder(File dir, String fileExtFilter) {
        return getFilesInDir(dir, false, fileExtFilter);
    }

    /**
     * Obtains the fully resolved internal storage path for a given file.
     *
     * @param  fileName the file name of (or relative path to) the file.
     *
     * @return the fully resolved internal storage path for the given file (or the path to the
     *          internal storage folder if fileName is empty or null)
     */
    public static String getInternalPath(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            return AppUtil.CONTEXT.getFilesDir().toString() + separator;
        }
        return AppUtil.CONTEXT.getFilesDir().toString() + separator + fileName;
    }

    /**
     * Returns the absolute file path of the file specified by the given URI.
     *
     * @param uri the URI of the specified file.
     *
     * @return    the resolved file path as a {@link String}, or an empty string if the file path
     *             could not be determined.
     *
     * @see Uri
     */
    public static String getPathFromURI(Uri uri) {
        // Reference: https://gist.github.com/asifmujteba/d89ba9074bc941de1eaa
        if (Build.VERSION.SDK_INT >= 19) {
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            //return Environment.getExternalStorageDirectory() + separator + split[1];
            return split[1];
        }
        // @TODO: Add support for pre-19 Android builds?
        AppUtil.printSnackBarMsg("Error: Unsupported Android build");
        return "";
    }


    public static void createDirectoryChooser(@NonNull Activity activity, int requestCode) {
        checkExternalStoragePermissions(activity);
        Intent i = new Intent(activity.getApplicationContext(), FilePickerActivity.class);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
        i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
        i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_DIR);
        i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
        activity.startActivityForResult(i, requestCode);
    }

    public static void createDirectoryChooser() {
        createDirectoryChooser(AppUtil.CONTEXT, REQUEST_CODE_CHOOSE_DIR);
    }

    public static void createDirectoryChooser(int requestCode) {
        createDirectoryChooser(AppUtil.CONTEXT, requestCode);
    }

    public static void createDirectoryChooser(@NonNull Activity activity) {
        createDirectoryChooser(activity, REQUEST_CODE_CHOOSE_DIR);
    }

    public static void createFileChooser(@NonNull Activity activity, int requestCode) {
        checkExternalStoragePermissions(activity);
        if (USE_SYSTEM_FILE_CHOOSER) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType("*/*");
            activity.startActivityForResult(intent, FileUtil.REQUEST_CODE_READ);
        } else {
            Intent i = new Intent(activity.getApplicationContext(), FilePickerActivity.class);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
            i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, true);
            i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);
            i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());
            activity.startActivityForResult(i, requestCode);
        }
    }

    public static void createFileChooser() {
        createFileChooser(AppUtil.CONTEXT, REQUEST_CODE_READ);
    }

    public static void createFileChooser(@NonNull Activity activity) {
        createFileChooser(activity, REQUEST_CODE_READ);
    }

    public static void createFileChooser(int requestCode) {
        createFileChooser(AppUtil.CONTEXT, requestCode);
    }

    public static void checkExternalStoragePermissions(@NonNull Activity activity) {
        // Reference: https://stackoverflow.com/a/32175771/7891239
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(activity)) {
                activity.requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE_EXT_STORAGE_PERMISSIONS);
            }
        } else if (Build.VERSION.SDK_INT >= 16) {
            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity,
                    new String[]{ Manifest.permission.WRITE_EXTERNAL_STORAGE },
                    REQUEST_CODE_EXT_STORAGE_PERMISSIONS);
            }
        } else {
            AppUtil.printToast(activity, activity.getString(R.string.err_unsupported_android_version));
        }
    }

    public static void checkExternalStoragePermissions() {
        checkExternalStoragePermissions(AppUtil.CONTEXT);
    }


    public static Drawable resizeImage(@NonNull Context context, @DrawableRes int imageResource, @IntRange(from=0) int newWidthPx) {
        // Source: https://stackoverflow.com/a/35222639/7891239

        BitmapDrawable bd = (BitmapDrawable) context.getResources().getDrawable(imageResource);
        double imageHeight = bd.getBitmap().getHeight();
        double imageWidth = bd.getBitmap().getWidth();

        double ratio = newWidthPx / imageWidth;
        int newImageHeight = (int) (imageHeight * ratio);

        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), imageResource);
        return new BitmapDrawable(context.getResources(), getResizedBitmap(bMap, newImageHeight, newWidthPx));
    }

    public static Bitmap getResizedBitmap(Bitmap bm, int newHeightPx, int newWidthPx) {
        // Source: https://stackoverflow.com/a/35222639/7891239

        int width = bm.getWidth();
        int height = bm.getHeight();

        float scaleWidth = ((float) newWidthPx) / width;
        float scaleHeight = ((float) newHeightPx) / height;

        // Create a matrix for the manipulation
        Matrix matrix = new Matrix();

        // Resize the bit map
        matrix.postScale(scaleWidth, scaleHeight);

        // Recreate the new Bitmap
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);

        return resizedBitmap;
    }
}
