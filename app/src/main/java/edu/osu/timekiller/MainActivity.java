package edu.osu.timekiller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static androidx.fragment.app.FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;

//
public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottom_nav_bar = null;
    private final String TAG = "MainActivity";
    ViewPager viewPager = null;
    List<String> user_info = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyBsQ-4iMKqnVLLwlzcDN67bR68DdrckFvU");
        }

        PlacesClient placesClient = Places.createClient(this);

        // Nav_bar matches three major fragments WorldFragment, NewPostFragment, and UserProfileFragment

        bottom_nav_bar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        Fragment[] frags = new Fragment[3];
        frags[0] = new WorldFragment();
        frags[1] = new NewPostFragment();
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

        // Connects ViewPager to fragments
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(adapter);

        // Setting different display fragment according to users selections
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