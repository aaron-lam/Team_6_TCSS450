package edu.uw.tcss450.group6project.ui.chat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatListBinding;
import edu.uw.tcss450.group6project.databinding.FragmentContactListTabBinding;
import edu.uw.tcss450.group6project.model.NewMessageCountViewModel;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.services.PushReceiver;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabFragment;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabRecyclerViewAdapter;

/**
 * A fragment to display a list of chats the user is in.
 *
 * @author Robert M.
 * @version 2 November 2020
 */
public class ChatListFragment extends Fragment {

    private ChatRoomViewModel mChatRoomModel;
    private ChatSendViewModel mChatSendViewModel;
    private NewMessageCountViewModel mNewMessageCountViewModel;
    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;
    private ContactListBroadcastReceiver mPushMessageReceiver;

    private RecyclerView rv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatRoomModel = provider.get(ChatRoomViewModel.class);
        mChatRoomModel.loadChatRooms(mUserModel.getEmail(), mUserModel.getJWT());
        mChatSendViewModel = provider.get(ChatSendViewModel.class);
        mNewMessageCountViewModel = provider.get(NewMessageCountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatListBinding binding = FragmentChatListBinding.bind(view);
        rv = binding.listRoot;
        rv.setAdapter(new ChatListRecyclerViewAdapter(
                this.mChatRoomModel,
                this.mChatSendViewModel,
                this.mNewMessageCountViewModel,
                mUserModel.getJWT(),
                mUserModel.getEmail()));

        //if any of the chats receive messages, update the preview
        mChatRoomModel.addRoomObserver(getViewLifecycleOwner(),
                newMessage -> {
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                });

        binding.buttonCreateChat.setOnClickListener(view1 ->
                Navigation.findNavController(view).navigate(ChatListFragmentDirections.actionNavigationChatToChatCreateFormFragment()));
    }

    /**
     * This updates the list of contacts for the client, even if the user is on the page.
     */
    public void update() {
        mChatRoomModel.loadChatRooms(mUserModel.getEmail(), mUserModel.getJWT());
        FragmentChatListBinding binding = FragmentChatListBinding.bind(requireView());
        // If the list has items, update the recyclerview. If it doesn't, update the recyclerview with an empty list
        mChatRoomModel.addRoomListObserver(getViewLifecycleOwner(), chatMap -> {
            if (!chatMap.isEmpty()) {
                List<ChatRoom> chatRoomList = new ArrayList(chatMap.values());
                binding.listRoot.setAdapter(
                        new ChatListRecyclerViewAdapter(
                                this.mChatRoomModel,
                                this.mChatSendViewModel,
                                this.mNewMessageCountViewModel,
                                chatRoomList,
                                mUserModel.getJWT(),
                                mUserModel.getEmail())
                );
            } else {
                binding.listRoot.setAdapter(
                        new ChatListRecyclerViewAdapter(
                                this.mChatRoomModel,
                                this.mChatSendViewModel,
                                this.mNewMessageCountViewModel,
                                new ArrayList<>(),
                                mUserModel.getJWT(),
                                mUserModel.getEmail())
                );
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new ContactListBroadcastReceiver();
        }
        IntentFilter iFilter = new IntentFilter(PushReceiver.RECEIVED_NEW_MESSAGE);
        getActivity().registerReceiver(mPushMessageReceiver, iFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPushMessageReceiver != null) {
            getActivity().unregisterReceiver(mPushMessageReceiver);
        }
    }

    /**
     * This receives broadcasts, and refreshes the chat room list if it receives one and you're on
     * the chat room page when it happens.
     */
    private class ContactListBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("roomName") || intent.hasExtra("chatMessage")) {
                update();
            }
        }
    }
}