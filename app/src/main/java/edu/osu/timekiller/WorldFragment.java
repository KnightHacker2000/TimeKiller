package edu.osu.timekiller;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WorldFragment#newInstance} factory method to
 * create an instance of this fragment.
 */

// This fragment is to display google map and posts
public class WorldFragment extends Fragment {

    TabLayout tabLayout;
    ViewPager viewPager;
    Adapter adapter;
    public WorldFragment() {
        // Required empty public constructor
    }


    public static WorldFragment newInstance() {
        WorldFragment fragment = new WorldFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Get view and inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_world, container, false);
        tabLayout = (TabLayout) v.findViewById(R.id.tabs);
        viewPager = (ViewPager) v.findViewById(R.id.viewpager);
        adapter = new Adapter(getChildFragmentManager());
        // Embed MapViewFragment fragment to current WorldFragment
        adapter.addFragment(MapViewFragment.newInstance(), "map view");
        // Embed ListViewFragment fragment to current WorldFragment
        adapter.addFragment(ListViewFragment.newInstance(), "list view");
        // Passes relations to viewPager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
}


// Override adapter
class Adapter extends FragmentPagerAdapter {
    public final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public Adapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
