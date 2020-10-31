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
import edu.uw.tcss450.group6project.utils.RegisterValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;
    private RegisterViewModel mRegisterModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

            mRegisterModel.addResponseObserver(getViewLifecycleOwner(),
                    this::observeResponse);
        });
    }

    private void successfulSignIn() {
        Navigation.findNavController(getView()).navigate(RegisterFragmentDirections.actionRegisterFragmentToSignInFragment());
    }

    private void verifyAuthWithServer() {
        mRegisterModel.connect(
                binding.fieldRegisterFirstName.getText().toString(),
                binding.fieldRegisterLastName.getText().toString(),
                binding.fieldRegisterEmail.getText().toString(),
                binding.fieldRegisterPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

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
                successfulSignIn();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}