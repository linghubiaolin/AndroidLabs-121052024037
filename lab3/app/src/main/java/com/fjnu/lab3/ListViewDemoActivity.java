package com.fjnu.lab3;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实验点 1：ListView + SimpleAdapter。
 * 列表项 = 名称 + 缩略图；点击时用 Toast 显示选中信息，并发送一条通知。
 */
public class ListViewDemoActivity extends AppCompatActivity {

    private static final String CHANNEL_ID = "lab3_list_click";
    private static final String CHANNEL_NAME = "实验3 列表点击通知";
    private static final int NOTIFICATION_ID = 3001;

    private final List<Map<String, Object>> data = new ArrayList<>();
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        listView = findViewById(android.R.id.list);

        buildData();
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                data,
                R.layout.item_animal,
                new String[]{"name", "icon"},
                new int[]{R.id.tv_name, R.id.iv_icon});
        listView.setAdapter(adapter);

        // 单选模式：点击后 Item 进入 activated 状态，配合 selector 呈现深红选中效果
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener((parent, view, position, id) -> {
            String name = (String) data.get(position).get("name");
            Toast.makeText(this, "你选中了：" + name, Toast.LENGTH_SHORT).show();
            listView.setItemChecked(position, true);
            sendNotification(name);
        });

        createNotificationChannel();
    }

    /** 用 Map 组织 SimpleAdapter 需要的「列名 -> 值」结构。 */
    private void buildData() {
        for (Animal animal : Animal.createZoo()) {
            Map<String, Object> row = new HashMap<>();
            row.put("name", animal.getName());
            row.put("icon", animal.getPhotoRes());
            data.add(row);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("点击 ListView 列表项时触发的通知");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

    /** 通知的 icon 使用应用图标，Title 使用被点击的列表项内容。 */
    private void sendNotification(String title) {
        if (!canPostNotification()) {
            Toast.makeText(this, "通知权限未开启，已跳过通知", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, ListViewDemoActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText("你点击了 " + title + "，通知标题与列表项内容一致。")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(this).notify(NOTIFICATION_ID, builder.build());
    }

    private boolean canPostNotification() {
        if (!NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            return false;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(this,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }
}
