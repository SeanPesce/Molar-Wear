package mw.molarwear.util;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Utility class for tasks related to file I/O.
 *
 * <p>
 * <a href="https://stackoverflow.com/a/33896724/7891239">Original source for serialization-related functions by Sandro Machado on Stack Overflow</a>
 * </p>
 *
 * @author <a href="https://stackoverflow.com/users/3482000/sandro-machado">Sandro Machado</a>
 * @author Sean Pesce
 *
 * @see    Serializable
 */

public class FileUtility {
    // @TODO: JSON support

    /**
     * Saves a serializable object using the default {@link Context}.MODE_PRIVATE flag.
     *
     * @param context      The application context.
     * @param objectToSave The object to save.
     * @param fileName     The name of the file.
     * @param <T>          The type of the object.
     *
     * @see   mw.molarwear.util.FileUtility#saveSerializable(Context, Serializable, String, int)
     */
    public static <T extends Serializable> void saveSerializable(Context context, T objectToSave, String fileName) {
        saveSerializable(context, objectToSave, fileName, Context.MODE_PRIVATE);
    }

    /**
     * Saves a serializable object.
     *
     * @param context      The application context.
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
     * @see   mw.molarwear.util.FileUtility#saveSerializable(Context, Serializable, String)
     */
    public static <T extends Serializable> void saveSerializable(Context context, T objectToSave, String fileName, int flags) {
        try {
            FileOutputStream fileOutputStream = context.openFileOutput(fileName, flags);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            objectOutputStream.writeObject(objectToSave);

            objectOutputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads a serializable object.
     *
     * @param context The application context.
     * @param fileName The filename.
     * @param <T> The object type.
     *
     * @return the serializable object.
     */
    public static<T extends Serializable> T readSerializable(Context context, String fileName) {
        T objectToReturn = null;

        try {
            FileInputStream fileInputStream = context.openFileInput(fileName);
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
     * Deletes the given private file associated with the given {@link Context}'s application package.
     *
     * @param context  The application context.
     * @param filename The name of the private file.
     *
     * @see   Context#deleteFile(String)
     */
    public static void deletePrivate(Context context, String filename) {
        context.deleteFile(filename);
    }

}
