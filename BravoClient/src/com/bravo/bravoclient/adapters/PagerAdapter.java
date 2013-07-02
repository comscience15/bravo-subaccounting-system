package com.bravo.bravoclient.adapters;
 
import com.bravo.bravoclient.fragments.CardsTabFragment;
import com.bravo.bravoclient.fragments.HomeTabFragment;
import com.bravo.bravoclient.fragments.RewardsTabFragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
 
public class PagerAdapter extends FragmentPagerAdapter{
 
    final int PAGE_COUNT = 3;
 
    /** Constructor of the class */
    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    /** This method will be invoked when a page is requested to create */
    @Override
    public Fragment getItem(int arg0) {
        Bundle data = new Bundle();
        switch(arg0){
            /** Home tab is selected */
            case 0:
                HomeTabFragment homeFragment = new HomeTabFragment();
                data.putInt("current_page", arg0+1);
                homeFragment.setArguments(data);
                return homeFragment;
 
            /** Cards tab is selected */
            case 1:
                CardsTabFragment cardsFragment = new CardsTabFragment();
                data.putInt("current_page", arg0+1);
                cardsFragment.setArguments(data);
                return cardsFragment;
            
            /** Rewards tab is selected */
            case 2:
            	RewardsTabFragment rewardsFragment = new RewardsTabFragment();
            	data.putInt("current_page", arg0+1);
            	rewardsFragment.setArguments(data);
            	return rewardsFragment;
        }
        return null;
    }
 
    /** Returns the number of pages */
    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}