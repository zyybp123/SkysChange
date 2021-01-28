package com.bpzzr.skyschange.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewbinding.ViewBinding;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.bpzzr.audiolibrary.audio.AudioControlEntity;
import com.bpzzr.audiolibrary.audio.AudioEntity;
import com.bpzzr.audiolibrary.audio.ControlCommand;
import com.bpzzr.commonlibrary.base.BaseBindingFragment;
import com.bpzzr.commonlibrary.util.LogUtil;
import com.bpzzr.skyschange.R;
import com.bpzzr.skyschange.databinding.FragmentHomeBinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class HomeFragment extends BaseBindingFragment {
    public static HomeFragment newInstance() {
        HomeFragment fragmentOne = new HomeFragment();
        Bundle bundle = new Bundle();

        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }


    @Override
    public void paramsInitial(@Nullable Bundle arguments) {

    }


    @Override
    public int getSuccessViewId() {
        return R.layout.fragment_home;
    }

    @NotNull
    @Override
    public Class<FragmentHomeBinding> getSuccessBindingClass() {
        return FragmentHomeBinding.class;
    }

    @Override
    public void dealRequest() {
        getMBinding().cbStateLayout.showSuccess();
        AudioEntity entity = new AudioEntity();
        entity.setAudioName("1111");
        entity.setTest(new AudioControlEntity(ControlCommand.ERROR,
                100, "111", 156,1564543417000L));
        String jsonString = JSON.toJSONString(entity);
        Object parse = JSON.parse(jsonString);
        JSON.parse(jsonString, Feature.AutoCloseSource);
        LogUtil.Companion.e(this.getClass().getSimpleName(),
                jsonString);
        if (parse instanceof AudioEntity){
            LogUtil.Companion.e(this.getClass().getSimpleName(),
                   "audio: "+ parse.toString());
        }
        //LogUtil.Companion.e(this.getClass().getSimpleName(),
        //        parse.toString());

        //getMSuccessBinding().tvDesc.setText("测试");


    }
}
