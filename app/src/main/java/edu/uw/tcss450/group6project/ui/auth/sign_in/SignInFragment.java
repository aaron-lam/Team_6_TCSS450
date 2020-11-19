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

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group6project.ui.auth.EmailVerificationDialog;
import edu.uw.tcss450.group6project.ui.auth.forgot_password.ForgotPasswordDialog;
import edu.uw.tcss450.group6project.utils.Validator;

/** This fragment represents the sign in page.
 *
 * @author Robert Mangrum & Chase Alder
 */
public class SignInFragment extends Fragment {

    private SharedPreferences sp;
    private FragmentSignInBinding mBinding;
    private SignInViewModel mSignInModel;
    boolean mFirstSignInPress; // This tells the class whether the "Sign In" button has been clicked yet.

    private String m_Text = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstSignInPress = true;
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
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
        mBinding.testHome.setOnClickListener(this::handleHome);

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
    }

    /** This is a method used for testing. It skips directly to the home page without needing to sign in.
     *
     * @param view is the current view
     */
    private void handleHome(View view) {
        NavDirections action = SignInFragmentDirections.actionSignInFragmentToMainActivity("testBypass","");
        Navigation.findNavController(view).navigate(action);
    }

    /** This is called when a user's sign in attempt has been successfully authenticated.
     *
     * @param email The user's email
     * @param jwt The web authentication token
     */
    public void successfulSignIn(final String email, final String jwt) {
        Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity(email,jwt));
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
                    successfulSignIn(
                            mBinding.fieldSignInEmail.getText().toString(),
                            response.getString("token")
                    );
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}