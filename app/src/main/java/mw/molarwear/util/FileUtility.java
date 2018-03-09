package mw.molarwear.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.text.InputFilter;
import android.text.Spanned;

import net.rdrei.android.dirchooser.DirectoryChooserActivity;
import net.rdrei.android.dirchooser.DirectoryChooserConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import mw.molarwear.R;

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

public class FileUtility {

    public static final String FILE_EXT_CSV = ".csv";

    public static String FILE_EXT_SERIALIZED_DATA = ".mwsd";
    public static String FILE_EXT_JSON_DATA = ".json";

    // @TODO: JSON/CSV support

    public static final int REQUEST_CODE_READ  = 42;
    public static final int REQUEST_CODE_WRITE = 43;
    public static final int REQUEST_CODE_CHOOSE_DIR = 451;
    public static final int REQUEST_CODE_EXT_STORAGE_PERMISSIONS = 928;

    // Text filter to disallow whitespace
    public static InputFilter WHITESPACE_FILTER = new InputFilter() {
        // Reference: https://stackoverflow.com/a/33993117/7891239
        @Override
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dStart, int dEnd) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
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
     * @see   mw.molarwear.util.FileUtility#saveSerializable(Serializable, String, boolean, int)
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
     * @see   mw.molarwear.util.FileUtility#saveSerializable(Serializable, String)
     */
    public static <T extends Serializable> boolean saveSerializable(T objectToSave, String fileName, boolean internal, int flags) {
        try {
            FileOutputStream fileOutputStream = internal ? AppUtility.CONTEXT.openFileOutput(fileName, flags) : new FileOutputStream(fileName);
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
    public static<T extends Serializable> T readInternalSerializable(String fileName) {
        T objectToReturn = null;

        try {
            FileInputStream fileInputStream = AppUtility.CONTEXT.openFileInput(fileName);
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
                AppUtility.CONTEXT.openFileOutput(fileName, (append) ? Context.MODE_APPEND : Context.MODE_PRIVATE)
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
        return AppUtility.CONTEXT.deleteFile(filename);
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
     * @see mw.molarwear.util.FileUtility#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInDir(File dir) {
        return getFilesInDir(dir, true, null);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with fileExtFilter=null.
     *
     * @see mw.molarwear.util.FileUtility#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInDir(File dir, boolean searchSubDirs) {
        return getFilesInDir(dir, searchSubDirs, null);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=true.
     *
     * @see mw.molarwear.util.FileUtility#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInDir(File dir, String fileExtFilter) {
        return getFilesInDir(dir, true, fileExtFilter);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=false and fileExtFilter=null.
     *
     * @see mw.molarwear.util.FileUtility#getFilesInDir(File, boolean, String)
     */
    public static ArrayList<File> getFilesInFolder(File dir) {
        return getFilesInDir(dir, false, null);
    }

    /**
     * Calls {@link #getFilesInDir(File, boolean, String)} with searchSubDirs=false.
     *
     * @see mw.molarwear.util.FileUtility#getFilesInDir(File, boolean, String)
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
            return AppUtility.CONTEXT.getFilesDir().toString() + separator;
        }
        return AppUtility.CONTEXT.getFilesDir().toString() + separator + fileName;
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
        AppUtility.printSnackBarMsg("Error: Unsupported Android build");
        return "";
    }


    public static void createDirectoryChooser(@NonNull Activity activity, int requestCode, boolean allowReadOnlyDir) {
        checkExternalStoragePermissions(activity);
        final Intent chooseDir = new Intent(activity, DirectoryChooserActivity.class);
        final DirectoryChooserConfig config = DirectoryChooserConfig.builder()
                                                .newDirectoryName(activity.getString(R.string.default_new_folder_name))
                                                .allowReadOnlyDirectory(allowReadOnlyDir)
                                                .allowNewDirectoryNameModification(true)
                                                .build();
        chooseDir.putExtra(DirectoryChooserActivity.EXTRA_CONFIG, config);
        activity.startActivityForResult(chooseDir, requestCode);
    }

    public static void createDirectoryChooser() {
        createDirectoryChooser(AppUtility.CONTEXT, REQUEST_CODE_CHOOSE_DIR, true);
    }

    public static void createDirectoryChooser(int requestCode) {
        createDirectoryChooser(AppUtility.CONTEXT, requestCode, true);
    }

    public static void createDirectoryChooser(@NonNull Activity activity) {
        createDirectoryChooser(activity, REQUEST_CODE_CHOOSE_DIR, true);
    }

    public static void createDirectoryChooser(@NonNull Activity activity, int requestCode) {
        createDirectoryChooser(activity, requestCode, true);
    }

    public static void createDirectoryChooser(@NonNull Activity activity, boolean allowReadOnlyDir) {
        createDirectoryChooser(activity, REQUEST_CODE_CHOOSE_DIR, allowReadOnlyDir);
    }

    public static void createDirectoryChooser(boolean allowReadOnlyDir) {
        createDirectoryChooser(AppUtility.CONTEXT, REQUEST_CODE_CHOOSE_DIR, allowReadOnlyDir);
    }

    public static void createDirectoryChooser(int requestCode, boolean allowReadOnlyDir) {
        createDirectoryChooser(AppUtility.CONTEXT, requestCode, allowReadOnlyDir);
    }

    public static void checkExternalStoragePermissions(@NonNull Activity activity) {
        // Reference: https://stackoverflow.com/a/32175771/7891239
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(activity)) {
                activity.requestPermissions(new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE }, REQUEST_CODE_EXT_STORAGE_PERMISSIONS);
            }
        }
    }

    public static void checkExternalStoragePermissions() {
        checkExternalStoragePermissions(AppUtility.CONTEXT);
    }
}
