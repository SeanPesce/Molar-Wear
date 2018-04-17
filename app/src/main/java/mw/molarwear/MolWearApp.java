package mw.molarwear;

import android.app.Application;
import android.content.Context;

public class MolWearApp extends Application {
    // https://stackoverflow.com/a/5114361/7891239

    private static Application sApplication;

    public static Application getApplication() {
        return sApplication;
    }

    public static Context getContext() {
        return getApplication().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sApplication = this;
    }

}
