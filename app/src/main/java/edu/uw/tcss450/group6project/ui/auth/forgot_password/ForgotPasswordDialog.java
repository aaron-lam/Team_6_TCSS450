package edu.uw.tcss450.group6project.ui.auth.forgot_password;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import org.json.JSONException;
import org.json.JSONObject;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.utils.Validator;

public class ForgotPasswordDialog implements View.OnClickListener {

    private Fragment signInFragment;
    private FragmentActivity activity;
    private View mView;
    private boolean mFirstCall;
    private AlertDialog mDialog;
    private ForgotPasswordViewModel mForgotPasswordModel;

    public ForgotPasswordDialog(Fragment fragment) {
        mFirstCall = true;
        signInFragment = fragment;
        activity = signInFragment.getActivity();
        mForgotPasswordModel = new ViewModelProvider(activity)
                .get(ForgotPasswordViewModel.class);
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(signInFragment.getContext());
        mBuilder.setTitle(R.string.forgot_password_title);
        mView = signInFragment.getLayoutInflater().inflate(R.layout.dialog_forgot_password,null);

        // Grabbing references to all the elements in the fragment
        EditText mEmail = (EditText) mView.findViewById(R.id.field_forgot_password_email);
        Button mSubmit = (Button) mView.findViewById(R.id.button_forgot_password_submit);
        Button mCancel = (Button) mView.findViewById(R.id.button_forgot_password_cancel);

        // Assembling the dialog?
        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();

        // Cancel button functionality
        mCancel.setOnClickListener(button -> mDialog.cancel());

        // Submit button functionality
        mSubmit.setOnClickListener(button -> {
            Validator validator = new Validator(signInFragment.getActivity(), mEmail);

            // If all fields are valid, send the request
            if (validator.validateAll()) {
                verifyForgotPasswordWithServer(mEmail.getText().toString());
            }

            if (mFirstCall) {
                mForgotPasswordModel.addResponseObserver(signInFragment.getViewLifecycleOwner(),
                        this::observeResponse);
                mFirstCall = false;
            }
        });
    }

    private void verifyForgotPasswordWithServer(String email) {
        mForgotPasswordModel.connectForgotPassword(email);
        //This is an Asynchronous call. No statements after should rely on the
        //result of connectForgotPassword().
    }

    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            if (response.has("code")) {
                try {
                    if (response.getJSONObject("data").getString("message").equals("Email is not verified yet")) {
                        ((EditText)mView.findViewById(R.id.field_forgot_password_email)).setError(
                                "Error Authenticating: " + response.getJSONObject("data").getString("message"));
                    } else {
                        ((EditText)mView.findViewById(R.id.field_forgot_password_email)).setError(
                                "Error Authenticating: " + response.getJSONObject("data").getString("message"));
                    }
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }
            } else {
                ResetVerificationDialog dialog = new ResetVerificationDialog();
                dialog.show(activity.getSupportFragmentManager(),"Password Reset Success");
                mDialog.cancel();
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    public static class ResetVerificationDialog extends AppCompatDialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Password Reset Success")
                    .setMessage("Please click the link in the email sent to you to complete your password reset.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
            return builder.create();
        }
    }
}
