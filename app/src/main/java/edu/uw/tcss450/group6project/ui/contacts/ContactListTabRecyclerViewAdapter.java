package edu.uw.tcss450.group6project.ui.contacts;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentContactCardBinding;
import edu.uw.tcss450.group6project.databinding.FragmentContactListBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;

/**
 * A RecyclerViewAdapter to create scrolling list view of contacts.
 *
 * @author Robert M
 * @version 3 November 2020
 */
public class ContactListTabRecyclerViewAdapter extends
        RecyclerView.Adapter<ContactListTabRecyclerViewAdapter.ContactViewHolder> {

    /**
     * The list of contacts.
     */
    private final List<Contact> mContacts;

    private Fragment mFragment;
    private UserInfoViewModel mUserInfoViewModel;

    /**
     * Constructs the RecyclerView.
     *
     * @param contacts a list of contacts
     */
    public ContactListTabRecyclerViewAdapter(List<Contact> contacts, Fragment fragment, UserInfoViewModel userInfoViewModel) {
        this.mContacts = contacts;
        this.mFragment = fragment;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_contact_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        holder.setContact(mContacts.get(position));
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

    /**
     * Represents an individual row View from the list of rows in the Contact Recycler View.
     *
     * @author Robert M
     * @version 3 November 2020
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        /**
         * The View.
         */
        public final View mView;

        /**
         * The binding for the contact list card.
         */
        public FragmentContactCardBinding binding;

        /**
         * The contact.
         */
        private Contact mContact;

        private ContactListTabViewModel mContactListTabViewModel;

        /**
         * Constructs the contact view.
         *
         * @param view the view
         */
        public ContactViewHolder(View view) {
            super(view);
            mView = view;

            // Get the UserInfoViewModel and ContactListTabViewModel (used for contact deletion)
            ViewModelProvider provider = new ViewModelProvider(mFragment.getActivity());
            mContactListTabViewModel = provider.get(ContactListTabViewModel.class);
            mUserInfoViewModel = provider.get(UserInfoViewModel.class);

            binding = FragmentContactCardBinding.bind(view);
            binding.buttonDeleteContact.setOnClickListener(this::handleDelete);
            binding.buttonOpenChat.setOnClickListener(this::createChat);
        }

        /**
         * Helper method to handle deleting a contact.  Not yet implemented.
         *
         * @param button the delete button
         */
        private void handleDelete(final View button) {
            mContactListTabViewModel.connectDelete(mUserInfoViewModel.getJWT(),mContact.getUserId());
        }

        /**
         * Helper method to open a new chat with the selected contact.
         *
         * @param button the create chat button
         */
        private void createChat(final View button) {
//            List<String> participants = new ArrayList<>();
//            participants.add(binding.textContactName.getText().toString());
//            ChatRoom newChat = new ChatRoom(participants);
//            Navigation.findNavController(mView).navigate
//                    (ContactListFragmentDirections.actionNavigationContactsToChatFragment(newChat));
        }

        /**
         * Sets each card view for the contact list.
         *
         * @param contact the contact being set
         */
        void setContact(final Contact contact) {
            mContact = contact;
            binding.textContactName.setText(contact.getFirstName());
        }
    }
}
