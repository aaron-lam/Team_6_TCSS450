package edu.uw.tcss450.group6project.ui.contacts.list_tab;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentContactListTabBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.contacts.Contact;

/**
 * A fragment for displaying the list of contacts.
 *
 * @author Robert M., Aaron L.
 * @version 1.0
 */
public class ContactListTabFragment extends Fragment {

    private ContactListTabViewModel mModel;
    /** Model to store info about the user. */
    private UserInfoViewModel mUserModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mModel = provider.get(ContactListTabViewModel.class);
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel.connectGet(mUserModel.getJWT());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_list_tab, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListTabBinding binding = FragmentContactListTabBinding.bind(requireView());

        // If the list has items, update the recyclerview. If it doesn't, update the recyclerview with an empty list
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                binding.contactsListRoot.setAdapter(new ContactListTabRecyclerViewAdapter(contactList,this,mUserModel));
            } else {
                contactList.clear();
                binding.contactsListRoot.setAdapter(new ContactListTabRecyclerViewAdapter(contactList,this,mUserModel));
            }
        });
    }
}