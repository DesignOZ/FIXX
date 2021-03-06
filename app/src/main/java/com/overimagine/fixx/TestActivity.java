package com.overimagine.fixx;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.overimagine.fixx.Fragment.InfoFragment;
import com.overimagine.fixx.Fragment.ListFragment;
import com.overimagine.fixx.Fragment.SettingFragment;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        final TabLayout tabLayout = findViewById(R.id.tabLayout);

        ViewPager viewPager = findViewById(R.id.viewPager);
        FragmentPagerAdapter fragmentPagerAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

            private final static int NUM_FRAGMENTS = 3;

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ListFragment();
                    case 1:
                        return SettingFragment.newInstance("", "");
                    case 2:
                        return InfoFragment.newInstance("", "");
                    default:
                        return null;
                }
            }

            @Override
            public int getCount() {
                return NUM_FRAGMENTS;
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "List";
                    case 1:
                        return "Setting";
                    case 2:
                        return "Info";
                    default:
                        return null;
                }
            }
        };

        viewPager.setAdapter(fragmentPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
