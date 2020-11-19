package edu.uw.tcss450.group6project.ui.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import edu.uw.tcss450.group6project.R;
import edu.uw.tcss450.group6project.databinding.FragmentSettingsHomeBinding;

/** The settings page
 *
 * @author Chase Alder
 */
public class SettingsHomeFragment extends Fragment {

    private FragmentSettingsHomeBinding mBinding;
    SharedPreferences sp;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentSettingsHomeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sp = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
        int spTheme = sp.getInt("theme",0);

        if (spTheme == R.style.ThemeOne) {
            mBinding.radioButtonSettingsThemeOne.setChecked(true);
        } else if (spTheme == R.style.ThemeTwo) {
            mBinding.radioButtonSettingsThemeTwo.setChecked(true);
        } else {
            mBinding.radioButtonSettingsThemeThree.setChecked(true);
        }

        mBinding.buttonSettingsChangePassword.setOnClickListener(button -> {
            Navigation.findNavController(getView()).navigate(SettingsHomeFragmentDirections.actionSettingsFragmentToChangePasswordFragment());
        });

        mBinding.radioButtonSettingsThemeOne.setOnClickListener(button -> {
            sp = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("theme",R.style.ThemeOne);
            editor.commit();
            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
        });

        mBinding.radioButtonSettingsThemeTwo.setOnClickListener(button -> {
            sp = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("theme",R.style.ThemeTwo);
            editor.commit();
            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
        });

        mBinding.radioButtonSettingsThemeThree.setOnClickListener(button -> {
            sp = getActivity().getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putInt("theme",R.style.ThemeThree);
            editor.commit();
            getActivity().finish();
            getActivity().startActivity(getActivity().getIntent());
        });
    }

}
