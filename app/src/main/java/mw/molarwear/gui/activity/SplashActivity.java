package mw.molarwear.gui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.util.AppUtility;

/**
 * Splash screen shown when the app is loading.
 *
 * <p>
 * This Activity class is based on a
 *  <a href="https://www.bignerdranch.com/blog/splash-screens-the-right-way/">blog post</a> by
 *  <a href="https://github.com/cstew">Chris Stewart</a>.
 * </p>
 *
 * @author <a href="https://github.com/cstew">Chris Stewart</a>
 * @author Sean Pesce
 *
 * @see    android.app.Activity
 * @see    AppCompatActivity
 */
public class SplashActivity extends AppCompatActivity {

    protected static int _WAIT_TIME = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtility.CONTEXT = this;
        AppUtility.initializeRuntimeSettings();
        ProjectHandler.loadProjects();

        try {
            Thread.sleep(_WAIT_TIME);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(getApplicationContext(), MolWearMainActivity.class);
        startActivity(intent);
        finish();
    }


    public static int  WAIT_TIME    ()             { return _WAIT_TIME;                                    }
    public static void SET_WAIT_TIME(int waitTime) { _WAIT_TIME = (waitTime >= 0) ? waitTime : _WAIT_TIME; }
}
