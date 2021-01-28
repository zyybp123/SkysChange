package com.bpzzr.managerlib.rong;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bpzzr.managerlib.R;

import java.util.List;

import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.SubConversationListFragment;
import io.rong.imkit.model.UIConversation;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

public class BaseConversationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_conversation);
        //SubConversationListFragment
        //RongIMClient.getInstance().getConversation(Conversation.ConversationType.GROUP,
        //        );

        //UIConversation.obtain(this,)

        //获取本地会话列表
        RongIMClient.getInstance().getConversationList(
                new RongIMClient.ResultCallback<List<Conversation>>() {
                    @Override
                    public void onSuccess(List<Conversation> conversations) {
                        //RongMsgEvent event = new RongMsgEvent();
                        int unReadCount = 0;
                        long max = 0;
                        if (conversations == null || conversations.size() == 0) {
                            //未读消息数未0
                            return;
                        }
                        //在里面统计数量
                        for (int i = 0; i < conversations.size(); i++) {
                            Conversation conversation = conversations.get(i);
                            if (conversation == null) continue;
                            unReadCount += conversation.getUnreadMessageCount();
                            //获取最新收到的消息的时间
                            max = Math.max(conversation.getReceivedTime(), max);
                        }
                        //变更UI，通知项目列表排序

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                }, Conversation.ConversationType.GROUP);
        // 添加会话界面
        ConversationFragment conversationFragment = new ConversationFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, conversationFragment);
        transaction.commit();
    }
}
