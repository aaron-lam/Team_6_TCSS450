package edu.uw.tcss450.group6project.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatListBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;

/**
 * A fragment to display a list of chats the user is in.
 *
 * @author Robert M.
 * @version 2 November 2020
 */
public class ChatListFragment extends Fragment {

    private ChatRoomViewModel mChatRoomModel;
    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatRoomModel = provider.get(ChatRoomViewModel.class);
        mChatRoomModel.loadChatRooms(mUserModel.getEmail(), mUserModel.getJWT());
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
        final RecyclerView rv = binding.listRoot;
        rv.setAdapter(new ChatListRecyclerViewAdapter(mChatRoomModel.getChatRooms()));

        //if any of the chats receive messages, update the preview
        mChatRoomModel.addRoomObserver(getViewLifecycleOwner(),
                newMessage -> {
                    rv.getAdapter().notifyDataSetChanged();
                    rv.scrollToPosition(rv.getAdapter().getItemCount() - 1);
                });
    }
}