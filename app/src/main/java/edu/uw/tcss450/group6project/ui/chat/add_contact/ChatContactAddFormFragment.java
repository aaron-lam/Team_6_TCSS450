package edu.uw.tcss450.group6project.ui.chat.add_contact;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentChatAddContactFormBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.chat.ChatRoom;
import edu.uw.tcss450.group6project.ui.chat.ChatRoomViewModel;
import edu.uw.tcss450.group6project.ui.chat.create.ChatCreateFormRecyclerViewAdapter;
import edu.uw.tcss450.group6project.ui.chat.create.ChatCreateFormViewModel;

/**
 * A fragment to display a chat room create form
 *
 * @author Aaron L.
 * @version 23 November 2020
 */
public class ChatContactAddFormFragment extends Fragment {

    private ChatCreateFormViewModel mModel;
    private FragmentChatAddContactFormBinding mBinding;
    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;
    private ChatRoomViewModel mChatRoomModel;
    private Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = new ViewModelProvider(requireActivity()).get(ChatCreateFormViewModel.class);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mChatRoomModel = provider.get(ChatRoomViewModel.class);
        mModel.connectGet(mUserModel.getJWT());
        mContext = this.getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentChatAddContactFormBinding.inflate(inflater, container, false);
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
        mBinding.buttonCreateChatForm.setOnClickListener(this::addNewContacts);
    }

    /**
     * adding new contacts to an existed chat room.
     * @param view view
     */
    private void addNewContacts(View view) {
        assert getArguments() != null;
        ChatContactAddFormFragmentArgs args = ChatContactAddFormFragmentArgs.fromBundle(getArguments());
        int roomId = args.getRoomId();
        mModel.addNewContactsToRoom(mUserModel.getJWT(), roomId, this);
    }

    public void handleAddNewContactsToRoomError(Object message, int length) {
        Toast.makeText(mContext, (CharSequence) message, length).show();
    }

    /**
     * Callback of adding new contacts to an existed chat room.
     */
    public void addNewContactsToRoomCallback() {
        Toast.makeText(mContext, R.string.toast_add_contact_existed_room_success, Toast.LENGTH_SHORT).show();
        mChatRoomModel.loadChatRooms(mUserModel.getEmail(), mUserModel.getJWT());
        requireActivity().onBackPressed();
    }
}
