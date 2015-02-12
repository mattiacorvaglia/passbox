package com.mcdev.passbox.ui;

import com.mcdev.passbox.R;
import com.mcdev.passbox.utils.Constants;
import com.mcdev.passbox.views.ScrimInsetsFrameLayout;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends ActionBarActivity {
	
	private DrawerLayout mDrawerLayout;
	private ScrimInsetsFrameLayout mDrawer;
	private ActionBarHelper mActionBar;
	private ActionBarDrawerToggle mDrawerToggle;
	private Context mContext;

	private String[] titles = new String[] {
		"Raccolta",
		"Impostazioni"
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = this;
		
		/*****************************************************
         ** Toolbar											**
         ** Comment the following lines to use the classic	**
         ** ActionBar instead								**
         *****************************************************/
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Find Views
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (ScrimInsetsFrameLayout) findViewById(R.id.scrimInsetsFrameLayout);
//        mContent = (TextView) findViewById(R.id.content_text);
        
        /*****************************************************
         ** Navigation Drawer								** 
         *****************************************************/
        // Set the status bar background color
        mDrawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.light_blue_700));
        
        // 
        mDrawerLayout.setDrawerListener(new DemoDrawerListener());
        
        //
        mDrawerLayout.setDrawerTitle(GravityCompat.START, getString(R.string.drawer_title));
        
        // Inflate the drawer list
        ListView mDrawerList = (ListView) findViewById(R.id.drawer_list);
		
        // Set the Adapter
        List<String> strings = Arrays.asList(titles);
        mDrawerList.setAdapter(new DrawerListAdapter(mContext, strings));

        // Set the ItemClickListener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		
        /*****************************************************
         ** ActionBar										** 
         *****************************************************/
        // 
        mActionBar = createActionBarHelper();
        mActionBar.init();
		
        // 
        mDrawerToggle = new ActionBarDrawerToggle(
        	this,
        	mDrawerLayout,
        	R.string.drawer_open,
        	R.string.drawer_close
        );
        
        // Set the startup fragment to show
        Fragment collectionFragment = new CollectionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                       .replace(R.id.container, collectionFragment)
                       .commit();
		
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
        * The action bar home/up action should open or close the drawer.
        * mDrawerToggle will take care of this.
        */
       if (mDrawerToggle.onOptionsItemSelected(item)) {
           return true;
       }
       return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
				case Constants.REQUEST_CODE_ADD_PASSWORD:
					// Update UI in the password collection fragment
					CollectionFragment mFragmentOnInsert = (CollectionFragment) getSupportFragmentManager().findFragmentById(R.id.container);
					mFragmentOnInsert.updateUI();
					break;
				case Constants.REQUEST_CODE_DETAIL_PASSWORD:
					// Update UI in the password collection fragment
					CollectionFragment mFragmentOnUpdateOrDelete = (CollectionFragment) getSupportFragmentManager().findFragmentById(R.id.container);
					mFragmentOnUpdateOrDelete.updateUI();
					break;
				default:
					break;
			}
		}
	}
	
	/**
     * This list item click listener implements very simple view switching by changing
     * the primary content text. The drawer is closed when a selection is made.
     */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // Mark the selected item as selected in the Adapter
            ((DrawerListAdapter) parent.getAdapter()).selectItem(position);
            
        	// Handle the Drawer
            mActionBar.setTitle(titles[position]);
            mDrawerLayout.closeDrawer(mDrawer);
            
            // Create the right Fragment
        	selectItem(position);
        	
        }
    }
    
    /**
     * Swaps fragments in the main content view
     */
    private void selectItem(int position) {
    	
    	FragmentManager fragmentManager = getSupportFragmentManager();
    	
    	switch (position) {
			case 0:
				// Create the new fragment
				Fragment collectionFragment = new CollectionFragment();
//		        Bundle args = new Bundle();
//		        args.putInt(CollectionFragment.ARG_FRAGMENT_POSITION, position);
//		        fragment.setArguments(args);

		        // Insert the fragment by replacing any existing fragment
		        fragmentManager.beginTransaction()
		                       .replace(R.id.container, collectionFragment)
		                       .commit();

		        // Highlight the selected item
//		        mDrawerList.setItemChecked(position, true);
				break;
			case 1:
	//			// Create the new fragment
				Fragment preferenceFragment = new PreferenceFragment();
//		        Bundle args = new Bundle();
//		        args.putInt(CollectionFragment.ARG_FRAGMENT_POSITION, position);
//		        fragment.setArguments(args);

		        // Insert the fragment by replacing any existing fragment
		        fragmentManager.beginTransaction()
		                       .replace(R.id.container, preferenceFragment)
		                       .commit();

		        // Highlight the selected item
//		        mDrawerList.setItemChecked(position, true);
				break;

			default:
				break;
		}
        
    }
	
	/**
     * A drawer listener can be used to respond to drawer events such as becoming
     * fully opened or closed. You should always prefer to perform expensive operations
     * such as drastic relayout when no animation is currently in progress, either before
     * or after the drawer animates.
     *
     * When using ActionBarDrawerToggle, all DrawerLayout listener methods should be forwarded
     * if the ActionBarDrawerToggle is not used as the DrawerLayout listener directly.
     */
    private class DemoDrawerListener implements DrawerLayout.DrawerListener {
        @Override
        public void onDrawerOpened(View drawerView) {
            mDrawerToggle.onDrawerOpened(drawerView);
            mActionBar.onDrawerOpened();
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            mDrawerToggle.onDrawerClosed(drawerView);
            mActionBar.onDrawerClosed();
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            mDrawerToggle.onDrawerSlide(drawerView, slideOffset);
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            mDrawerToggle.onDrawerStateChanged(newState);
        }
    }
    
    /**
     * Create a compatible helper that
     * will manipulate the action bar if available.
     */
    private ActionBarHelper createActionBarHelper() {
        return new ActionBarHelper();
    }
	
	/**
     * Action bar helper for use on ICS and newer devices.
     */
    private class ActionBarHelper {
        private final ActionBar mActionBar;
        private CharSequence mDrawerTitle;
        private CharSequence mTitle;

        ActionBarHelper() {
            mActionBar = getSupportActionBar();
        }

        public void init() {
            mActionBar.setDisplayHomeAsUpEnabled(true);
            mActionBar.setDisplayShowHomeEnabled(false);
            mTitle = mDrawerTitle = getTitle();
        }

        /**
         * When the drawer is closed we restore the action bar state reflecting
         * the specific contents in view.
         */
        public void onDrawerClosed() {
            mActionBar.setTitle(mTitle);
        }

        /**
         * When the drawer is open we set the action bar to a generic title.
         * The action bar should only contain data relevant at the top level of
         * the nav hierarchy represented by the drawer, as the rest of your content
         * will be dimmed down and non-interactive.
         */
        public void onDrawerOpened() {
            mActionBar.setTitle(mDrawerTitle);
        }

        public void setTitle(CharSequence title) {
            mTitle = title;
        }
    }

    /**
     * Custom ListAdapter for the drawer
     */
    private class DrawerListAdapter extends ArrayAdapter<String> {

        private int selectedItem;
        private Typeface fontMedium;

        // Constructor
        public DrawerListAdapter(Context context, List<String> objects) {
            super(context, R.layout.item_drawer_list, objects);
            fontMedium = Typeface.createFromAsset(context.getAssets(),"Roboto-Medium.ttf");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Holder mHolder;
            // Inflating
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.item_drawer_list, parent, false);
                mHolder = new Holder();
                mHolder.icon = (ImageView) convertView.findViewById(R.id.item_drawer_icon);
                mHolder.title = (TextView) convertView.findViewById(R.id.item_drawer_title);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Holder) convertView.getTag();
            }

            // Set dynamic contents
            switch (position) {
                case 0:
                    mHolder.title.setText(getItem(0));
                    mHolder.title.setTypeface(fontMedium);
                    if (position == selectedItem) {
                        mHolder.title.setTextColor(getContext().getResources().getColor(R.color.light_blue_700));
                        mHolder.icon.setImageResource(R.drawable.ic_label_grey600_24dp);
                    } else {
                        mHolder.title.setTextColor(getContext().getResources().getColor(R.color.grey_600));
                        mHolder.icon.setImageResource(R.drawable.ic_label_grey600_24dp);
                    }
                    break;
                case 1:
                    mHolder.title.setText(getItem(1));
                    mHolder.title.setTypeface(fontMedium);
                    if (position == selectedItem) {
                        mHolder.title.setTextColor(getContext().getResources().getColor(R.color.light_blue_700));
                        mHolder.icon.setImageResource(R.drawable.ic_send_grey600_24dp);
                    } else {
                        mHolder.title.setTextColor(getContext().getResources().getColor(R.color.grey_600));
                        mHolder.icon.setImageResource(R.drawable.ic_send_grey600_24dp);
                    }
                    break;
                default:
                    break;
            }
            
            
            // The End
            return convertView;
        }

        /**
         * Mark an item as selected
         * @param selectedItem the item to mark as selected
         */
        public void selectItem(int selectedItem) {
            this.selectedItem = selectedItem;
            notifyDataSetChanged();
        }

    }

    /**
     * Util class used to reuse the views in the ListView
     * after scroll down
     */
    private static class Holder {
        private ImageView icon;
        private TextView title;
    }
    
}
