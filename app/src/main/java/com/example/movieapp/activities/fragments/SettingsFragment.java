package com.example.movieapp.activities.fragments;

import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import com.example.movieapp.R;
import com.example.movieapp.activities.activities.SettingsActivity;

public class SettingsFragment extends PreferenceFragmentCompat
        implements Preference.OnPreferenceClickListener {

    private SettingsFragmentInterface settingsFragmentInterface;

    public static SettingsFragment newInstance(String nickname) {
        SettingsFragment settingsFragment = new SettingsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(SettingsActivity.KEY_NICKNAME, nickname);
        settingsFragment.setArguments(bundle);
        return settingsFragment;

    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        setPreferencesFromResource(R.xml.app_preferences, s);


        settingsFragmentInterface = (SettingsFragmentInterface) getActivity();
        Preference signOut = findPreference(getString(R.string.sign_out_setting));
        signOut.setSummary("You are currently logged as " + getArguments().getString(SettingsActivity.KEY_NICKNAME));
        signOut.setOnPreferenceClickListener(this);


    }


    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        switch (key) {
            case "sign_out_setting":
                settingsFragmentInterface.signOut();

        }
        return false;
    }

    public interface SettingsFragmentInterface {
        void signOut();
    }
}
