package com.bpzzr.managerlib.rong;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.bpzzr.commonlibrary.util.LogUtil;
import com.bpzzr.managerlib.R;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imkit.utils.RongDateUtils;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

public class BaseConversationListAdapter extends ConversationListAdapter {
    private static final String TAG = "BaseConversationListAdapter";
    private Context mContext;

    public BaseConversationListAdapter(Context context) {
        super(context);
        this.mContext = context;
    }

    @Override
    protected View newView(Context context, int position, ViewGroup group) {
        // 此处用于会话列表界面的初始化. 可在此处自定义界面或修改界面.
        View view = View.inflate(mContext, R.layout.item_base_conversation_list, null);
        ViewHolder holder = new ViewHolder();
        holder.ivAvatar = view.findViewById(R.id.iv_avatar);
        holder.tvRoomName = view.findViewById(R.id.chat_room_name);
        holder.tvChatContent = view.findViewById(R.id.chat_content);
        holder.tvUserCount = view.findViewById(R.id.chat_user_count);
        holder.tvBadge = view.findViewById(R.id.tv_badge);
        holder.tvChatTime = view.findViewById(R.id.tv_chat_time);
        view.setTag(holder);
        return view;
    }

    @Override
    protected void bindView(View v, int position, UIConversation data) {
        //super.bindView(v, position, data);
        // 此处是对控件进行操作. 可在方法中操作控件.
        Conversation.ConversationType type = data.getConversationType();
        ViewHolder holder = (ViewHolder) v.getTag();
        if (type == Conversation.ConversationType.GROUP) {
            String groupId = data.getConversationTargetId();
            //对群组来说，这个就是群组id
            LogUtil.Companion.e(TAG, "groupId: " + groupId);
            //通过群组id匹配后台服务器返回的研讨室的数据
            //holder.tvRoomName.setText("研讨室");
            //holder.ivAvatar.setImageResource(R.drawable.fire);
            //holder.tvUserCount.setText(String.format("%s人", 3));
            //
            //RongIM.getInstance().getU
            RongIM.getInstance().getConversation(Conversation.ConversationType.GROUP,
                    groupId, new RongIMClient.ResultCallback<Conversation>() {
                        @Override
                        public void onSuccess(Conversation conversation) {
                            UIConversation uc = UIConversation
                                    .obtain(mContext, conversation, false);
                            String senderId = uc.getConversationSenderId();
                            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(senderId);
                            if (userInfo != null) {
                                holder.tvChatContent.setText(String.format("%s：%s",
                                        userInfo.getName(), uc.getConversationContent()));
                            }
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });

            holder.tvChatTime.setText(RongDateUtils
                    .getConversationListFormatDate(data.getUIConversationTime(), mContext));

            /*String senderId = data.getConversationSenderId();
            UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(senderId);
            if (userInfo != null) {
                holder.tvChatContent.setText(String.format("%s：%s",
                        userInfo.getName(), data.getConversationContent()));
            }*/

            if (data.getUnReadMessageCount() > 0) {
                holder.tvBadge.setVisibility(View.VISIBLE);
                if (data.getUnReadMessageCount() > 99) {
                    holder.tvBadge.setText(this.mContext.getResources()
                            .getString(R.string.rc_message_unread_count));
                } else {
                    holder.tvBadge.setText(String.valueOf(data.getUnReadMessageCount()));
                }
            } else {
                holder.tvBadge.setVisibility(View.INVISIBLE);
            }
        }
    }

    class ViewHolder {
        ImageView ivAvatar;
        TextView tvRoomName;
        TextView tvUserCount;
        TextView tvBadge;
        TextView tvChatContent;
        TextView tvChatTime;
    }
}
