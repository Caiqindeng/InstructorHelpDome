package com.example.instructorhelpdome.receiver;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

import com.example.instructorhelpdome.JavaBean.PushInfo;
import com.example.instructorhelpdome.R;
import com.example.instructorhelpdome.WebviewActivity;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import androidx.core.app.NotificationCompat;
import cn.bmob.push.PushConstants;
import cn.bmob.v3.util.BmobNotificationManager;



//TODO 集成：1.3、创建自定义的推送消息接收器，并在清单文件中注册
public class PushMessageReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if (intent.getAction().equals(PushConstants.ACTION_MESSAGE)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
            Logger.i("客户端收到推送消息：" + msg);
            Gson gson = new Gson();
            PushInfo pushInfo = gson.fromJson(msg, PushInfo.class);
            String alert = pushInfo.getAlert();
            String articleurl = pushInfo.getArticleurl();
            String articleUrl =articleurl.replaceAll("http://bmob-cdn-25347.b0.upaiyun.com","http://jn.philuo.com");
            Intent pendingIntent = new Intent(context, WebviewActivity.class);
            Bundle B=new Bundle();
            B.putString("name",alert);
            B.putString("url",articleUrl);
            pendingIntent.putExtra("data",B);
            pendingIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.logo);
            BmobNotificationManager.getInstance(context).showNotification(largeIcon, "校内通知", alert, articleurl, pendingIntent, NotificationManager.IMPORTANCE_MIN, NotificationCompat.FLAG_ONLY_ALERT_ONCE);
        }
    }
}
