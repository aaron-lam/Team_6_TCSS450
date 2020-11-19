package edu.uw.tcss450.group6project.ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentHomeBinding;
import edu.uw.tcss450.group6project.ui.chat.ChatRoom;
import edu.uw.tcss450.group6project.ui.chat.ChatGenerator;

/**
 * A fragment for displaying the home landing page
 *
 * @author Anthony W
 * @version 1.0
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRecentChat();
    }

    /**
     * Temporary for Design purposes
     * Displays the 3 most recent chats
     */
    private void setRecentChat() {
        FragmentHomeBinding binding = FragmentHomeBinding.bind(getView());
        List<ChatRoom> chats = ChatGenerator.getChatList();
        TextView[] recentParticipants = {binding.textParticipant1, binding.textParticipant2, binding.textParticipant3};
        TextView[] recentMessages = {binding.textMessage1, binding.textMessage2, binding.textMessage3};
        for(int i = 0; i < 3; i++) {
            recentParticipants[i].setText(chats.get(i).getParticipants().toString());
            recentMessages[i].setText(chats.get(i).getLastMessage());
        }
    }
}