package edu.uw.tcss450.group6project.ui.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatlistCardBinding;

/**
 * A RecyclerViewAdapter to create scrolling list view of chats.
 *
 * @author Robert M
 * @version 2 November 2020
 */
public class ChatListRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    /**
     * A list of chats.
     */
    private final List<ChatRoom> mChats;

    /**
     * Parameterized constructor method taking a list of chats.
     *
     * @param items the list of chats
     */
    public ChatListRecyclerViewAdapter(List<ChatRoom> items) {
        this.mChats = items;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatListViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_chatlist_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatListViewHolder holder, int position) {
        holder.setChat(mChats.get(position));
    }

    @Override
    public int getItemCount() {
        return mChats.size();
    }

    /**
     * Represents an individual row View from the list of rows in the Chat Recycler View.
     *
     * @author Robert M
     * @version 3 November 2020
     */
    public class ChatListViewHolder extends RecyclerView.ViewHolder {

        /** The view. */
        public final View mView;

        /** The binding for the chat list card. */
        public FragmentChatlistCardBinding binding;

        /** The chat in the card. */
        private ChatRoom mChat;

        /**
         * Constructs the Chat view.
         *
         * @param view the view
         */
        public ChatListViewHolder(View view) {
            super(view);
            mView = view;
            binding = FragmentChatlistCardBinding.bind(view);
        }

        /**
         * Sets each card view for a chat in the recycler view.
         *
         * @param chat the chatroom to setup
         */
        void setChat(final ChatRoom chat) {
            mChat = chat;
            binding.buttonFullChat.setOnClickListener(view ->
                    Navigation.findNavController(mView).navigate
                            (ChatListFragmentDirections.actionNavigationChatToChatFragment(chat.getChatRoomID())));

            binding.textParticipants.setText(chat.participantsAsString());
            binding.textPreview.setText(chat.getLastMessage());
        }
    }
}
