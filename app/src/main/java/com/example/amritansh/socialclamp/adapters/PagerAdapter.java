package com.example.amritansh.socialclamp.adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.amritansh.socialclamp.models.ViewPagerItem;

import java.util.List;

public class PagerAdapter extends FragmentPagerAdapter {

    private List<ViewPagerItem> viewPagerItems;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (viewPagerItems != null){
            return viewPagerItems.get(position).getFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        if (viewPagerItems != null){
            return viewPagerItems.size();
        }
        return 0;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (viewPagerItems != null){
            return viewPagerItems.get(position).getTitle();
        }
        return null;
    }

    public void updateList(List<ViewPagerItem> viewPagerItems) {
        this.viewPagerItems = viewPagerItems;
        notifyDataSetChanged();
    }
}
