package edu.uw.tcss450.group6project.ui.auth.sign_in;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.auth0.android.jwt.JWT;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group6project.model.PushyTokenViewModel;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.auth.EmailVerificationDialog;
import edu.uw.tcss450.group6project.ui.auth.forgot_password.ForgotPasswordDialog;
import edu.uw.tcss450.group6project.utils.Validator;

/** This fragment represents the sign in page.
 *
 * @author Robert Mangrum & Chase Alder
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding mBinding;
    private SignInViewModel mSignInModel;
    private PushyTokenViewModel mPushyTokenViewModel;
    private UserInfoViewModel mUserViewModel;
    boolean mFirstSignInPress; // This tells the class whether the "Sign In" button has been clicked yet.

    private String m_Text = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstSignInPress = true;
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);

        mPushyTokenViewModel = new ViewModelProvider(getActivity())
                .get(PushyTokenViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSignInBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonSignInRegister.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToRegisterFragment2());
        });

        mBinding.buttonSignInSubmit.setOnClickListener(button -> {
            Validator validator = new Validator(getActivity(), mBinding.fieldSignInEmail, mBinding.fieldSignInPassword);

            if (validator.validateAll()) {
                verifySignInWithServer();
            }
        });

        mBinding.buttonSignInForgotPassword.setOnClickListener(new ForgotPasswordDialog(this));

        // This makes sure that the response observer doesn't get added every subsequent call
        if (mFirstSignInPress) {
            mSignInModel.addResponseObserver(
                    getViewLifecycleOwner(),
                    this::observeResponse);
            mFirstSignInPress = false;
        }

        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                mBinding.buttonSignInSubmit.setEnabled(!token.isEmpty()));
        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences prefs =
                getActivity().getSharedPreferences(
                        getString(R.string.keys_shared_prefs),
                        Context.MODE_PRIVATE);
        if (prefs.contains(getString(R.string.keys_prefs_jwt))) {
            String token = prefs.getString(getString(R.string.keys_prefs_jwt), "");
            JWT jwt = new JWT(token);
            // Check to see if the web token is still valid or not. To make a JWT expire after a
            // longer or shorter time period, change the expiration time when the JWT is
            // created on the web service.
            if(!jwt.isExpired(0)) {
                String email = jwt.getClaim("email").asString();
                String username = prefs.getString("username","ERROR ON SHARED PREFERENCES");
                successfulSignIn(email, token, username);
                return;
            }
        }
    }

    /**
     * Helper to abstract the request to send the pushy token to the web service
     */
    private void sendPushyToken() {
        mPushyTokenViewModel.sendTokenToWebservice(mUserViewModel.getJWT());
    }

    /**
     * An observer on the HTTP Response from the web server. This observer should be
     * attached to PushyTokenViewModel.
     *
     * @param response the Response from the server
     */
    private void observePushyPutResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                //this error cannot be fixed by the user changing credentials...
                mBinding.fieldSignInEmail.setError(
                        "Error Authenticating on Push Token. Please contact support");
            } else {
                successfulSignIn(
                        mBinding.fieldSignInEmail.getText().toString(),
                        mUserViewModel.getJWT(),
                        mUserViewModel.getUsername()
                );
            }
        }
    }


    /** This is called when a user's sign in attempt has been successfully authenticated.
     *
     * @param email The user's email
     * @param jwt The web authentication token
     */
    public void successfulSignIn(final String email, final String jwt, final String username) {

        SharedPreferences prefs =
                getActivity().getSharedPreferences(getString(R.string.keys_shared_prefs), Context.MODE_PRIVATE);

        //Store the credentials in SharedPrefs
        prefs.edit().putString(getString(R.string.keys_prefs_jwt), jwt).apply();
        prefs.edit().putString("username",username).apply();

        Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity(email, jwt, username));
        getActivity().finish();
    }

    /** This method sends the users credentials to the web service for authentication.
     *
     */
    private void verifySignInWithServer() {
        mSignInModel.connectSignIn(
                mBinding.fieldSignInEmail.getText().toString(),
                mBinding.fieldSignInPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connectSignIn().
    }

    /** This makes a popup reminding the user to verify their email.
     *
     */
    private void verificationPopup() {
        EmailVerificationDialog dialog = new EmailVerificationDialog();
        dialog.show(getActivity().getSupportFragmentManager(),"Email Verification Reminder");
    }

    /** Checks the response from the web service after attempting to sign in.
     * If the user's email is not yet verified, it will put up a popup.
     *
     * @param response the JSON object response from the server
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if (response.getJSONObject("data").getString("message").equals("Email is not verified yet")) {
                        verificationPopup();
                    }
                    mBinding.fieldSignInEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    mBinding.fieldSignInEmail.getText().toString(),
                                    response.getString("token"),
                                    response.getString("username")
                            )).get(UserInfoViewModel.class);
                    sendPushyToken();
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}