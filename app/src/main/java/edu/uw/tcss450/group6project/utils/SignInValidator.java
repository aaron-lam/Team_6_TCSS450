package edu.uw.tcss450.group6project.utils;

import androidx.fragment.app.FragmentActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;

/** Used to make sure that sign in parameters are valid before sending them to the web service.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class SignInValidator {

    String email, password;
    FragmentSignInBinding binding;
    FragmentActivity activity;
    Pattern passCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    /** Constructor.
     *
     * @param binding The ViewModel bindings for the sign in page
     */
    public SignInValidator(FragmentActivity activity, FragmentSignInBinding binding) {
        this.binding = binding;
        this.activity = activity;
        email = binding.fieldSigninEmail.getText().toString();
        password = binding.fieldSigninPassword.getText().toString();
    }

    /** Validates all fields.
     *
     * @return True if the inputs are all valid, false if any of them aren't.
     */
    public boolean validateAll() {
        boolean result = validatePassword();
        result = result && validateEmail();
        return result;
    }

    /** Ensures the inputted email is valid.
     *
     * @return Whether or not the inputted email is valid.
     */
    private boolean validateEmail() {
        if (email.length() < 1) {
            binding.fieldSigninEmail.setError(activity
                    .getResources().getString(R.string.all_empty_field_error));
            return false;
        } else if (email.length() < 5) {
            binding.fieldSigninEmail.setError(activity
                    .getResources().getString(R.string.all_email_invalid_error));
            return false;
        } else if (!email.contains("@")) {
            binding.fieldSigninEmail.setError(activity
                    .getResources().getString(R.string.all_email_no_at_symbol_error));
            return false;
        }

        return true;
    }

    /** Ensures the inputted password is valid.
     *
     * @return Whether or not the inputted password is valid.
     */
    private boolean validatePassword() {

        Matcher m = passCheck.matcher(password);

        if (password.length() < 1) {
            binding.fieldSigninPassword.setError(activity
                    .getResources().getString(R.string.all_empty_field_error));
            return false;
        } else if (password.length() < 6) {
            binding.fieldSigninPassword.setError(activity
                    .getResources().getString(R.string.all_password_length_error));
            return false;
        } else if (!m.matches()) {
            binding.fieldSigninPassword.setError(activity
                    .getResources().getString(R.string.all_password_requirements_error));
            return false;
        }

        return true;
    }
}
