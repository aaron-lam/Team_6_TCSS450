package edu.uw.tcss450.group6project.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatCardBinding;

/**
 * A RecyclerViewAdapter to create scrolling list view of chats.
 *
 * @author Robert M
 * @version 2 November 2020
 */
public class ChatRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatRecyclerViewAdapter.ChatViewHolder> {

    /**
     * A list of chats.
     */
    private final List<ChatMessage> mMessages;

    /**
     * Parameterized constructor method taking a list of chats.
     *
     * @param items the list of chats
     */
    public ChatRecyclerViewAdapter(List<ChatMessage> items) {
        this.mMessages = items;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chat_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.setChat(mMessages.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    /**
     * Represents an individual row View from the list of rows in the Chat Recycler View.
     */
    public class ChatViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public FragmentChatCardBinding binding;
        private ChatMessage mMessage;

        /**
         * Constructs the Chat view.
         *
         * @param view the view
         */
        public ChatViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatCardBinding.bind(view);
        }

        void setChat(final ChatMessage msg) {
            mMessage = msg;

            binding.textParticipants.setText(msg.getUsername());
            binding.textPreview.setText(msg.getMessage());
        }

    }
}
