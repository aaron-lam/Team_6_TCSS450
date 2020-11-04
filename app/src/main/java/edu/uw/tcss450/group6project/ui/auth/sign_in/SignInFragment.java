package edu.uw.tcss450.group6project.ui.auth.sign_in;

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

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group6project.ui.auth.register.EmailVerificationDialog;
import edu.uw.tcss450.group6project.utils.SignInValidator;

/** This fragment represents the sign in page.
 *
 * @author Robert Mangrum & Chase Alder
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;
    private SignInViewModel mSignInModel;
    boolean firstCall; // This tells the class whether the "Sign In" button has been clicked yet.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstCall = true;
        mSignInModel = new ViewModelProvider(getActivity())
                .get(SignInViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.testHome.setOnClickListener(this::handleHome);

        binding.buttonSigninRegister.setOnClickListener(v -> {
            Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToRegisterFragment2());
        });

        binding.buttonSigninSubmit.setOnClickListener(v -> {
            SignInValidator signInValidator = new SignInValidator(binding);

            if (signInValidator.validateAll()) {
                verifyAuthWithServer();
            }
        });

        if (firstCall) {
            mSignInModel.addResponseObserver(
                    getViewLifecycleOwner(),
                    this::observeResponse);
            firstCall = false;
        }
    }

    /** This is a method used for testing. It skips directly to the home page without needing to sign in.
     *
     * @param v is the current view
     * @author Robert Mangrum
     */
    private void handleHome(View v) {
        NavDirections action = SignInFragmentDirections.actionSignInFragmentToMainActivity("testBypass","");
        Navigation.findNavController(v).navigate(action);
    }

    /** This is called when a user's sign in attempt has been successfully authenticated.
     *
     * @param email The user's email
     * @param jwt The web authentication token
     * @author Chase Alder
     */
    public void successfulSignIn(final String email, final String jwt) {
        Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity(email,jwt));
    }

    /** This method sends the users credentials to the web service for authentication.
     *
     * @author Chase Alder
     */
    private void verifyAuthWithServer() {
        mSignInModel.connect(
                binding.fieldSigninEmail.getText().toString(),
                binding.fieldSigninPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    /** This makes a popup reminding the user to verify their email.
     *
     * @author Chase Alder
     */
    private void verificationPopup() {
        EmailVerificationDialog dialog = new EmailVerificationDialog();
        dialog.show(getActivity().getSupportFragmentManager(),"Email Verification Reminder");
    }

    /** Checks the response from the web service after attempting to sign in.
     * If the user's email is not yet verified, it will put up a popup.
     *
     * @param response the JSON object response from the server
     * @author Chase Alder
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if ((int)response.get("code") == 400) {
                        verificationPopup();
                    }
                    binding.fieldSigninEmail.setError(
                            "Error Authenticating: " +
                                    response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                try {
                    successfulSignIn(
                            binding.fieldSigninEmail.getText().toString(),
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