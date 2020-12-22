package com.bpzzr.managerlib.rong;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bpzzr.managerlib.R;

import java.util.List;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.model.Event;
import io.rong.imkit.widget.RongSwipeRefreshLayout;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.model.Conversation;

/**
 * 目标：
 * 1、只显示需要的会话列表
 * 2、用其他数据来绑定界面
 */
public class BaseConversationListFragment extends ConversationListFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        RongSwipeRefreshLayout mRefreshLayout = view.findViewById(R.id.rc_refresh);
        if (mRefreshLayout != null){
            mRefreshLayout.setCanRefresh(true);
        }
        return view;
    }

    @Override
    public void getConversationList(Conversation.ConversationType[] conversationTypes, IHistoryDataResultCallback<List<Conversation>> callback, boolean isLoadMore) {
        super.getConversationList(conversationTypes, callback, isLoadMore);
    }

    @Override
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        super.onEventMainThread(event);
    }

    @Override
    public ConversationListAdapter onResolveAdapter(Context context) {
        return new BaseConversationListAdapter(context);
    }
}
