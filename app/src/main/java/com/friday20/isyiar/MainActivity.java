package com.friday20.isyiar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.friday20.isyiar.fragment.Fragment1;
import com.friday20.isyiar.fragment.Fragment2;
import com.friday20.isyiar.fragment.Fragment3;
import com.friday20.isyiar.fragment.Fragment5;
import com.friday20.isyiar.model.User;
import com.friday20.isyiar.preference.DataUser;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TextView textViewCari;
    private int[] icon1 = {
            R.drawable.ic_home,
            R.drawable.ic_heart,
            R.drawable.ic_activity,
            R.drawable.ic_account
    };


    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (!DataUser.getInstance(this).isLoggedIn()) {
            finish();
            Intent intent = new Intent(this, SignInActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            return;
        }
        textViewCari = findViewById(R.id.textViewCari);
        textViewCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        tabLayout =  findViewById(R.id.tabs);
        viewPager =  findViewById(R.id.viewpager);
        tabLayout.setupWithViewPager(viewPager);
        user = DataUser.getInstance(this).getUser();
        setupViewPager(viewPager);
        setupTabIcons();

    }

    private void setupTabIcons() {
        if (user.getUserType().equals("1")){
            tabLayout.getTabAt(0).setIcon(icon1[0]);
            tabLayout.getTabAt(1).setIcon(icon1[1]);
            tabLayout.getTabAt(2).setIcon(icon1[2]);
            tabLayout.getTabAt(3).setIcon(icon1[3]);
        } else {
            tabLayout.getTabAt(0).setIcon(icon1[0]);
            tabLayout.getTabAt(1).setIcon(icon1[1]);
            tabLayout.getTabAt(2).setIcon(icon1[3]);
        }

    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (user.getUserType().equals("1")){
            adapter.addFrag(new Fragment1(), "Home");
            adapter.addFrag(new Fragment2(), "Likes");
            adapter.addFrag(new Fragment3(), "My Syiar");
            adapter.addFrag(new Fragment5(), "Account");
        } else {
            adapter.addFrag(new Fragment1(), "Home");
            adapter.addFrag(new Fragment2(), "Likes");
            adapter.addFrag(new Fragment5(), "Account");
        }

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
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

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            //setToolbarTitle(mFragmentTitleList.get(position));
            return mFragmentTitleList.get(position);
            //return null;
        }
    }



}