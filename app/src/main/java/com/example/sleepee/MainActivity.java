package com.example.sleepee;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.sleepee.fragment.HomeFragment;
import com.example.sleepee.fragment.ProfileFragment;
import com.example.sleepee.fragment.TodayFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);
        setupViewPager();
        tabLayout.setupWithViewPager(viewPager);

    }

    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new HomeFragment(), "NGỦ NÈ");
        adapter.addFragment(new TodayFragment(), "HÔM NAY");
        adapter.addFragment(new ProfileFragment(), "THÔNG TIN");
        viewPager.setAdapter(adapter);
    }

    public static MainActivity getInstance() {
        return instance;
    }

    public void selectTab(int i) {
        tabLayout.getTabAt(i).select();
    }
}
