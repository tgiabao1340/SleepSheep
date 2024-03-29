package com.example.sleepee;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sleepee.fragment.HomeFragment;
import com.example.sleepee.fragment.ProfileFragment;
import com.example.sleepee.fragment.TodayFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    public final static int TAB_SLEEP = 0;
    public final static int TAB_TODAY = 1;
    public final static int TAB_INFO = 2;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), getString(R.string.tab_name_sleep));
        adapter.addFragment(new TodayFragment(), getString(R.string.tab_name_today));
        adapter.addFragment(new ProfileFragment(), getString(R.string.tab_name_profile));
        viewPager.setAdapter(adapter);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void selectTab(int i) {
        tabLayout.getTabAt(i).select();
    }
}
