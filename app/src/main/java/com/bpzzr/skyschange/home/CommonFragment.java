package com.bpzzr.skyschange.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bpzzr.commonlibrary.util.LogUtil;
import com.bpzzr.skyschange.databinding.FragmentHomeBinding;

public class CommonFragment extends Fragment {
    private static final String TAG = "CommonFragment";
    private FragmentHomeBinding homeBinding;

    public static CommonFragment newInstance() {
        CommonFragment fragmentOne = new CommonFragment();
        Bundle bundle = new Bundle();

        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.Companion.e(TAG, "onCreateView");
        homeBinding = FragmentHomeBinding.inflate(inflater,
                container, false);
        return homeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.Companion.e(TAG, "onViewCreated");
    }

    @Override
    public void onResume() {
        super.onResume();
        homeBinding.tvDesc.setText(this.getClass().getSimpleName());
        LogUtil.Companion.e(TAG, "onResume");
    }

    @Override
    public void onDestroyView() {
        homeBinding = null;
        LogUtil.Companion.e(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        LogUtil.Companion.e(TAG, "onDestroy");
        super.onDestroy();
    }
}
