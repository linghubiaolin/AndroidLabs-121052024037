package com.fjnu.lhbl.lab1;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.fjnu.lhbl.lab1.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 首页 Activity。
 * 实验1 的改造点：在官方 Hello World 模板上增加了
 * 1) 点击计数（状态保存在 Activity 中）
 * 2) 显式 Intent 跳转到 SecondActivity 并携带数据
 * 3) ActivityResultLauncher 接收第二个页面回传的结果
 */
public class MainActivity extends AppCompatActivity {

    /** ViewBinding 生成的绑定对象，替代 findViewById */
    private ActivityMainBinding binding;

    /** 点击计数 */
    private int clickCount = 0;

    /** 注册结果回调：在 Activity 启动完成之前注册，比旧版 startActivityForResult 更安全 */
    private final ActivityResultLauncher<Intent> secondPageLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    String reply = result.getData().getStringExtra(SecondActivity.EXTRA_REPLY);
                    if (reply != null) {
                        binding.tvReply.setText(getString(R.string.reply_prefix, reply));
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 把 activity_main.xml 渲染成 View 树，并交给我们一个绑定对象
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 1. 显示进入页面的时间，证明代码真的跑起来了
        String now = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                .format(new Date());
        binding.tvTime.setText(getString(R.string.time_format, now));

        // 2. 初始化计数文本
        binding.tvCount.setText(getString(R.string.click_count, clickCount));

        // 3. 点击计数按钮：更新状态 + 刷新界面 + 弹一个 Toast
        binding.btnCount.setOnClickListener(v -> {
            clickCount++;
            binding.tvCount.setText(getString(R.string.click_count, clickCount));
            Toast.makeText(MainActivity.this,
                    getString(R.string.click_toast, clickCount),
                    Toast.LENGTH_SHORT).show();
        });

        // 4. 跳转到第二个页面，并把当前点击次数当作 Extra 传过去
        binding.btnJump.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra(SecondActivity.EXTRA_COUNT, clickCount);
            secondPageLauncher.launch(intent);
        });
    }
}
