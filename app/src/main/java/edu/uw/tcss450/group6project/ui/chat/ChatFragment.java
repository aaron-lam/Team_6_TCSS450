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


    private int mChatRoomID;
    private ChatFragmentArgs mArgs;
    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;
    /** Model to store info about chatrooms the user is in*/
    private ChatRoomViewModel mChatRoomViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArgs = ChatFragmentArgs.fromBundle(getArguments());
        mChatRoomID = mArgs.getChat().getChatRoomID();

        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatRoomViewModel = provider.get(ChatRoomViewModel.class);
        mChatRoomViewModel.getFirstMessages(mChatRoomID, mUserModel.getJWT());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentChatBinding binding = FragmentChatBinding.bind(getView());

        //Add the chat messages to the recycler view
        final RecyclerView rv = binding.listRoot;
        rv.setAdapter(new ChatRecyclerViewAdapter(mArgs.getChat().getMessages(), mUserModel.getEmail()));
    }
}