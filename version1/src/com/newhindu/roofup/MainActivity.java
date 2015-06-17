package com.newhindu.roofup;


import android.os.Bundle;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

 
public class MainActivity extends FragmentActivity {
    ViewPager Tab;
    TabPagerAdapter TabAdapter;
    ActionBar actionBar;
 
    @SuppressWarnings("deprecation")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 
        TabAdapter = new TabPagerAdapter(getSupportFragmentManager());
 
        Tab = (ViewPager)findViewById(R.id.pager);
        Tab.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
 
                        actionBar = getActionBar();
                        actionBar.setSelectedNavigationItem(position);                    }
                });
        Tab.setAdapter(TabAdapter);
 
        actionBar = getActionBar();
        //Enable Tabs on Action Bar
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        ActionBar.TabListener tabListener = new ActionBar.TabListener(){
 
            @Override
            public void onTabReselected(android.app.ActionBar.Tab tab,
                    FragmentTransaction ft) {
                // TODO Auto-generated method stub
 
            }
 
            @Override
             public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
 
                Tab.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(android.app.ActionBar.Tab tab,
                    FragmentTransaction ft) {
                // TODO Auto-generated method stub
 
            }};
            //Add New Tab
            actionBar.addTab(actionBar.newTab().setText("Map View").setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab().setText("List View").setTabListener(tabListener));
            
 
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
        	finish();
        	System.exit(0);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

 
}