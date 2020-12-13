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

import java.util.List;

import edu.uw.tcss450.group6project.MainActivity;
import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatBinding;
import edu.uw.tcss450.group6project.model.NewMessageCountViewModel;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.services.PushReceiver;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabFragment;

/**
 * A fragment to display a single chat between users.
 *
 * @author Robert M
 * @version 3 November 2020
 */
public class ChatFragment extends Fragment {

    /** ID of the current chat room*/
    private int mChatRoomID;

    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;
    /** Model to store info about chatrooms the user is in*/
    private ChatRoomViewModel mChatRoomViewModel;

    private ChatSendViewModel mSendModel;
    private NewMessageCountViewModel mNewMessageModel;

    private ChatRoomReceiver mPushMessageReceiver;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());
        mChatRoomID = args.getChatRoomID();

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mSendModel = provider.get(ChatSendViewModel.class);
        mChatRoomViewModel = provider.get(ChatRoomViewModel.class);
        mChatRoomViewModel.getFirstMessages(mChatRoomID, mUserModel.getJWT());
        mNewMessageModel = provider.get(NewMessageCountViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(view);

        //Send the chat messages to the recycler view
        final RecyclerView rv = binding.listRoot;
        rv.setAdapter(new ChatRecyclerViewAdapter(mChatRoomViewModel.getMessageListByChatId(mChatRoomID),
                mUserModel.getUsername()));

        //When the user scrolls to the top of the RV, the swiper list will "refresh"
        //The user is out of messages, go out to the service and get more
        binding.swipeContainer.setOnRefreshListener(() -> {
            mChatRoomViewModel.getNextMessages(mChatRoomID, mUserModel.getJWT());
        });

        mChatRoomViewModel.addMessageObserver(mChatRoomID, getViewLifecycleOwner(),
                list -> {
                    /*
                     * This solution needs work on the scroll position. As a group,
                     * you will need to come up with some solution to manage the
                     * recyclerview scroll position. You also should consider a
                     * solution for when the keyboard is on the screen.
                     */
                    //inform the RV that the underlying list has (possibly) changed
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                    binding.swipeContainer.setRefreshing(false);
                });

        //Send button was clicked. Send the message via the SendViewModel
        binding.buttonSend.setOnClickListener(button -> {
            mSendModel.sendMessage(mChatRoomID,
                    mUserModel.getJWT(),
                    binding.editMessage.getText().toString());
        });
        //when we get the response back from the server, clear the edittext
        mSendModel.addResponseObserver(getViewLifecycleOwner(), response ->
                binding.editMessage.setText(""));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new ChatRoomReceiver();
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
     * A BroadcastReceiver that listens for messages sent from PushReceiver
     */
    private class ChatRoomReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            getActivity(), R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();
            if (intent.hasExtra("chatMessage")) {

                int chatRoomID = intent.getIntExtra("chatid", -1);
                //If the user is not on the chat screen, update the
                // NewMessageCountView Model
                Log.d("Push Notification Chat Message", Integer.toString(chatRoomID));
                Log.d("Current Chat Room ID", Integer.toString(mChatRoomID));
                if (mChatRoomID != chatRoomID) {
                    mNewMessageModel.increment();
                }
                List<ChatMessage> chatMessageList = mChatRoomViewModel.getMessageListByChatId(chatRoomID);
                chatMessageList.get(chatMessageList.size() - 1).setIsRead(true);
            }

        }
    }
}