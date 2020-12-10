package edu.uw.tcss450.group6project.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.group6project.ui.auth.EmailVerificationDialog;
import edu.uw.tcss450.group6project.utils.Validator;

/** This fragment represents the new user registration page.
 *
 * @author chasealder
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding mBinding;
    private RegisterViewModel mRegisterModel;
    boolean mFirstCall; // This tells the class whether the "Register" button has been clicked yet.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstCall = true;
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentRegisterBinding.inflate(inflater);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonRegisterSubmit.setOnClickListener(button -> {

            Validator validator = new Validator(getActivity(),
                    mBinding.fieldRegisterFirstName,
                    mBinding.fieldRegisterLastName,
                    mBinding.fieldRegisterUsername,
                    mBinding.fieldRegisterEmail,
                    mBinding.fieldRegisterPassword,
                    mBinding.fieldRegisterRetypePassword);

            if (validator.validateAll()) {
                verifyAuthWithServer();
            }

            // For some reason, if this gets called more than once (aka the user attempts registration
            // is rejected, and must try again) then the program crashes. This boolean is here so it
            // only runs once.
            if (mFirstCall) {
                mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                        this::observeResponse);
                mFirstCall = false;
            }
        });
    }

    /** This method navigates to the sign in page after a successful registration.
     *
     */
    private void successfulRegistration() {
        Navigation.findNavController(getView()).navigate(RegisterFragmentDirections
                .actionRegisterFragmentToSignInFragment());
    }

    /** This method makes a popup reminding the user to verify their email.
     *
     */
    private void verificationPopup() {

        EmailVerificationDialog dialog = new EmailVerificationDialog();
        dialog.show(getActivity().getSupportFragmentManager(),"Email Verification Reminder");
    }

    /** This sends a message to the web service with the current information in the registration fields.
     *
     */
    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                mBinding.fieldRegisterFirstName.getText().toString(),
                mBinding.fieldRegisterLastName.getText().toString(),
                mBinding.fieldRegisterUsername.getText().toString(),
                mBinding.fieldRegisterEmail.getText().toString(),
                mBinding.fieldRegisterPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /** Handles the response from the web service after attempting registration.
     *
     * @param response The response from the web service.
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if (response.getJSONObject("data").getString("message").equals("Username exists")) {
                        mBinding.fieldRegisterUsername.setError(
                                "Error Authenticating: " + getString(R.string.register_username_exists_error));
                    } else {
                        mBinding.fieldRegisterEmail.setError(
                                "Error Authenticating: " + getString(R.string.register_email_exists_error));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                verificationPopup();
                successfulRegistration();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}