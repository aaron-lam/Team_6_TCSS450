package edu.uw.tcss450.group6project.ui.chat;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.uw.tcss450.group6project.R;

/**
 * A fragment to display a single chat between users.
 *
 * @author Robert M
 * @version 3 November 2020
 */
public class ChatFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        ChatFragmentArgs args = ChatFragmentArgs.fromBundle(getArguments());

        if (view instanceof RecyclerView) {
            ((RecyclerView) view).setAdapter(new ChatRecyclerViewAdapter(args.getChat().getMessages()));
        }

        return view;
    }
}