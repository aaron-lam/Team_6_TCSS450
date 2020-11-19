package edu.uw.tcss450.group6project;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group6project.ui.auth.sign_in.SignInFragmentDirections;

/**
 * Activity for all authentication functionality.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
    }
}