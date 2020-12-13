package edu.uw.tcss450.group6project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import edu.uw.tcss450.group6project.model.PushyTokenViewModel;
import me.pushy.sdk.Pushy;

/**
 * Activity for all authentication functionality.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class AuthActivity extends AppCompatActivity {

    SharedPreferences sp;
    int curTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        Pushy.listen(this);
        initiatePushyTokenRequest();
    }

    /**
     * Retrieves the Push Token for the device.
     */
    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }


    @Override
    protected void onRestart() {
        super.onRestart();

        sp = getSharedPreferences(getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);
        int spTheme = sp.getInt("theme",0);

        if (spTheme != curTheme) {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        sp = getSharedPreferences(getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);
        curTheme = sp.getInt("theme",0);
        theme.applyStyle(curTheme, true);
        return theme;
    }
}