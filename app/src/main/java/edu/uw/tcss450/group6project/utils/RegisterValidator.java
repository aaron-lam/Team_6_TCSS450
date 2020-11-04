package edu.uw.tcss450.group6project.utils;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.databinding.FragmentRegisterBinding;

/** Used to make sure that registration parameters are valid before sending them to the web service.
 *
 * @author Chase Alder
 * @version 1.0
 */
public class RegisterValidator extends AppCompatActivity {

    String firstName, lastName, email, username, password, retypePassword;
    FragmentRegisterBinding binding;
    Pattern passCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    /** Constructor.
     *
     * @param binding The ViewModel bindings for the register page.
     */
    public RegisterValidator(FragmentRegisterBinding binding) {
        this.binding = binding;
        firstName = binding.fieldRegisterFirstName.getText().toString();
        lastName = binding.fieldRegisterLastName.getText().toString();
        email = binding.fieldRegisterEmail.getText().toString();
        username = binding.fieldRegisterUsername.getText().toString();
        password = binding.fieldRegisterPassword.getText().toString();
        retypePassword = binding.fieldRegisterRetypePassword.getText().toString();
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
        if (firstName.length() < 1) {
            binding.fieldRegisterFirstName.setError("Cannot be empty");
            return false;
        } else if (!firstName.matches("^[a-zA-Z]*$")) {
            binding.fieldRegisterFirstName.setError("Must only contain letters with no spaces");
            return false;
        }

        return true;
    }

    /** Ensures the inputted last name is valid.
     *
     * @return Whether or not the inputted last name is valid.
     */
    private boolean validateLastName() {
        if (lastName.length() < 1) {
            binding.fieldRegisterLastName.setError("Cannot be empty");
            return false;
        } else if (!lastName.matches("^[a-zA-Z]*$")) {
            binding.fieldRegisterLastName.setError("Must only contain letters with no spaces");
            return false;
        }

        return true;
    }

    /** Ensures the inputted email is valid.
     *
     * @return Whether or not the inputted email is valid.
     */
    private boolean validateEmail() {
        if (email.length() < 1) {
            binding.fieldRegisterEmail.setError("Cannot be empty");
            return false;
        } else if (email.length() < 5) {
            binding.fieldRegisterEmail.setError("Must be a valid email");
            return false;
        } else if (!email.contains("@")) {
            binding.fieldRegisterEmail.setError("Must contain an '@' symbol");
            return false;
        }

        return true;
    }

    /** Ensures the inputted username is valid.
     *
     * @return Whether or not the inputted username is valid.
     */
    private boolean validateUsername() {
        if (username.length() < 1) {
            binding.fieldRegisterUsername.setError("Cannot be empty");
            return false;
        } else if (!username.matches("^[a-zA-Z]*$")) {
            binding.fieldRegisterUsername.setError("Must only contain letters with no spaces");
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
            binding.fieldRegisterPassword.setError("Cannot be empty");
            return false;
        } else if (password.length() < 6) {
            binding.fieldRegisterPassword.setError("Must be at least 6 characters long");
            return false;
        } else if (!m.matches()) {
            binding.fieldRegisterPassword.setError("Must contain a lowercase, uppercase, number, and special character");
            return false;
        }

        return true;
    }

    /** Ensures the inputted "retype password" is valid.
     *
     * @return Whether or not the inputted "retype password" is valid.
     */
    private boolean validateRetypePassword() {
        if (retypePassword.length() < 1) {
            binding.fieldRegisterRetypePassword.setError("Cannot be empty");
            return false;
        } else if (!password.equals(retypePassword)) {
            binding.fieldRegisterRetypePassword.setError("Passwords must match");
            return false;
        }

        return true;
    }
}
