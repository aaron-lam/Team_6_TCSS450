package edu.uw.tcss450.group6project.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;

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
                mUserModel.getEmail()));

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
}