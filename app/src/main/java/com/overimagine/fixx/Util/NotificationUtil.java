package com.overimagine.fixx.Util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import com.overimagine.fixx.R;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.StringDef;

public class NotificationUtil {
    private static final String NotiGroup = "FIXX";

    @TargetApi(Build.VERSION_CODES.O)
    public static void createChannel(Context context) {
        NotificationChannelGroup group1 = new NotificationChannelGroup(NotiGroup, NotiGroup);
        getManager(context).createNotificationChannelGroup(group1);


        NotificationChannel channelMessage = new NotificationChannel(Channel.MESSAGE,
                context.getString(R.string.notification_channel_message_title), android.app.NotificationManager.IMPORTANCE_DEFAULT);
//        channelMessage.setDescription(context.getString(R.string.notification_channel_message_description));
        channelMessage.setGroup(NotiGroup);
        channelMessage.setLightColor(Color.GREEN);
        channelMessage.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager(context).createNotificationChannel(channelMessage);

//        NotificationChannel channelComment = new NotificationChannel(Channel.COMMENT,
//                context.getString(R.string.notification_channel_comment_title), android.app.NotificationUtil.IMPORTANCE_DEFAULT);
////        channelComment.setDescription(context.getString(R.string.notification_channel_comment_description));
//        channelComment.setGroup(NotiGroup);
//        channelComment.setLightColor(Color.BLUE);
//        channelComment.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//        getManager(context).createNotificationChannel(channelComment);

        NotificationChannel channelNotice = new NotificationChannel(Channel.NOTICE,
                context.getString(R.string.notification_channel_notice_title), android.app.NotificationManager.IMPORTANCE_HIGH);
//        channelNotice.setDescription(context.getString(R.string.notification_channel_notice_description));
        channelNotice.setGroup(NotiGroup);
        channelNotice.setLightColor(Color.RED);
        channelNotice.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager(context).createNotificationChannel(channelNotice);


    }

    private static android.app.NotificationManager getManager(Context context) {
        return (android.app.NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void deleteChannel(Context context, @Channel String channel) {
        getManager(context).deleteNotificationChannel(channel);

    }

    @TargetApi(Build.VERSION_CODES.O)
    public static void sendNotification(Context context, int id, @Channel String channel, String title, String body) {
        Notification.Builder builder = new Notification.Builder(context, channel)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(getSmallIcon())
                .setAutoCancel(true);

        getManager(context).notify(id, builder.build());
    }

    private static int getSmallIcon() {
        return android.R.drawable.stat_notify_chat;
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            Channel.MESSAGE,
            Channel.COMMENT,
            Channel.NOTICE
    })
    public @interface Channel {
        String MESSAGE = "message";
        String COMMENT = "comment";
        String NOTICE = "notice";
    }
}
