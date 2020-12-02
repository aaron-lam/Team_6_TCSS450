package edu.uw.tcss450.group6project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.ui.contacts.list.ContactListTabFragment;
import edu.uw.tcss450.group6project.ui.contacts.requests.ContactRequestTabFragment;

/** The main page of contacts, which contains the other tabs
 *
 */
public class ContactsFragment extends Fragment {

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createContactsTab(view);
    }

    private void createContactsTab(View view) {

        String[] tabNames = {"Contacts","Search","Requests"};

        ViewPager2 viewPager = view.findViewById(R.id.contacts_view_pager);
        viewPager.setAdapter(new ContactsAdapter(this));

        TabLayout tabLayout = view.findViewById(R.id.contacts_tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_AUTO);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabNames[position]);
        }).attach();
    }

    private class ContactsAdapter extends FragmentStateAdapter {

        public ContactsAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            if (position == 0) {
                return new ContactListTabFragment();
            } else if (position == 1) {
                return new ContactListTabFragment();
            } else {
                return new ContactRequestTabFragment();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }
}