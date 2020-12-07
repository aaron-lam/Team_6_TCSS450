package edu.uw.tcss450.group6project.ui.chat;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatlistCardBinding;

/**
 * A RecyclerViewAdapter to create scrolling list view of chats.
 *
 * @author Robert M, Aaron L
 * @version 2 November 2020
 */
public class ChatListRecyclerViewAdapter extends
        RecyclerView.Adapter<ChatListRecyclerViewAdapter.ChatListViewHolder> {

    private ChatRoomViewModel chatRoomViewModel;

    /**
     * A list of chats.
     */
    private final List<ChatRoom> mChats;

    /**
     * User jwt token.
     */
    private final String jwt;

    /**
     * User email.
     */
    private final String email;

    /**
     * The fragment's context.
     */
    private Context mContext;

    /**
     * Parameterized constructor method taking a list of chats.
     *
     * @param chatRoomViewModel chat room view model
     * @param jwt user jwt
     * @param email user email
     */
    public ChatListRecyclerViewAdapter(ChatRoomViewModel chatRoomViewModel, final String jwt, final String email) {
        this.chatRoomViewModel = chatRoomViewModel;
        this.mChats = chatRoomViewModel.getChatRooms();
        this.jwt = jwt;
        this.email = email;
    }

    @NonNull
    @Override
    public ChatListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
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

    public void removeItem(int position) {
        this.mChats.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mChats.size());
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
            int chatRoomId = chat.getChatRoomID();
            mChat = chat;
            binding.buttonFullChat.setOnClickListener(view ->
                    Navigation.findNavController(mView).navigate
                            (ChatListFragmentDirections.actionNavigationChatToChatFragment(chatRoomId)));
            binding.buttonDeleteChat.setOnClickListener(view -> {
                chatRoomViewModel.deleteChatRoom(jwt, chat.getChatRoomID(), email, this);
            });
            binding.buttonAddContact.setOnClickListener(view -> {
                Navigation.findNavController(view)
                        .navigate(ChatListFragmentDirections
                                .actionNavigationChatToChatContactAddFormFragment(chatRoomId));
            });
            binding.textParticipants.setText(chat.participantsAsString());
            binding.textPreview.setText(chat.getLastMessage());
        }

        public void deleteChatCallback() {
            removeItem(this.getAdapterPosition());
            Toast.makeText(mContext, R.string.chat_delete, Toast.LENGTH_SHORT).show();
        }
    }
}
