package edu.uw.tcss450.group6project.ui.contacts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentContactCardBinding;

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

    /**
     * The fragment's context.
     */
    private Context mContext;

    /**
     * Constructs the RecyclerView.
     *
     * @param contacts a list of contacts
     */
    public ContactListTabRecyclerViewAdapter(List<Contact> contacts) {
        this.mContacts = contacts;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
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

        /**
         * Constructs the contact view.
         *
         * @param view the view
         */
        public ContactViewHolder(View view) {
            super(view);
            mView = view;
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
//            mContacts.remove(mContacts.indexOf(mContact));
            Toast.makeText(mContext, R.string.all_incomplete, Toast.LENGTH_SHORT).show();
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
