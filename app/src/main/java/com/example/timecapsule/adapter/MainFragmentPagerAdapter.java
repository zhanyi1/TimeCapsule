package com.example.timecapsule.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainFragmentPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments ;//Collection of Fragment added
    private List<String> mFragmentsTitles ;//The collection of title corresponding to each Fragment

    public MainFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        mFragments = new ArrayList<>();
        mFragmentsTitles = new ArrayList<>();
    }

    public void addFragment(Fragment fragment, String fragmentTitle) {
        mFragments.add(fragment);
        mFragmentsTitles.add(fragmentTitle);
    }

    public void updateFragment(int index,Fragment fragment, String fragmentTitle) {
        mFragments.remove(index);
        mFragments.add(index,fragment);
        mFragmentsTitles.add(fragmentTitle);

        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        //Get the title of the Fragment corresponding to the position
        return mFragmentsTitles.get(position);
    }
}
