package edu.uw.tcss450.group6project.ui.chat.create;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatCreateFormBinding;
import edu.uw.tcss450.group6project.databinding.FragmentSignInBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.contacts.Contact;

/**
 * A fragment to display a chat room create form
 *
 * @author Aaron L.
 * @version 23 November 2020
 */
public class ChatCreateFormFragment extends Fragment {

    private ChatCreateFormViewModel mModel;
    private FragmentChatCreateFormBinding mBinding;
    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(requireActivity()).get(ChatCreateFormViewModel.class);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel.connectGet(mUserModel.getJWT());
        mContext = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentChatCreateFormBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mModel.addChatCreateFormObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                mBinding.listRoot.setAdapter(new ChatCreateFormRecyclerViewAdapter(contactList, mModel));
            }
        });
        mBinding.buttonCreateChatForm.setOnClickListener(this::createNewChatRoom);
    }

    /**
     * Create a new chat room.
     * @param view view
     */
    private void createNewChatRoom(View view) {
        String roomName = mBinding.editTextRoomName.getText().toString();
        if (roomName.isEmpty()) {
            mBinding.editTextRoomName.setError(getString(R.string.error_room_name_empty));
        }
        else {
            mModel.connectPost(mUserModel.getJWT(), roomName, this);
        }
    }

    /**
     * Callback of creating new chat room.
     */
    public void createNewChatRoomCallback() {
        Toast.makeText(mContext, R.string.toast_create_room_success, Toast.LENGTH_SHORT).show();
        // go to previous fragment
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        }
    }
}
