package edu.osu.timekiller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.firestore.auth.User;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

public class MainActivity2 extends AppCompatActivity {
    private BottomNavigationView bottom_nav_bar = null;
    private final String TAG = "mainactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        bottom_nav_bar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Fragment[] frags = new Fragment[3];
        frags[0] = new WorldFragment();
        frags[1] = new SunsetFragment();
        frags[2] = new UserProfileFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(fragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            @NonNull
            @Override
            public Fragment getItem(int position) {
                return frags[position];
            }

            @Override
            public int getCount() {
                return frags.length;
            }
        };
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                if(position == 0){
                    bottom_nav_bar.setSelectedItemId(R.id.item_1);
                }

                if(position == 1){
                    bottom_nav_bar.setSelectedItemId(R.id.item_2);
                }

                if(position == 3){
                    bottom_nav_bar.setSelectedItemId(R.id.item_3);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }


        });


        bottom_nav_bar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId() == R.id.item_3){
                    Log.d(TAG,"item 3 selected");
                    viewPager.setCurrentItem(2);
                    return true;
                }
                if(item.getItemId() == R.id.item_1){
                    Log.d(TAG,"item 1 selected");
                    viewPager.setCurrentItem(0);
                    return true;
                }
                if(item.getItemId() == R.id.item_2){
                    Log.d(TAG,"item 2 selected");
                    viewPager.setCurrentItem(1);
                    return true;
                }
                return false;
            }
        });
    }
}