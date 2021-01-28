package com.bpzzr.skyschange.home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import com.bpzzr.skyschange.R;
import com.bpzzr.skyschange.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding homeBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeBinding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(homeBinding.getRoot());

        List<Fragment> fragmentList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            fragmentList.add(HomeFragment.newInstance());
            fragmentList.add(MeFragment.newInstance());
            fragmentList.add(MoreFragment.newInstance());
            fragmentList.add(CommonFragment.newInstance());
        }


        homeBinding.rcViewPager.setAdapter(
                new FragmentStateAdapter(this) {
                    @NonNull
                    @Override
                    public Fragment createFragment(int position) {
                        return fragmentList.get(position);
                    }

                    @Override
                    public int getItemCount() {
                        return fragmentList.size();
                    }
                });
        //homeBinding.rcViewPager.setLayoutDirection();
    }
}