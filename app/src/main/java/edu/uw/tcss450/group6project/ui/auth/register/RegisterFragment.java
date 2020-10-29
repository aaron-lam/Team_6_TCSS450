package edu.uw.tcss450.group6project.ui.auth.register;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentRegisterBinding;
import edu.uw.tcss450.group6project.utils.RegisterValidator;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding binding;

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
                Navigation.findNavController(getView()).navigate(RegisterFragmentDirections.actionRegisterFragmentToSignInFragment());
            }
        });
    }
}