package mw.molarwear.data.classes.dental.molar;

import android.content.Context;
import android.graphics.drawable.Drawable;

import java.util.HashMap;

import mw.molarwear.MolWearApp;
import mw.molarwear.R;
import mw.molarwear.util.AppUtil;
import mw.molarwear.util.FileUtil;

/**
 * @author Sean Pesce
 *
 * @see    FileUtil.Cache
 */
public class WearImageCacheMap extends HashMap<Integer, Drawable> {

    private static boolean _INITIALIZED = false;

    public static final int WEAR_IMAGE_DIMENSION_DP = 74;

    public WearImageCacheMap() {}


    public static boolean isInitialized() { return _INITIALIZED; }

    public static void initialize() {
        if (!_INITIALIZED) {
            if (MolWearApp.getContext() == null) {
                return;
            }
            _INITIALIZED = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final int pxSize = AppUtil.dpToPixels(WEAR_IMAGE_DIMENSION_DP);
                    final Context context = MolWearApp.getContext();
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_0, FileUtil.resizeImage(context, R.drawable.q1_0, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_1, FileUtil.resizeImage(context, R.drawable.q1_1, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_2, FileUtil.resizeImage(context, R.drawable.q1_2, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_3, FileUtil.resizeImage(context, R.drawable.q1_3, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_4, FileUtil.resizeImage(context, R.drawable.q1_4, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_5, FileUtil.resizeImage(context, R.drawable.q1_5, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_6, FileUtil.resizeImage(context, R.drawable.q1_6, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_7, FileUtil.resizeImage(context, R.drawable.q1_7, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_8, FileUtil.resizeImage(context, R.drawable.q1_8, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q1_unknown, FileUtil.resizeImage(context, R.drawable.q1_unknown, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_0, FileUtil.resizeImage(context, R.drawable.q2_0, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_1, FileUtil.resizeImage(context, R.drawable.q2_1, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_2, FileUtil.resizeImage(context, R.drawable.q2_2, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_3, FileUtil.resizeImage(context, R.drawable.q2_3, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_4, FileUtil.resizeImage(context, R.drawable.q2_4, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_5, FileUtil.resizeImage(context, R.drawable.q2_5, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_6, FileUtil.resizeImage(context, R.drawable.q2_6, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_7, FileUtil.resizeImage(context, R.drawable.q2_7, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_8, FileUtil.resizeImage(context, R.drawable.q2_8, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q2_unknown, FileUtil.resizeImage(context, R.drawable.q2_unknown, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_0, FileUtil.resizeImage(context, R.drawable.q3_0, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_1, FileUtil.resizeImage(context, R.drawable.q3_1, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_2, FileUtil.resizeImage(context, R.drawable.q3_2, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_3, FileUtil.resizeImage(context, R.drawable.q3_3, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_4, FileUtil.resizeImage(context, R.drawable.q3_4, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_5, FileUtil.resizeImage(context, R.drawable.q3_5, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_6, FileUtil.resizeImage(context, R.drawable.q3_6, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_7, FileUtil.resizeImage(context, R.drawable.q3_7, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_8, FileUtil.resizeImage(context, R.drawable.q3_8, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q3_unknown, FileUtil.resizeImage(context, R.drawable.q3_unknown, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_0, FileUtil.resizeImage(context, R.drawable.q4_0, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_1, FileUtil.resizeImage(context, R.drawable.q4_1, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_2, FileUtil.resizeImage(context, R.drawable.q4_2, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_3, FileUtil.resizeImage(context, R.drawable.q4_3, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_4, FileUtil.resizeImage(context, R.drawable.q4_4, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_5, FileUtil.resizeImage(context, R.drawable.q4_5, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_6, FileUtil.resizeImage(context, R.drawable.q4_6, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_7, FileUtil.resizeImage(context, R.drawable.q4_7, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_8, FileUtil.resizeImage(context, R.drawable.q4_8, pxSize));
                    FileUtil.Cache.MOLAR_WEAR_IMAGES.put(R.drawable.q4_unknown, FileUtil.resizeImage(context, R.drawable.q4_unknown, pxSize));
                }
            }).start();
        }
    }

}
