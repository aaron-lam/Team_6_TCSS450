package edu.uw.tcss450.group6project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentContactListBinding;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;

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
        mModel = new ViewModelProvider(requireActivity()).get(ContactListTabViewModel.class);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mUserModel = provider.get(UserInfoViewModel.class);
        mModel.connectGet(mUserModel.getJWT());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentContactListBinding binding = FragmentContactListBinding.bind(requireView());
        mModel.addContactListObserver(getViewLifecycleOwner(), contactList -> {
            if (!contactList.isEmpty()) {
                List<String> firstNameList = getFirstName(contactList);
                binding.listRoot.setAdapter(new ContactListTabRecyclerViewAdapter(firstNameList));
            }
        });
    }

    /**
     * Extract a list of first names from contact list.
     * @param contactList contact list
     * @return List of first names
     */
    private List<String> getFirstName(List<Contact> contactList) {
        List<String> firstNameList = new ArrayList<>();
        for (Contact contact : contactList) {
            firstNameList.add(contact.getFirstName());
        }
        return firstNameList;
    }
}