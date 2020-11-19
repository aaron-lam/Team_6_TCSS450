package edu.uw.tcss450.group6project.model;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

/**
 * View Model to keep track of information about the current user using the app.
 * @author Anthony Nguyen
 */
public class UserInfoViewModel extends ViewModel {

    /** The email of the current user. */
    private final String mEmail;
    /** The JWT auth for the current user. */
    private final String mJwt;

    /**
     * Private constructor for the factory method.
     * @param email email of the user
     * @param jwt jwt of the user
     */
    private UserInfoViewModel(final String email,  final String jwt) {
        mEmail = email;
        mJwt = jwt;
    }

    /**
     * Returns the email of the user
     * @return user's email
     */
    public String getEmail() {
        return mEmail;
    }

    /**
     * Returns JSON-Web-Token for the app
     * @return Current JWT
     */
    public String getJWT() {
        return mJwt;
    }

    public static class UserInfoViewModelFactory implements ViewModelProvider.Factory {

        private final String email;
        private final String jwt;

        public UserInfoViewModelFactory(String email, String jwt) {
            this.email = email;
            this.jwt = jwt;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if (modelClass == UserInfoViewModel.class) {
                return (T) new UserInfoViewModel(email, jwt);
            }
            throw new IllegalArgumentException(
                    "Argument must be: " + UserInfoViewModel.class);
        }
    }
}
