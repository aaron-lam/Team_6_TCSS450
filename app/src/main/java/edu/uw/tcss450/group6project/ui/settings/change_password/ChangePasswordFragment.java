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

/** The page that shows when a user wants to change their password
 *
 * @author Chase Alder
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

    /** Sends the change password request to the web service asynchronously
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

    /** Popup that tells the user that their password was successfully changed
     *
     */
    private void successConfirmationDialog() {
        AlertDialog.Builder successDialog  = new AlertDialog.Builder(getContext());
        successDialog.setMessage("Your password was successfully changed!");
        successDialog.setTitle("Password Change Successful");
        successDialog.setPositiveButton("Ok", (dialog, which) -> {});
        successDialog.setCancelable(true);
        successDialog.create().show();
    }

    /** Reacts to the response from the web service
     *
     * @param response the response from the web service
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if (response.getJSONObject("data").getString("message").equals("User not found")) {
                        mBinding.fieldPasswordChangeEmail.setError(
                                "Error Authenticating: " + response.getJSONObject("data").getString("message"));
                    } else if (response.getJSONObject("data").getString("message").equals("Email is not verified yet")) {
                        mBinding.fieldPasswordChangeEmail.setError(
                                "Error Authenticating: " + response.getJSONObject("data").getString("message"));
                    } else if (response.getJSONObject("data").getString("message").equals("Credentials did not match")) {
                        mBinding.fieldPasswordChangeEmail.setError(
                                "Error Authenticating: " + response.getJSONObject("data").getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                successConfirmationDialog();
                getActivity().onBackPressed();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }
}