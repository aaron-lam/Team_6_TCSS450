package edu.uw.tcss450.group6project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;

/** Used to make sure that sign in parameters are valid before sending them to the web service.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class SignInValidator {

    String email, password;
    FragmentSignInBinding binding;
    Pattern passCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    /** Constructor.
     *
     * @param binding The ViewModel bindings for the sign in page
     * @author Chase Alder
     */
    public SignInValidator(FragmentSignInBinding binding) {
        this.binding = binding;
        email = binding.fieldSigninEmail.getText().toString();
        password = binding.fieldSigninPassword.getText().toString();
    }

    /** Validates all fields.
     *
     * @return True if the inputs are all valid, false if any of them aren't.
     * @author Chase Alder
     */
    public boolean validateAll() {
        boolean result = validatePassword();
        result = result && validateEmail();
        return result;
    }

    /** Ensures the inputted email is valid.
     *
     * @return Whether or not the inputted email is valid.
     * @author Chase Alder
     */
    private boolean validateEmail() {
        if (email.length() < 1) {
            binding.fieldSigninEmail.setError("Cannot be empty");
            return false;
        } else if (email.length() < 5) {
            binding.fieldSigninEmail.setError("Must be a valid email");
            return false;
        } else if (!email.contains("@")) {
            binding.fieldSigninEmail.setError("Must contain an '@' symbol");
            return false;
        }

        return true;
    }

    /** Ensures the inputted password is valid.
     *
     * @return Whether or not the inputted password is valid.
     * @author Chase Alder
     */
    private boolean validatePassword() {

        Matcher m = passCheck.matcher(password);

        if (password.length() < 1) {
            binding.fieldSigninPassword.setError("Cannot be empty");
            return false;
        } else if (password.length() < 6) {
            binding.fieldSigninPassword.setError("Must be at least 6 characters long");
            return false;
        } else if (!m.matches()) {
            binding.fieldSigninPassword.setError("Must contain a lowercase, uppercase, number, and special character");
            return false;
        }

        return true;
    }
}
