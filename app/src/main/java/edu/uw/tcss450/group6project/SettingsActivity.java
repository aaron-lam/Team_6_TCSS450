package edu.uw.tcss450.group6project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

/** Activity for the settings page
 *
 */
public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        sp = getSharedPreferences(getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);
        theme.applyStyle(sp.getInt("theme",0), true);
        return theme;
    }
}