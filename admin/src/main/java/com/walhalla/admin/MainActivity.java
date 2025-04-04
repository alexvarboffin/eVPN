package com.walhalla.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.walhalla.admin.adapter.ViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements CompatFragment.Callback {
    private ViewPager2 viewPager2;
    private final List<Integer> indexToPage = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simle_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager2 = findViewById(R.id.mainViewPager);
        viewPager2.setUserInputEnabled(false);
        indexToPage.add(R.id.navigation_home);
        indexToPage.add(R.id.navigation_dashboard);
        indexToPage.add(R.id.navigation_notifications);
        //indexToPage.add(R.id.navigation_3);
        final BottomNavigationView navView = findViewById(R.id.bottomNavigation);
        navView.setOnItemSelectedListener(mOnNavigationItemSelectedListener);


        ViewPagerAdapter mPagerAdapter = new ViewPagerAdapter(this);

//        try {
//            String base = "apps";
//            String[] kk = this.getAssets().list(base);
//            for (int i = 0; i < kk.length; i++) {
//                String s = kk[i];
//                mPagerAdapter.addFragment(AppListFragment.newInstance(base + "/" + s), base + "/" + s);
//                navView.getMenu().getItem(i).setTitle(s.split("\\.")[0]);
//            }
//        } catch (IOException e) {
//            DLog.handleException(e);
//        }

        mPagerAdapter.addFragment(new AppListFragment(), "W");
        viewPager2.setAdapter(mPagerAdapter);
        viewPager2.setOffscreenPageLimit(4);
    }


    @SuppressLint("NonConstantResourceId")
    private final NavigationBarView.OnItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

        Integer position = indexToPage.indexOf(item.getItemId());
        if (position >= 0) {
            menuSelected(position);
            return true;
        }

//        switch (item.getItemId()) {
//            case R.id.navigation_home:
//                fm.beginTransaction().hide(active).show(fragment1).commit();
//                active = fragment1;
//                return true;
//
//            case R.id.navigation_dashboard:
//                fm.beginTransaction().hide(active).show(fragment2).commit();
//                active = fragment2;
//                return true;
//
//            case R.id.navigation_notifications:
//                fm.beginTransaction().hide(active).show(fragment3).commit();
//                active = fragment3;
//                return true;
//        }
        return false;
    };

    private void menuSelected(Integer position) {
        handleSelectedSuccess(position);
    }

    private void handleSelectedSuccess(int position) {
        if (viewPager2.getCurrentItem() != position) {
            setItem(position);
        }
    }

    private void setItem(Integer position) {
        viewPager2.setCurrentItem(position);
        //viewModel.push(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void showMessage(String s) {

    }

    @Override
    public void onAttach(String tag, boolean b) {

    }

    @Override
    public void checkAnswer(int cursor, String string) {

    }

    @Override
    public void showLoader() {

    }

    @Override
    public void hideLoader() {

    }
}
