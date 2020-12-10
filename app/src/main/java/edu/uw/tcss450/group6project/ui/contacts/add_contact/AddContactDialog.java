package edu.uw.tcss450.group6project.ui.contacts.add_contact;

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
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.utils.Validator;

/** Used to create the dialog popup for adding a new contact.
 *
 * @author chasealder
 */
public class AddContactDialog {

    private Fragment mCurrentFragment;
    private FragmentActivity mCurrentActivity;
    private View mView;
    private boolean firstCall;
    private AlertDialog mDialog;
    private AddContactViewModel mAddContactViewModel;
    private UserInfoViewModel mUserInfoViewModel;

    public AddContactDialog(Fragment fragment) {
        mCurrentFragment = fragment;
        mCurrentActivity = mCurrentFragment.getActivity();
        firstCall = true;
        ViewModelProvider viewProvider = new ViewModelProvider(mCurrentActivity);
        mAddContactViewModel = viewProvider.get(AddContactViewModel.class);
        mUserInfoViewModel = viewProvider.get(UserInfoViewModel.class);
    }

    /** This is the method that actually builds the dialog and displays it.
     *
     */
    public void buildAddContactDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(mCurrentFragment.getContext());
        mBuilder.setTitle(R.string.contact_add);
        mView = mCurrentFragment.getLayoutInflater().inflate(R.layout.dialog_contacts_add,null);

        // Grabbing references to all the elements in the fragment
        EditText mUsername = (EditText) mView.findViewById(R.id.field_contacts_add_username);
        Button mSubmit = (Button) mView.findViewById(R.id.button_contacts_add_submit);
        Button mCancel = (Button) mView.findViewById(R.id.button_contacts_add_cancel);

        // Assembling the dialog
        mBuilder.setView(mView);
        mDialog = mBuilder.create();
        mDialog.show();

        // Cancel button functionality
        mCancel.setOnClickListener(button -> mDialog.cancel());

        // Submit button functionality
        mSubmit.setOnClickListener(button -> {

            Validator validator = new Validator(mCurrentFragment.getActivity(), mUsername, "username");

            // If all fields are valid, send the request
            if (validator.validateAll()) {
                // Disable so that the user cannot send multiple requests quickly
                mSubmit.setEnabled(false);
                verifyAddContactWithServer(mUsername.getText().toString());
            }

            if (firstCall) {
                mAddContactViewModel.addResponseObserver(mCurrentFragment.getViewLifecycleOwner(),
                        this::observeResponse);
                firstCall = false;
            }
        });
    }

    /** This method use the AddContactViewModel to send the contact add request to the web service.
     *
     * @param username the username of the member you want to add as a contact
     */
    private void verifyAddContactWithServer(String username) {
        mAddContactViewModel.connectAddContact(mUserInfoViewModel.getJWT(),username);
        //This is an Asynchronous call. No statements after should rely on the
        //result of connectAddContact().
    }

    /** Handles the response from the web service after trying to add a new contact
     *
     * @param response the response from the web service
     */
    private void observeResponse(final JSONObject response) {
        if (response.length() > 0) {
            // On failure...
            if (response.has("code")) {

                EditText usernameField = (EditText)mView.findViewById(R.id.field_contacts_add_username);

                try {
                    usernameField.setError(
                            "Error Adding Contact: " + response.getJSONObject("data").getString("message"));
                } catch (JSONException e) {
                    Log.e("JSON Parse Error", e.getMessage());
                }

                // Re-enable the submit button so the user can make another request
                ((Button)mView.findViewById(R.id.button_contacts_add_submit)).setEnabled(true);
            } else {
                // Close the window
                mDialog.cancel();

                // Re-enable the submit button so the user can make another request
                ((Button)mView.findViewById(R.id.button_contacts_add_submit)).setEnabled(true);
                ContactAddSuccessfulDialog dialog = new ContactAddSuccessfulDialog();
                dialog.show(mCurrentActivity.getSupportFragmentManager(),"Contact Added Successfully");
            }
        } else {
            Log.d("JSON Response", "No Response");
        }
    }

    /** This is an inner class, used to create a popup confirming a contact was added successfully.
     * @author chasealder
     */
    public static class ContactAddSuccessfulDialog extends AppCompatDialogFragment {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Contact Added Successfully")
                    .setMessage("The user has been sent a contact request which they must approve. You will be notified if they accept.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {}
                    });
            return builder.create();
        }
    }
}
