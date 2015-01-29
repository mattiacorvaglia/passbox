package com.mcdev.passbox.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcdev.passbox.R;

public class PreferenceFragment extends Fragment {
	
//	public static final String ARG_FRAGMENT_POSITION = "fragmentPos";
//	private static final String ARG_FRAGMENT_TITLE = "Impostazioni";
	
	// Constructor
	public PreferenceFragment() {}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_preference, container, false);
//        int i = getArguments().getInt(ARG_FRAGMENT_POSITION);
//        getActivity().setTitle(ARG_FRAGMENT_TITLE);
        
        return rootView;
        
	}
}
