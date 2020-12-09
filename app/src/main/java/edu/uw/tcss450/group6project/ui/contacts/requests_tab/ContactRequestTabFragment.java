package edu.uw.tcss450.group6project.ui.contacts.requests_tab;

import androidx.lifecycle.ViewModelProvider;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.MainActivity;
import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentContactListTabBinding;
import edu.uw.tcss450.group6project.databinding.FragmentContactRequestTabBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.services.PushReceiver;
import edu.uw.tcss450.group6project.ui.chat.ChatMessage;
import edu.uw.tcss450.group6project.ui.chat.ChatRoomViewModel;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabRecyclerViewAdapter;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabViewModel;

public class ContactRequestTabFragment extends Fragment {

    private ContactRequestTabViewModel mModel;
    private UserInfoViewModel mUserModel;
    private ContactRequestBroadcastReceiver mPushMessageReceiver;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mModel = provider.get(ContactRequestTabViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel.connectGet(mUserModel.getJWT());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_request_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Update the list
        update();
    }

    public void update() {
        mModel.connectGet(mUserModel.getJWT());
        FragmentContactRequestTabBinding binding = FragmentContactRequestTabBinding.bind(requireView());

        // If the list has items, update the recyclerview. If it doesn't, update the recyclerview with an empty list
        mModel.addContactRequestListObserver(getViewLifecycleOwner(), contactRequestList -> {
            if (!contactRequestList.isEmpty()) {
                binding.contactRequestsListRoot.setAdapter(new ContactRequestTabRecyclerViewAdapter(contactRequestList,this,mUserModel));
            } else {
                contactRequestList.clear();
                binding.contactRequestsListRoot.setAdapter(new ContactRequestTabRecyclerViewAdapter(contactRequestList,this,mUserModel));
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
        if (mPushMessageReceiver == null) {
            mPushMessageReceiver = new ContactRequestBroadcastReceiver();
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

    private class ContactRequestBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NavController nc =
                    Navigation.findNavController(
                            getActivity(), R.id.nav_host_fragment);
            NavDestination nd = nc.getCurrentDestination();

            // If it's a message for the contacts page...
            if (!(intent.hasExtra("chatMessage") || intent.hasExtra("roomName"))) {
                update();
            }
        }
    }
}