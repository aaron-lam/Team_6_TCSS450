package edu.uw.tcss450.group6project.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentRegisterBinding;

/** Used to make sure that registration parameters are valid before sending them to the web service.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class RegisterValidator extends AppCompatActivity {

    private String mFirstName, mLastName, mEmail, mUsername, mPassword, mRetypePassword, mEmptyFieldError;
    private FragmentRegisterBinding mBinding;
    private FragmentActivity mActivity;
    private Pattern mPassCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    /** Constructor.
     *
     * @param binding The ViewModel bindings for the register page.
     */
    public RegisterValidator(FragmentActivity activity, FragmentRegisterBinding binding) {
        this.mBinding = binding;
        this.mActivity = activity;
        mEmptyFieldError = activity.getResources().getString(R.string.all_empty_field_error);
        mFirstName = binding.fieldRegisterFirstName.getText().toString();
        mLastName = binding.fieldRegisterLastName.getText().toString();
        mEmail = binding.fieldRegisterEmail.getText().toString();
        mUsername = binding.fieldRegisterUsername.getText().toString();
        mPassword = binding.fieldRegisterPassword.getText().toString();
        mRetypePassword = binding.fieldRegisterRetypePassword.getText().toString();
    }

    /** Validates all fields.
     *
     * @return True if the inputs are all valid, false if any of them aren't.
     */
    public boolean validateAll() {
        boolean result = validateRetypePassword();
        result = result && validatePassword();
        result = result && validateEmail();
        result = result && validateUsername();
        result = result && validateLastName();
        result = result & validateFirstName();
        return result;
    }

    /** Ensures the inputted first name is valid.
     *
     * @return Whether or not the inputted first name is valid.
     */
    private boolean validateFirstName() {
        if (mFirstName.length() < 1) {
            mBinding.fieldRegisterFirstName.setError(mEmptyFieldError);
            return false;
        } else if (!mFirstName.matches("^[a-zA-Z]*$")) {
            mBinding.fieldRegisterFirstName.setError(mActivity
                    .getResources().getString(R.string.register_must_only_contain_letters_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted last name is valid.
     *
     * @return Whether or not the inputted last name is valid.
     */
    private boolean validateLastName() {
        if (mLastName.length() < 1) {
            mBinding.fieldRegisterLastName.setError(mEmptyFieldError);
            return false;
        } else if (!mLastName.matches("^[a-zA-Z]*$")) {
            mBinding.fieldRegisterLastName.setError(mActivity
                    .getResources().getString(R.string.register_must_only_contain_letters_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted email is valid.
     *
     * @return Whether or not the inputted email is valid.
     */
    private boolean validateEmail() {
        if (mEmail.length() < 1) {
            mBinding.fieldRegisterEmail.setError(mEmptyFieldError);
            return false;
        } else if (mEmail.length() < 5) {
            mBinding.fieldRegisterEmail.setError(mActivity
                    .getResources().getString(R.string.all_email_invalid_error));
            return false;
        } else if (!mEmail.contains("@")) {
            mBinding.fieldRegisterEmail.setError(mActivity
                    .getResources().getString(R.string.all_email_no_at_symbol_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted username is valid.
     *
     * @return Whether or not the inputted username is valid.
     */
    private boolean validateUsername() {
        if (mUsername.length() < 1) {
            mBinding.fieldRegisterUsername.setError(mEmptyFieldError);
            return false;
        } else if (!mUsername.matches("^[a-zA-Z]*$")) {
            mBinding.fieldRegisterUsername.setError(mActivity
                    .getResources().getString(R.string.register_must_only_contain_letters_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted password is valid.
     *
     * @return Whether or not the inputted password is valid.
     */
    private boolean validatePassword() {

        Matcher m = mPassCheck.matcher(mPassword);

        if (mPassword.length() < 1) {
            mBinding.fieldRegisterPassword.setError(mEmptyFieldError);
            return false;
        } else if (mPassword.length() < 6) {
            mBinding.fieldRegisterPassword.setError(mActivity
                    .getResources().getString(R.string.all_password_length_error));
            return false;
        } else if (!m.matches()) {
            mBinding.fieldRegisterPassword.setError(mActivity
                    .getResources().getString(R.string.all_password_requirements_error));
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
            mBinding.fieldRegisterRetypePassword.setError(mEmptyFieldError);
            return false;
        } else if (!mPassword.equals(mRetypePassword)) {
            mBinding.fieldRegisterRetypePassword.setError(mActivity
                    .getResources().getString(R.string.register_retype_password_error));
            return false;
        }

        return true;
    }
}
