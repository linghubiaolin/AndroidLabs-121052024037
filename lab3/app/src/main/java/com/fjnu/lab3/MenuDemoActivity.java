package com.fjnu.lab3;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * 实验点 3：使用 XML 定义菜单。
 * 字体大小子菜单：小/中/大（10/16/20sp）；字体颜色子菜单：红/黑；普通菜单项点击弹 Toast。
 */
public class MenuDemoActivity extends AppCompatActivity {

    private TextView tvTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        tvTest = findViewById(R.id.tv_test);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_font, menu);
        // 初始状态：中号字（16sp）+ 黑色，与布局默认值一致
        menu.findItem(R.id.action_size_medium).setChecked(true);
        menu.findItem(R.id.action_color_black).setChecked(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_size_small) {
            setFontSize(10f);
            item.setChecked(true);
            Toast.makeText(this, "字体大小：小（10sp）", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_size_medium) {
            setFontSize(16f);
            item.setChecked(true);
            Toast.makeText(this, "字体大小：中（16sp）", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_size_large) {
            setFontSize(20f);
            item.setChecked(true);
            Toast.makeText(this, "字体大小：大（20sp）", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_color_red) {
            tvTest.setTextColor(ContextCompat.getColor(this, R.color.red));
            item.setChecked(true);
            Toast.makeText(this, "字体颜色：红色", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_color_black) {
            tvTest.setTextColor(ContextCompat.getColor(this, R.color.black));
            item.setChecked(true);
            Toast.makeText(this, "字体颜色：黑色", Toast.LENGTH_SHORT).show();
            return true;
        } else if (id == R.id.action_normal) {
            Toast.makeText(this, "你点击了普通菜单项", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setFontSize(float sp) {
        tvTest.setTextSize(TypedValue.COMPLEX_UNIT_SP, sp);
    }
}
