package edu.uw.tcss450.group6project.ui.settings;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group6project.databinding.FragmentSettingsHomeBinding;

public class SettingsHomeFragment extends Fragment {

    private FragmentSettingsHomeBinding mBinding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSettingsHomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonSettingsChangePassword.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(SettingsHomeFragmentDirections.actionSettingsFragmentToChangePasswordFragment());
        });
    }

}
