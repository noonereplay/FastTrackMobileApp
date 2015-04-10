package com.kmutnb.fasttrack;


import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class HomeSpeed extends Activity {
	/*
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_speed);
	}*/
	
	private String[] drawerListViewItems;
    private ListView drawerListView;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private Fragment profileFragment;
    private Fragment speedFragment;

    FragmentManager fragmentManager = getFragmentManager();
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	// TODO Auto-generated method stub
    	super.onCreate(savedInstanceState);
    	setContentView(R.layout.activity_home_speed);

//        // get list items from strings.xml
        drawerListViewItems = getResources().getStringArray(R.array.list_menu);
// 
//        // get ListView defined in activity_main.xml
        drawerListView = (ListView) findViewById(R.id.drawer);
 
//                // Set the adapter for the list view
        drawerListView.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_listview_item, drawerListViewItems));
       
        // 2. App Icon
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
 
        // 2.1 create ActionBarDrawerToggle
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                drawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description */
                R.string.drawer_close  /* "close drawer" description */
                );
 
//        // 2.2 Set actionBarDrawerToggle as the DrawerListener
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
// 
//        // 2.3 enable and show "up" arrow
        getActionBar().setDisplayHomeAsUpEnabled(true);
// 
//        // just styling option
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
// 
        drawerListView.setOnItemClickListener(new DrawerItemClickListener());
        
        Bundle bundle = new Bundle();
        bundle.putString("username", this.getIntent().getStringExtra("username"));
        bundle.putString("profile_id", this.getIntent().getStringExtra("profile_id"));
        
        profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        speedFragment = new SpeedFragment();
        speedFragment.setArguments(bundle);
        
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_layout_home, speedFragment).commit();
        
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
         actionBarDrawerToggle.syncState();
    }
 
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }
 
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
 
         // call ActionBarDrawerToggle.onOptionsItemSelected(), if it returns true
        // then it has handled the app icon touch event
 
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
 
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

    	

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
			// TODO Auto-generated method stub
			Toast.makeText(HomeSpeed.this, ((TextView)view).getText()+" : "+position, Toast.LENGTH_LONG).show();
			drawerLayout.closeDrawer(drawerListView);
			switch (position) {
			case 0:
				fragmentManager.beginTransaction().replace(R.id.content_layout_home, speedFragment).commit();
				break;
			case 1:
				fragmentManager.beginTransaction().replace(R.id.content_layout_home, profileFragment).commit();
				break;
			case 2:
				//Intent main_intent = new Intent(HomeSpeed.this, MainActivity.class);
				//startActivity(main_intent);
				HomeSpeed.this.finish();
				break;
			case 3:
				AppExit();
				break;

			default:
				break;
			}
			
		}
    }
    
    public void AppExit()
    {

        this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        int pid = android.os.Process.myPid();//=====> use this if you want to kill your activity. But its not a good one to do.
        android.os.Process.killProcess(pid);

    }
    


}
