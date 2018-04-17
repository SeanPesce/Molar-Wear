package mw.molarwear.gui.activity.interfaces;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import mw.molarwear.data.classes.dental.molar.WearImageCacheMap;
import mw.molarwear.data.handlers.ProjectHandler;
import mw.molarwear.util.AppUtil;

public abstract class DataCachingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtil.CONTEXT = this;

        if (!ProjectHandler.initialized()) {
            AppUtil.initializeRuntimeSettings();
            AppUtil.loadPreferences(this);
            ProjectHandler.loadProjects();
        }

        if (!WearImageCacheMap.isInitialized()){
            WearImageCacheMap.initialize();
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        AppUtil.CONTEXT = this;
        if (!ProjectHandler.initialized()) {
            AppUtil.initializeRuntimeSettings();
            AppUtil.loadPreferences(this);
            ProjectHandler.loadProjects();
        }
    }


}
