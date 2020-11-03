package edu.uw.tcss450.group6project.ui.contacts;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;

/**
 * A simple {@link Fragment} subclass.
 *
 * @author Robert M.
 * @version 1.0
 */
public class ContactListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_list, container, false);
        if (view instanceof RecyclerView) {
            ((RecyclerView) view).setAdapter(
                    new ContactRecyclerViewAdapter(DummyContacts.getContactList()));
        }
        return view;
    }
}