package edu.uw.tcss450.group6project.utils;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.uw.tcss450.group6project.databinding.FragmentRegisterBinding;

public class RegisterValidator extends AppCompatActivity {

    String firstName, lastName, email, nickname, password, retypePassword;
    FragmentRegisterBinding binding;
    Pattern passCheck = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{6,}$");

    public RegisterValidator(FragmentRegisterBinding binding) {
        this.binding = binding;
        firstName = binding.fieldRegisterFirstName.getText().toString();
        lastName = binding.fieldRegisterLastName.getText().toString();
        email = binding.fieldRegisterEmail.getText().toString();
        nickname = binding.fieldRegisterNickname.getText().toString();
        password = binding.fieldRegisterPassword.getText().toString();
        retypePassword = binding.fieldRegisterRetypePassword.getText().toString();
    }

    public boolean validateAll() {
        boolean result = validateRetypePassword();
        result = result && validatePassword();
        result = result && validateEmail();
        result = result && validateNickname();
        result = result && validateLastName();
        result = result & validateFirstName();
        return result;
    }

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

    private boolean validateNickname() {
        if (nickname.length() < 1) {
            binding.fieldRegisterNickname.setError("Cannot be empty");
            return false;
        } else if (!nickname.matches("^[a-zA-Z]*$")) {
            binding.fieldRegisterNickname.setError("Must only contain letters with no spaces");
            return false;
        }

        return true;
    }

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
