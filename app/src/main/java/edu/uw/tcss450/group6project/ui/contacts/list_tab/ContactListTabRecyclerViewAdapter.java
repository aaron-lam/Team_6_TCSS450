package edu.uw.tcss450.group6project.ui.contacts.list_tab;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentContactListCardBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.contacts.Contact;

/**
 * A RecyclerViewAdapter to create scrolling list view of contacts.
 *
 * @author Robert M, Chase Alder
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
                .inflate(R.layout.fragment_contact_list_card, parent, false));
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
        public FragmentContactListCardBinding binding;

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

            binding = FragmentContactListCardBinding.bind(view);
            binding.buttonDeleteContact.setOnClickListener(this::handleDelete);
        }

        /**
         * Helper method to handle deleting a contact.
         *
         * @param button the delete button
         */
        private void handleDelete(final View button) {
            mContactListTabViewModel.connectDelete(mUserInfoViewModel.getJWT(),mContact.getMemberId());
        }

        /**
         * Sets each card view for the contact list.
         *
         * @param contact the contact being set
         */
        void setContact(final Contact contact) {
            mContact = contact;
            binding.textContactListName.setText(contact.getUserName());
        }
    }
}
