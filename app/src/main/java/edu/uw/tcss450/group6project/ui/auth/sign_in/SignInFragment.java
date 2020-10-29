package edu.uw.tcss450.group6project.ui.auth.sign_in;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group6project.utils.SignInValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    private void handleHome(View v) {
        NavDirections action = SignInFragmentDirections.actionSignInFragmentToMainActivity();
        Navigation.findNavController(v).navigate(action);
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
                Navigation.findNavController(getView()).navigate(SignInFragmentDirections.actionSignInFragmentToMainActivity());
            }
        });
    }
}