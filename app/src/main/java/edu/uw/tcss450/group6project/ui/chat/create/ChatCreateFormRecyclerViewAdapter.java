package edu.uw.tcss450.group6project.ui.chat.create;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatCreateFormCardBinding;
import edu.uw.tcss450.group6project.ui.contacts.Contact;

/**
 * A RecyclerViewAdapter to create scrolling list view of contacts.
 *
 * @author Aaron L
 * @version 22 November 2020
 */
public class ChatCreateFormRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatCreateFormRecyclerViewAdapter.ContactViewHolder> {

    /**
     * The list of contacts.
     */
    private final List<Contact> mContacts;

    private ChatCreateFormViewModel chatCreateFormViewModel;

    /**
     * Constructs the RecyclerView.
     *
     * @param items a list of contacts
     */
    public ChatCreateFormRecyclerViewAdapter(List<Contact> items, ChatCreateFormViewModel chatCreateFormViewModel) {
        this.mContacts = items;
        this.chatCreateFormViewModel = chatCreateFormViewModel;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ContactViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_create_form_card, parent, false));
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
     * @author Aaron L
     * @version 22 November 2020
     */
    public class ContactViewHolder extends RecyclerView.ViewHolder {

        /**
         * The View.
         */
        public final View mView;

        /**
         * The binding for the contact list card.
         */
        public FragmentChatCreateFormCardBinding binding;

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
            binding = FragmentChatCreateFormCardBinding.bind(view);
        }

        /**
         * Helper method to add a selected contact to new room.
         *
         * @param button the create chat button
         */
        private void addContactToNewRoom(final View button) {
            chatCreateFormViewModel.updateContact(this.mContact.getMemberId());
        }

        /**
         * Sets each card view for the contact list.
         *
         * @param contact the contact being set
         */
        void setContact(final Contact contact) {
            mContact = contact;
            chatCreateFormViewModel.uncheckContact(this.mContact.getMemberId());
            binding.textContact.setText(contact.getUserName());
            binding.checkboxAdd.setOnClickListener(this::addContactToNewRoom);
        }
    }
}
