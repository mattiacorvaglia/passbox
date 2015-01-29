package com.mcdev.passbox.ui;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import com.mcdev.passbox.R;
import com.mcdev.passbox.content.PasswordDao;
import com.mcdev.passbox.content.PasswordDto;
import com.mcdev.passbox.utils.CommonResources;
import com.mcdev.passbox.utils.FloatingActionButton;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class CollectionFragment extends ListFragment {
	
	public static final int REQUEST_CODE_ADD_PASSWORD = 123;
	public static final int REQUEST_CODE_DETAIL_PASSWORD = 124;
	
	private Context mContext;
	private TextView emptyAlert;
	
	// Constructor
	public CollectionFragment() {}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		mContext = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.fragment_collection, container, false);
		
		// Inflate widgets
		emptyAlert = (TextView) rootView.findViewById(R.id.empty_markbook);
		FloatingActionButton addButton = (FloatingActionButton) rootView.findViewById(R.id.action_add_new);
        
		// Add onClickListener to the add button
		addButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent mIntent = new Intent(getActivity(), AddPasswordActivity.class);
				getActivity().startActivityForResult(mIntent, REQUEST_CODE_ADD_PASSWORD);
			}
		});
        return rootView;
        
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		// Update UI
		updateUI();
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent mIntent = new Intent(getActivity(), PasswordDetailActivity.class);
		PasswordDto mPassword = (PasswordDto) l.getItemAtPosition(position);
		mIntent.putExtra(CommonResources.TAG_EXTRA_PASSWORD_ID, mPassword.getId());
		mIntent.putExtra(CommonResources.TAG_EXTRA_PASSWORD_TITLE, mPassword.getTitle());
		mIntent.putExtra(CommonResources.TAG_EXTRA_PASSWORD_COLOR, mPassword.getColor());
		getActivity().startActivityForResult(mIntent, REQUEST_CODE_DETAIL_PASSWORD);
	}
	
	/**
	 * Update the UI with the contents retrieved
	 * from the database
	 */
	protected void updateUI() {
		
		PasswordDao.getInstance(mContext).open();
		LinkedList<PasswordDto> pwds = PasswordDao.getInstance(mContext).getAllPasswordsLite();
		PasswordDao.getInstance(mContext).close();
		
		if (pwds == null) {
			emptyAlert.setVisibility(View.VISIBLE);
		} else {
			emptyAlert.setVisibility(View.GONE);
			PasswordAdapter adapter = new PasswordAdapter(mContext, pwds);
			setListAdapter(adapter);
		}
	}
	
	/**
	 * Custom adapter for the list
	 */
	private class PasswordAdapter extends ArrayAdapter<PasswordDto> {

		// Constructor
		public PasswordAdapter(Context context, List<PasswordDto> objects) {
			super(context, R.layout.item_password, objects);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder mHolder;
			// Inflating
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.item_password, parent, false);
				mHolder = new Holder();
				mHolder.iconBackground = (LinearLayout) convertView.findViewById(R.id.pwd_icon_background);
				mHolder.icon = (TextView) convertView.findViewById(R.id.pwd_icon);
				mHolder.title = (TextView) convertView.findViewById(R.id.pwd_title);
				convertView.setTag(mHolder);
			} else {
				mHolder = (Holder) convertView.getTag();
			}
			// Get current item
			PasswordDto mPassword = getItem(position);
			String title = mPassword.getTitle();
			if (title != null && title.length() > 0) {
				mHolder.icon.setText(title.substring(0, 1).toUpperCase(Locale.ITALIAN));
				mHolder.title.setText(title);
			}
			// Get current color
			String color = mPassword.getColor();
			mHolder.iconBackground.getBackground().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_ATOP);
			
			// The End
			return convertView;
		}
		
	}
	
	/**
	 * Util class used to reuse the views in the ListView
	 * after scroll down
	 */
	private static class Holder {
		private LinearLayout iconBackground;
		private TextView icon;
		private TextView title;
	}

}
