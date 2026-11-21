package com.fjnu.lab3;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验点 5：RecyclerView 列表视图。
 * 列表项包含图片与名称；点击显示名称；新增记录后立即刷新且滚动、点击均不错位。
 */
public class RecyclerDemoActivity extends AppCompatActivity {

    private final List<Animal> data = new ArrayList<>();
    private AnimalAdapter adapter;
    private RecyclerView recyclerView;

    private int addedCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        data.addAll(Animal.createZoo());

        recyclerView = findViewById(R.id.recycler_view);
        // 固定尺寸可避免每次绑定都重新测量，滚动更流畅
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AnimalAdapter(data, (position, animal) ->
                Toast.makeText(this, "你点击的是：" + animal.getName(), Toast.LENGTH_SHORT).show());
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btn_add).setOnClickListener(v -> addRecord());
    }

    /** 新增一条记录：插入到表头并调用 notifyItemInserted，实现「立即更新」。 */
    private void addRecord() {
        addedCount++;
        String[] names = {"Panda", "Wolf", "Rabbit", "Bear", "Fox"};
        int[] photos = {R.drawable.cat, R.drawable.dog, R.drawable.monkey,
                R.drawable.lion, R.drawable.tiger};
        int index = (addedCount - 1) % names.length;

        Animal animal = new Animal(names[index] + " #" + addedCount, photos[index]);
        adapter.addItem(animal);
        recyclerView.scrollToPosition(0);

        Toast.makeText(this, "已新增：" + animal.getName(), Toast.LENGTH_SHORT).show();
    }
}
