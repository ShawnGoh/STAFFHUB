package com.example.blewifiterm5project.AdminWorld;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.blewifiterm5project.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;


public class MappingFragment extends Fragment  {

    TabLayout tabLayout;
    ViewPager viewPager;
    String url;

    public MappingFragment(String url) {
        this.url = url;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create fragment and views' instance
        View view = inflater.inflate(R.layout.fragment_mapping, container, false);

        //childfragmentmanager used to instantiate and populate nested child fragments
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        viewPagerAdapter.addFragment(new ChildMappingFragment(url), "Map");
        viewPagerAdapter.addFragment(new ChildTestingFragment(), "Test");
        viewPager.setAdapter(viewPagerAdapter);

        return view;
    }

    /**
     * ViewPager Adapter.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> fragments;
        private ArrayList<String> titles;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment frag, String title){
            fragments.add(frag);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }
    }



}