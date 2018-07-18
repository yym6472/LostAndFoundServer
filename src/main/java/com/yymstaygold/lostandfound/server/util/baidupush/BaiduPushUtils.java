package com.yymstaygold.lostandfound.server.util.baidupush;

import com.baidu.yun.core.log.YunLogEvent;
import com.baidu.yun.core.log.YunLogHandler;
import com.baidu.yun.push.auth.PushKeyPair;
import com.baidu.yun.push.client.BaiduPushClient;
import com.baidu.yun.push.constants.BaiduPushConstants;
import com.baidu.yun.push.exception.PushClientException;
import com.baidu.yun.push.exception.PushServerException;
import com.baidu.yun.push.model.PushMsgToAllRequest;
import com.baidu.yun.push.model.PushMsgToAllResponse;
import com.baidu.yun.push.model.PushMsgToSingleDeviceRequest;
import com.baidu.yun.push.model.PushMsgToSingleDeviceResponse;

/**
 * Created by StayGold on 2018/7/16.
 */
public class BaiduPushUtils {
    private static final String API_KEY = "VpN7bFzyHygI1FiTm9Ggpl6o";
    private static final String SECRET_KEY = "khHUS3GkkuZEpn6Q0zjRtRyIcKuNVh0L";

    private static BaiduPushClient getClient() {
        PushKeyPair pair = new PushKeyPair(API_KEY, SECRET_KEY);
        BaiduPushClient client = new BaiduPushClient(pair, BaiduPushConstants.CHANNEL_REST_URL);
        client.setChannelLogHandler(new YunLogHandler() {
            @Override
            public void onHandle(YunLogEvent yunLogEvent) {
                System.out.println(yunLogEvent.getMessage());
            }
        });
        return client;
    }

    public static void pushMsgToSingleDevice(String channelId, String msg, int msgType, int expireSec) {
        try {
            PushMsgToSingleDeviceRequest request = new PushMsgToSingleDeviceRequest()
                    .addChannelId(channelId)
                    .addMsgExpires(expireSec)
                    .addMessageType(msgType)
                    .addMessage(msg)
                    .addDeviceType(3);

            BaiduPushClient pushClient = getClient();
            PushMsgToSingleDeviceResponse response = pushClient
                    .pushMsgToSingleDevice(request);
            System.out.println("msgId: " + response.getMsgId() +
                    ", sendTime: " + response.getSendTime());
        } catch (PushClientException e) {
            e.printStackTrace();
        } catch (PushServerException e) {
            System.out.println(String.format(
                    "requestId: %d, errorCode: %d, errorMsg: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
    }

    public static void pushMsgToAll(String msg, int msgType, int expireSec) {
        try {
            PushMsgToAllRequest request = new PushMsgToAllRequest()
                    .addMsgExpires(expireSec)
                    .addMessageType(msgType)
                    .addMessage(msg)
                    .addDeviceType(3);

            BaiduPushClient pushClient = getClient();
            PushMsgToAllResponse response = pushClient.pushMsgToAll(request);

            System.out.println("msgId: " + response.getMsgId() +
                    ", sendTime: " + response.getSendTime() +
                    ", timerId: " + response.getTimerId());
        } catch (PushClientException e) {
            e.printStackTrace();
        } catch (PushServerException e) {
            System.out.println(String.format(
                    "requestId: %d, errorCode: %d, errorMsg: %s",
                    e.getRequestId(), e.getErrorCode(), e.getErrorMsg()));
        }
    }

    public static void forceOffline(String channelId) {
        pushMsgToSingleDevice(channelId, "FORCE_OFFLINE", 0, 1);
    }
}
