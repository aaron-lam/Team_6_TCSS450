package edu.uw.tcss450.group6project.ui.contacts;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.model.UserInfoViewModel;
import edu.uw.tcss450.group6project.ui.contacts.add_contact.AddContactDialog;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabFragment;
import edu.uw.tcss450.group6project.ui.contacts.list_tab.ContactListTabViewModel;
import edu.uw.tcss450.group6project.ui.contacts.requests_tab.ContactRequestTabFragment;
import edu.uw.tcss450.group6project.ui.contacts.requests_tab.ContactRequestTabViewModel;

/** The main page of contacts, which contains the other tabs (list and requests)
 *
 * @author chasealder
 */
public class ContactsFragment extends Fragment {

    AddContactDialog mAddContactDialog;
    UserInfoViewModel mUserInfoViewModel;
    ContactListTabViewModel mContactListTabViewModel;
    ContactRequestTabViewModel mContactRequestTabViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddContactDialog = new AddContactDialog(this);
        ViewModelProvider provider = new ViewModelProvider(getActivity());
        mContactListTabViewModel = provider.get(ContactListTabViewModel.class);
        mUserInfoViewModel = provider.get(UserInfoViewModel.class);
        mContactRequestTabViewModel = provider.get(ContactRequestTabViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_contacts, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createContactsTab(view);
    }

    /** Adds the ViewPager and TabLayout (tabs) to the contacts fragment
     *
     * @param view the current view
     */
    private void createContactsTab(View view) {

        String[] tabNames = {"Contacts","Requests"};

        ViewPager2 viewPager = view.findViewById(R.id.contacts_view_pager);
        viewPager.setAdapter(new ContactsAdapter(this));

        // Update both lists (contacts & contact requests) whenever they are brought into focus
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                if (position == 0) {
                    mContactListTabViewModel.connectGet(mUserInfoViewModel.getJWT());
                } else {
                    mContactRequestTabViewModel.connectGet(mUserInfoViewModel.getJWT());
                }
            }
        });

        TabLayout tabLayout = view.findViewById(R.id.contacts_tab_layout);
        tabLayout.setTabMode(TabLayout.MODE_AUTO);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(tabNames[position]);
        }).attach();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.top_contact_list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.button_contacts_menu_add:
                mAddContactDialog.buildAddContactDialog();
        }

        return super.onOptionsItemSelected(item);
    }

    /** Used to determine which fragment to show based on the tab position
     *
     * @author chasealder
     */
    private class ContactsAdapter extends FragmentStateAdapter {

        public ContactsAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            if (position == 0) {
                return new ContactListTabFragment();
            } else {
                return new ContactRequestTabFragment();
            }
        }

        // There are two options, contacts or contact requests
        @Override
        public int getItemCount() {
            return 2;
        }
    }
}