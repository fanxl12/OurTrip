package com.fanxl.ourtrip;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity {
	
	private ViewPager main_vp;
	private List<Fragment> views;
	private FragmentPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initVarible();
    }

	private void initVarible() {
		views = new ArrayList<Fragment>();
		views.add(new Home());
		views.add(new Trip());
		views.add(new MyCenter());
		views.add(new About());
		adapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			
			@Override
			public int getCount() {
				return views.size();
			}
			
			@Override
			public Fragment getItem(int position) {
				return views.get(position);
			}
		};
		main_vp.setAdapter(adapter);
	}

	private void initView() {
		main_vp = (ViewPager) findViewById(R.id.main_vp);
	}
    
    
}
