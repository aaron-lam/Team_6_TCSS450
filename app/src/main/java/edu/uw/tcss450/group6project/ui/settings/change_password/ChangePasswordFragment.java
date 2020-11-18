package edu.uw.tcss450.group6project.ui.settings.change_password;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentPasswordChangeBinding;
import edu.uw.tcss450.group6project.utils.Validator;

/**
 *
 */
public class ChangePasswordFragment extends Fragment {

    private FragmentPasswordChangeBinding mBinding;
    private ChangePasswordViewModel mChangePasswordModel;
    boolean mFirstCall; // This tells the class whether the "submit" button has been clicked yet.

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFirstCall = true;
        mChangePasswordModel = new ViewModelProvider(getActivity())
                .get(ChangePasswordViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPasswordChangeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
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
                verifyAuthWithServer();
            }

            if (mFirstCall) {
                mChangePasswordModel.addResponseObserver(getViewLifecycleOwner(),
                        this::observeResponse);
                mFirstCall = false;
            }
        });
    }

    /**
     *
     */
    private void verifyAuthWithServer() {
        mChangePasswordModel.connect(
                mBinding.fieldPasswordChangeEmail.getText().toString(),
                mBinding.fieldPasswordChangeOldPassword.getText().toString(),
                mBinding.fieldPasswordChangeNewPassword.getText().toString());
        //This is an Asynchronous call. No statements after should rely on the
        //result of connect().
    }

    private void successConfirmationDialog() {
        AlertDialog.Builder successDialog  = new AlertDialog.Builder(getContext());
        successDialog.setMessage("Your password was successfully changed!");
        successDialog.setTitle("Password Change Successful");
        successDialog.setPositiveButton("Ok", (dialog, which) -> {});
        successDialog.setCancelable(true);
        successDialog.create().show();
    }

    /**
     *
     * @param response
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    // TODO Fix these errors so they're not hardcoded and instead use strings
                    if (response.getJSONObject("data").getString("message").equals("User not found")) {
                        mBinding.fieldPasswordChangeEmail.setError(
                                "Error Authenticating: " + "User was not found");
                    } else if (response.getJSONObject("data").getString("message").equals("Email is not verified yet")) {
                        mBinding.fieldPasswordChangeEmail.setError(
                                "Error Authenticating: " + "Email is not verified yet");
                    } else if (response.getJSONObject("data").getString("message").equals("Credentials did not match")) {
                        mBinding.fieldPasswordChangeEmail.setError(
                                "Error Authenticating: " + "Credentials did not match");
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                successConfirmationDialog();
                getActivity().onBackPressed(); // close the fragment
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}