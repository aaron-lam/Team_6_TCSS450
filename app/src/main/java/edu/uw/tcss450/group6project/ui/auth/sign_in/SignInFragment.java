package edu.uw.tcss450.group6project.ui.auth.sign_in;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6project.AuthActivity;
import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group6project.model.PushyTokenViewModel;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.auth.EmailVerificationDialog;
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

    boolean mFirstCall; // This tells the class whether the "Sign In" button has been clicked yet.

    private String m_Text = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstCall = true;
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
        mBinding.testHome.setOnClickListener(this::handleHome);

        mBinding.buttonSignInRegister.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToRegisterFragment2());
        });

        mBinding.buttonSignInSubmit.setOnClickListener(button -> {
            Validator validator = new Validator(getActivity(), mBinding.fieldSignInEmail, mBinding.fieldSignInPassword);

            if (validator.validateAll()) {
                verifyAuthWithServer();
            }
        });

        mBinding.buttonSignInForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                mBuilder.setTitle(R.string.forgot_password_title);
                View mView = getLayoutInflater().inflate(R.layout.dialog_forgot_password,null);

                // Grabbing references to all the elements in the fragment
                EditText mEmail = (EditText) mView.findViewById(R.id.field_forgot_password_email);
                Button mSubmit = (Button) mView.findViewById(R.id.button_forgot_password_submit);
                Button mCancel = (Button) mView.findViewById(R.id.button_forgot_password_cancel);

                // Assembling the dialog?
                mBuilder.setView(mView);
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();

                // Cancel button functionality
                mCancel.setOnClickListener(button -> mDialog.cancel());

                // Submit button functionality
                mSubmit.setOnClickListener(button -> {
                    Validator validator = new Validator(getActivity(), mEmail);

                    if (validator.validateAll()) {
                        // TODO
                        // At this point form validation is complete
                    }
                });
            }
        });

        if (mFirstCall) {
            mSignInModel.addResponseObserver(
                    getViewLifecycleOwner(),
                    this::observeResponse);
            mFirstCall = false;
        }

        mPushyTokenViewModel.addTokenObserver(getViewLifecycleOwner(), token ->
                mBinding.buttonSignInSubmit.setEnabled(!token.isEmpty()));
        mPushyTokenViewModel.addResponseObserver(
                getViewLifecycleOwner(),
                this::observePushyPutResponse);
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
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                mBinding.fieldSignInEmail.getText().toString(),
                mBinding.fieldSignInPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
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
                    if ((int) response.get("code") == 400) {
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
                    mUserViewModel = new ViewModelProvider(getActivity(),
                            new UserInfoViewModel.UserInfoViewModelFactory(
                                    mBinding.fieldSignInEmail.getText().toString(),
                                    response.getString("token")
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
                        mUserViewModel.getJWT()
                );
            }
        }
    }

}