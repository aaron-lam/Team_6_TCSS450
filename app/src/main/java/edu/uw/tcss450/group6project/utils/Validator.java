package edu.uw.tcss450.group6project.utils;

import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.R;

/** Used to make sure that registration parameters are valid before sending them to the web service.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class Validator extends AppCompatActivity {

    private EditText mFirstName, mLastName, mEmail, mUsername, mOldPassword, mNewPassword, mRetypeNewPassword;
    final private String mEmptyFieldError = "Cannot be empty";
    private FragmentActivity mActivity;
    private Pattern mPassCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!^#%*?&()])[A-Za-z\\d@$#!^%*?&()]{6,}$");

    /** Constructor for registration.
     *
     * @param // binding The ViewModel bindings for the register page.
     */
    public Validator(FragmentActivity activity, EditText firstName, EditText lastName, EditText username, EditText email, EditText password, EditText retypePassword) {
        this.mActivity = activity;
        this.mFirstName = firstName;
        this.mLastName = lastName;
        this.mEmail = email;
        this.mUsername = username;
        this.mNewPassword = password;
        this.mRetypeNewPassword = retypePassword;
    }

    /** Constructor for password change
     *
     * @param activity
     * @param email
     * @param oldPassword
     * @param newPassword
     * @param retypeNewPassword
     */
    public Validator(FragmentActivity activity, EditText email, EditText oldPassword, EditText newPassword, EditText retypeNewPassword) {
        this.mActivity = activity;
        this.mEmail = email;
        this.mOldPassword = oldPassword;
        this.mNewPassword = newPassword;
        this.mRetypeNewPassword = retypeNewPassword;
    }

    /** Constructor for sign in
     *
     * @param activity
     * @param email
     * @param password
     */
    public Validator(FragmentActivity activity, EditText email, EditText password) {
        this.mActivity = activity;
        this.mEmail = email;
        this.mNewPassword = password;
    }

    /** Can be used to validate either email or username individually.
     *
     * @param activity The current activity
     * @param input the text to be validated
     * @param caseChoice pass in "email" to validate an email, and "username" to validate a username
     */
    public Validator(FragmentActivity activity, EditText input, String caseChoice) {

        if (caseChoice.equals("email")) {
            this.mActivity = activity;
            this.mEmail = input;
        } else if (caseChoice.equals("username")) {
            this.mActivity = activity;
            this.mUsername = input;
        }
    }

    /** Validates all fields.
     *
     * @return True if the inputs are all valid, false if any of them aren't.
     */
    public boolean validateAll() {

        boolean result = (mFirstName == null) ? true : validateFirstName();
        result = result && ((mLastName == null) ? true : validateLastName());
        result = result && ((mUsername == null) ? true : validateUsername());
        result = result && ((mEmail == null) ? true : validateEmail());
        result = result && ((mOldPassword == null) ? true : validatePassword(mOldPassword));
        result = result && ((mNewPassword == null) ? true : validatePassword(mNewPassword));
        result = result && ((mRetypeNewPassword == null) ? true : validateRetypePassword());
        return result;
    }

    /** Ensures the inputted first name is valid.
     *
     * @return Whether or not the inputted first name is valid.
     */
    private boolean validateFirstName() {
        if (mFirstName.getText().toString().length() < 1) {
            mFirstName.setError(mEmptyFieldError);
            return false;
        } else if (!mFirstName.getText().toString().matches("^[a-zA-Z]*$")) {
            mFirstName.setError(mActivity
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
        if (mLastName.getText().toString().length() < 1) {
            mLastName.setError(mEmptyFieldError);
            return false;
        } else if (!mLastName.getText().toString().matches("^[a-zA-Z]*$")) {
            mLastName.setError(mActivity
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
        if (mEmail.getText().toString().length() < 1) {
            mEmail.setError(mEmptyFieldError);
            return false;
        } else if (mEmail.getText().toString().length() < 5) {
            mEmail.setError(mActivity
                    .getResources().getString(R.string.all_email_invalid_error));
            return false;
        } else if (!mEmail.getText().toString().contains("@")) {
            mEmail.setError(mActivity
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

        if (mUsername.getText().toString().length() < 1) {
            mUsername.setError(mEmptyFieldError);
            return false;
        } else if (!mUsername.getText().toString().matches("^[a-zA-Z]*$")) {
            mUsername.setError(mActivity
                    .getResources().getString(R.string.register_must_only_contain_letters_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted password is valid.
     *
     * @return Whether or not the inputted password is valid.
     */
    private boolean validatePassword(EditText password) {

        Matcher m = mPassCheck.matcher(password.getText().toString());

        if (password.getText().toString().length() < 1) {
            password.setError(mEmptyFieldError);
            return false;
        } else if (password.getText().toString().length() < 6) {
            password.setError(mActivity
                    .getResources().getString(R.string.all_password_length_error));
            return false;
        } else if (!m.matches()) {
            password.setError(mActivity
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

        if(!validatePassword(mRetypeNewPassword))
            return false;

        if (!mNewPassword.getText().toString().equals(mRetypeNewPassword.getText().toString())) {
            mRetypeNewPassword.setError(mActivity
                    .getResources().getString(R.string.all_retype_password_error));
            return false;
        }

        return true;
    }
}
