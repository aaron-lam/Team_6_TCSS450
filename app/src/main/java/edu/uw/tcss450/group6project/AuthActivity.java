package edu.uw.tcss450.group6project;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.auth.sign_in.SignInFragmentDirections;

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
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        sp = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        int spTheme = sp.getInt("theme",0);

        if (spTheme != curTheme) {
            finish();
            startActivity(getIntent());
        }
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        sp = getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        curTheme = sp.getInt("theme",0);
        theme.applyStyle(curTheme, true);
        return theme;
    }
}