package edu.uw.tcss450.group6project.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentPasswordChangeBinding;
import edu.uw.tcss450.group6project.utils.Validator;

/**
 *
 */
public class PasswordChangeFragment extends Fragment {

    FragmentPasswordChangeBinding mBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_password_change, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.buttonPasswordChangeSubmit.setOnClickListener(v -> {

            Validator validator = new Validator(getActivity(),
                    mBinding.fieldPasswordChangeEmail,
                    mBinding.fieldPasswordChangeOldPassword,
                    mBinding.fieldPasswordChangeNewPassword,
                    mBinding.fieldPasswordChangeRetypePassword);

            if (validator.validateAll()) {
                // do stuff here
            }

        });
    }
}