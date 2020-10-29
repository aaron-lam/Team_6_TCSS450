package edu.uw.tcss450.group6project.ui.auth.sign_in;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInFragment extends Fragment {

    private FragmentSignInBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSignInBinding.inflate(inflater);
        return binding.getRoot();
    }
}