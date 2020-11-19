package edu.uw.tcss450.group6project.ui.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;

/**
 * A fragment to display a list of chats the user is in.
 *
 * @author Robert M.
 * @version 2 November 2020
 */
public class ChatListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat_list, container, false);
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).setAdapter(
                    new ChatListRecyclerViewAdapter(ChatGenerator.getChatList()));
        }
        return view;
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