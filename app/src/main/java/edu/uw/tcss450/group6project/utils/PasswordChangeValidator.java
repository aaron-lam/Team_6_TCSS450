package edu.uw.tcss450.group6project.utils;

import androidx.fragment.app.FragmentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentPasswordChangeBinding;

/**
 *
 */
public class PasswordChangeValidator {

    private String mEmail, mOldPassword, mNewPassword, mRetypePassword;
    private FragmentPasswordChangeBinding mBinding;
    private FragmentActivity mActivity;
    private Pattern mPassCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    /** Constructor.
     *
     * @param binding The ViewModel bindings for the sign in page
     */
    public PasswordChangeValidator(FragmentActivity activity, FragmentPasswordChangeBinding binding) {
        this.mBinding = binding;
        this.mActivity = activity;
        mEmail = binding.fieldPasswordChangeEmail.getText().toString();
        mOldPassword = binding.fieldPasswordChangeOldPassword.getText().toString();
        mNewPassword = binding.fieldPasswordChangeNewPassword.getText().toString();
        mRetypePassword = binding.fieldPasswordChangeRetypePassword.getText().toString();
    }

    /** Validates all fields.
     *
     * @return True if the inputs are all valid, false if any of them aren't.
     */
    public boolean validateAll() {
        boolean result = validateOldPassword();
        result = result && validateNewPassword();
        result = result && validateRetypePassword();
        result = result && validateEmail();
        return result;
    }

    /** Ensures the inputted email is valid.
     *
     * @return Whether or not the inputted email is valid.
     */
    private boolean validateEmail() {
        if (mEmail.length() < 1) {
            mBinding.fieldPasswordChangeEmail.setError(mActivity
                    .getResources().getString(R.string.all_empty_field_error));
            return false;
        } else if (mEmail.length() < 5) {
            mBinding.fieldPasswordChangeEmail.setError(mActivity
                    .getResources().getString(R.string.all_email_invalid_error));
            return false;
        } else if (!mEmail.contains("@")) {
            mBinding.fieldPasswordChangeEmail.setError(mActivity
                    .getResources().getString(R.string.all_email_no_at_symbol_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted "retype password" is valid.
     *
     * @return Whether or not the inputted "retype password" is valid.
     */
    private boolean validateRetypePassword() {
        if (mRetypePassword.length() < 1) {
            mBinding.fieldPasswordChangeRetypePassword.setError(mActivity
                    .getResources().getString(R.string.all_empty_field_error));
            return false;
        } else if (!mNewPassword.equals(mRetypePassword)) {
            mBinding.fieldPasswordChangeRetypePassword.setError(mActivity
                    .getResources().getString(R.string.all_retype_password_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted new password is valid.
     *
     * @return Whether or not the inputted password is valid.
     */
    private boolean validateNewPassword() {

        Matcher m = mPassCheck.matcher(mNewPassword);

        if (mNewPassword.length() < 1) {
            mBinding.fieldPasswordChangeNewPassword.setError(mActivity
                    .getResources().getString(R.string.all_empty_field_error));
            return false;
        } else if (mNewPassword.length() < 6) {
            mBinding.fieldPasswordChangeNewPassword.setError(mActivity
                    .getResources().getString(R.string.all_password_length_error));
            return false;
        } else if (!m.matches()) {
            mBinding.fieldPasswordChangeNewPassword.setError(mActivity
                    .getResources().getString(R.string.all_password_requirements_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted old password is valid.
     *
     * @return Whether or not the inputted password is valid.
     */
    private boolean validateOldPassword() {

        Matcher m = mPassCheck.matcher(mOldPassword);

        if (mOldPassword.length() < 1) {
            mBinding.fieldPasswordChangeOldPassword.setError(mActivity
                    .getResources().getString(R.string.all_empty_field_error));
            return false;
        } else if (mOldPassword.length() < 6) {
            mBinding.fieldPasswordChangeOldPassword.setError(mActivity
                    .getResources().getString(R.string.all_password_length_error));
            return false;
        } else if (!m.matches()) {
            mBinding.fieldPasswordChangeOldPassword.setError(mActivity
                    .getResources().getString(R.string.all_password_requirements_error));
            return false;
        }

        return true;
    }
}
