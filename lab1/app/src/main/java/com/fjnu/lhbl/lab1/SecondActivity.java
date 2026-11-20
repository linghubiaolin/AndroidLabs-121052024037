package com.fjnu.lhbl.lab1;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fjnu.lhbl.lab1.databinding.ActivitySecondBinding;

/**
 * 第二个 Activity：演示 Intent 传值 + setResult 回传数据。
 */
public class SecondActivity extends AppCompatActivity {

    /** 首页 -> 本页：点击次数 */
    public static final String EXTRA_COUNT = "extra_count";

    /** 本页 -> 首页：回传的消息 */
    public static final String EXTRA_REPLY = "extra_reply";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivitySecondBinding binding = ActivitySecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 取出首页传过来的点击次数，取不到时给默认值 0
        int count = getIntent().getIntExtra(EXTRA_COUNT, 0);
        binding.tvCount.setText(getString(R.string.second_count, count));

        // 点击后回传数据并关闭本页，首页的回调会收到 RESULT_OK
        binding.btnBack.setOnClickListener(v -> {
            Intent data = new Intent();
            data.putExtra(EXTRA_REPLY, getString(R.string.reply_message));
            setResult(RESULT_OK, data);
            finish();
        });
    }
}
