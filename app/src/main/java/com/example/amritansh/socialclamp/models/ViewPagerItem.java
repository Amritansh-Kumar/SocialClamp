package com.example.amritansh.socialclamp.models;

import android.support.v4.app.Fragment;

public class ViewPagerItem {

    private Fragment fragment;
    private String title;

    public ViewPagerItem(Fragment fragment, String title){
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }

}
