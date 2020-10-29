package edu.uw.tcss450.group6project.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;

public class SignInValidator {

    String email, password;
    FragmentSignInBinding binding;
    Pattern passCheck = Pattern.compile("((?=.*[a-z])(?=.*d)(?=.*[@#$%!&()])(?=.*[A-Z]).{6,16})");

    public SignInValidator(FragmentSignInBinding binding) {
        this.binding = binding;
        email = binding.fieldSigninEmail.getText().toString();
        password = binding.fieldSigninPassword.getText().toString();
    }

    public boolean validateAll() {
        boolean result = validatePassword();
        result = result && validateEmail();
        return result;
    }

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
