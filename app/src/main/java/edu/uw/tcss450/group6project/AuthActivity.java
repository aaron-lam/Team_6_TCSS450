package edu.uw.tcss450.group6project;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group6project.model.PushyTokenViewModel;
import edu.uw.tcss450.group6project.ui.auth.sign_in.SignInFragmentDirections;
import me.pushy.sdk.Pushy;

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

        //If it is not already running, start the Pushy listening service
        Pushy.listen(this);
        initiatePushyTokenRequest();

    }

    private void initiatePushyTokenRequest() {
        new ViewModelProvider(this).get(PushyTokenViewModel.class).retrieveToken();
    }

}