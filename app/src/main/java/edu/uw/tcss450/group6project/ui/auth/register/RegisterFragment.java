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

import edu.uw.tcss450.group6project.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.group6project.ui.auth.EmailVerificationDialog;
import edu.uw.tcss450.group6project.utils.RegisterValidator;

/** This fragment represents the new user registration page.
 *
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel mRegisterModel;
    boolean firstCall; // This tells the class whether the "Register" button has been clicked yet.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstCall = true;
        mRegisterModel = new ViewModelProvider(getActivity())
                .get(RegisterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRegisterBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonRegisterSubmit.setOnClickListener(v -> {

            RegisterValidator registerValidator = new RegisterValidator(binding);

            if (registerValidator.validateAll()) {
                verifyAuthWithServer();
            }

            // For some reason, if this gets called more than once (aka the user attempts registration
            // is rejected, and must try again) then the program crashes. This boolean is here so it
            // only runs once.
            if (firstCall) {
                mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                        this::observeResponse);
                firstCall = false;
            }
        });
    }

    /** This method navigates to the sign in page after a successful registration.
     *
     */
    private void successfulRegistration() {
        Navigation.findNavController(getView()).navigate(RegisterFragmentDirections.actionRegisterFragmentToSignInFragment());
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
                binding.fieldRegisterFirstName.getText().toString(),
                binding.fieldRegisterLastName.getText().toString(),
                binding.fieldRegisterUsername.getText().toString(),
                binding.fieldRegisterEmail.getText().toString(),
                binding.fieldRegisterPassword.getText().toString());
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
                    binding.fieldRegisterEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
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