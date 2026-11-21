package com.fjnu.lab3;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * 实验3 入口页：通过 5 个按钮分别进入 5 个界面组件的演示页面。
 */
public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_NOTIFICATION = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_list_view).setOnClickListener(v ->
                startActivity(new Intent(this, ListViewDemoActivity.class)));

        findViewById(R.id.btn_dialog).setOnClickListener(v ->
                startActivity(new Intent(this, CustomDialogActivity.class)));

        findViewById(R.id.btn_menu).setOnClickListener(v ->
                startActivity(new Intent(this, MenuDemoActivity.class)));

        findViewById(R.id.btn_action_mode).setOnClickListener(v ->
                startActivity(new Intent(this, ActionModeListActivity.class)));

        findViewById(R.id.btn_recycler).setOnClickListener(v ->
                startActivity(new Intent(this, RecyclerDemoActivity.class)));

        requestNotificationPermission();
    }

    /**
     * Android 13（API 33）起，发送通知需要动态申请 POST_NOTIFICATIONS 权限，
     * 否则 NotificationManager.notify() 会被系统静默拦截。
     */
    private void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.POST_NOTIFICATIONS},
                        REQUEST_NOTIFICATION);
            }
        }
    }
}
