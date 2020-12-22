package com.bpzzr.managerlib;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;

import com.bpzzr.commonlibrary.util.LogUtil;
import com.bpzzr.commonlibrary.util.UiUtil;

import java.util.ArrayList;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

public class ManagerLib {
    public static final String KEY_RELEASE = "tdrvipksthhv5";
    public static final String KEY_DEBUG = "k51hidwqk99hb";
    private static final String TAG = "ManagerLib";

    public static void init(Context context) {
        // 初始化. 建议在 Application 中进行初始化.
        RongIM.init(context, BuildConfig.DEBUG ? KEY_DEBUG : KEY_RELEASE);

        //设置群组人员信息和会话页面的人员信息
        setGroupUserInfo();
        setUserInfo();

        RongIM.setOnReceiveMessageListener(new RongIMClient.OnReceiveMessageWrapperListener() {
            /**
             * 接收实时或者离线消息。
             * 注意:
             * 1. 针对接收离线消息时，服务端会将 200 条消息打成一个包发到客户端，客户端对这包数据进行解析。
             * 2. hasPackage 标识是否还有剩余的消息包，left 标识这包消息解析完逐条抛送给 App 层后，剩余多少条。
             * 如何判断离线消息收完：
             * 1. hasPackage 和 left 都为 0；
             * 2. hasPackage 为 0 标识当前正在接收最后一包（200条）消息，left 为 0 标识最后一包的最后一条消息也已接收完毕。
             *
             * @param message    接收到的消息对象
             * @param left       每个数据包数据逐条上抛后，还剩余的条数
             * @param hasPackage 是否在服务端还存在未下发的消息包
             * @param offline    消息是否离线消息
             * @return 是否处理消息。 如果 App 处理了此消息，返回 true; 否则返回 false 由 SDK 处理。
             */
            @Override
            public boolean onReceived(final Message message, final int left,
                                      boolean hasPackage, boolean offline) {
                MessageContent content = message.getContent();
                Conversation.ConversationType type = message.getConversationType();
                if (type == Conversation.ConversationType.GROUP) {
                    String groupId = message.getTargetId();
                    RongIM.getInstance().getUnreadCount(type, groupId,
                            new RongIMClient.ResultCallback<Integer>() {
                                @Override
                                public void onSuccess(Integer integer) {
                                    if (integer > 0) {
                                        LogUtil.Companion.e(TAG, "有未读消息");
                                    }
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                }
                UserInfo userInfo = content.getUserInfo();
                LogUtil.Companion.e(TAG, "user info： " + userInfo.getName()
                        + "," + userInfo.getUserId());
                return false;
            }
        });

    }

    //zVV1HIEdoiVkiHA5gj02svi7QVCHw8OpXOa/j1rM420Y8A8nSU7Dxw==@pjv5.cn.rongnav.com;pjv5.cn.rongcfg.com
    public static void connectRong(String rongToken) {
        RongIM.connect(rongToken, new RongIMClient.ConnectCallback() {
            @Override
            public void onDatabaseOpened(RongIMClient.DatabaseOpenStatus code) {
                //消息数据库打开，可以进入到主页面

            }

            @Override
            public void onSuccess(String s) {
                //连接成功
                LogUtil.Companion.d(TAG, "userId: " + s);
            }

            @Override
            public void onError(RongIMClient.ConnectionErrorCode errorCode) {
                if (errorCode.equals(RongIMClient.ConnectionErrorCode.RC_CONN_TOKEN_INCORRECT)) {
                    //从 APP 服务获取新 token，并重连
                } else {
                    //无法连接 IM 服务器，请根据相应的错误码作出对应处理
                }
            }
        });
    }

    public static void setUserInfo() {
        RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
            @Override
            public UserInfo getUserInfo(String userId) {
                //用异步方法获取后刷新
                UserInfo userInfo = new UserInfo(userId,
                        "userId 对应的名称",
                        Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605782399227&di=2202fda120d7657e1ca32291f2fb9631&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201401%2F09%2F20140109235950_Pnmyd.jpeg"));
                RongIM.getInstance().refreshUserInfoCache(userInfo);
                return null;
            }

        }, true);
    }

    public static void setGroupUserInfo() {
        RongIM.getInstance().setGroupMembersProvider(new RongIM.IGroupMembersProvider() {
            @Override
            public void getGroupMembers(
                    String groupId, RongIM.IGroupMemberCallback iGroupMemberCallback) {
                LogUtil.Companion.e(TAG, "getGroupMembers: " + groupId);
                //通过异步查询服务器的群组人员信息
                List<UserInfo> userInfoList = new ArrayList<>();
                userInfoList.add(new UserInfo(
                        "47974498498121728", "zyy",
                        Uri.parse("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605782399227&di=2202fda120d7657e1ca32291f2fb9631&imgtype=0&src=http%3A%2F%2Fcdn.duitang.com%2Fuploads%2Fitem%2F201401%2F09%2F20140109235950_Pnmyd.jpeg")));
                iGroupMemberCallback.onGetGroupMembersResult(userInfoList);
            }
        });
    }

    public static void setGroupInfo() {
        //RongUserInfoManager.getInstance().setGroupInfo();
    }

    public static void setUnreadCount() {
        //添加未读消息数的监听
        RongIM.getInstance().addUnReadMessageCountChangedObserver(
                new IUnReadMessageObserver() {
                    @Override
                    public void onCountChanged(int i) {

                    }
                },
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP);
    }
}
