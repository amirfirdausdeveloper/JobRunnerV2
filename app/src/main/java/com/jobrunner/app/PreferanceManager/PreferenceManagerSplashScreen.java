package com.jobrunner.app.PreferanceManager;


import android.content.Context;
import android.content.SharedPreferences;


public class PreferenceManagerSplashScreen {
    private static final String PREF_NAME = "welcome_screen";
    private static final String IS_FIRST_TIME_LAUNCH = "IsFirstTimeLaunch";
    SharedPreferences pref;
    SharedPreferences.Editor edit;
    Context context;
    //shared pref mode
    int PRIVATE_MODE = 0;

    public PreferenceManagerSplashScreen(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        edit = pref.edit();
    }

    public void setIsFirstTimeLaunch(boolean isFirstTime) {
        edit.putBoolean(IS_FIRST_TIME_LAUNCH, isFirstTime);
        edit.commit();
    }

    public boolean isFirstTimeLaunching() {
        return pref.getBoolean(IS_FIRST_TIME_LAUNCH, true);
    }
}
