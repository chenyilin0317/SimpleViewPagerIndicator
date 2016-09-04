package com.example.viewpagerindicator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class MainActivity extends FragmentActivity{

	private ViewPager pager;
	private ViewPagerIndicator indicator;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(1);
		setContentView(R.layout.activity_main2);
		pager = (ViewPager) findViewById(R.id.pager);
		indicator = (ViewPagerIndicator) findViewById(R.id.indicator);
		indicator.setVisCount(4);
		indicator.setTitleTable(new String[]{"TAG-1","TAG-2","TAG-3","TAG-4","TAG-5","TAG-6"});
		pager.setAdapter(new FragmentAdapter(getSupportFragmentManager()));
		indicator.setViewPager(pager,0);
	}
	
	
	public class FragmentAdapter extends FragmentPagerAdapter{

		public FragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			BaseFragment baseFragment = new BaseFragment("Test");
			return baseFragment;
		}

		@Override
		public int getCount() {
			return 9;
		}
		
	}
}
