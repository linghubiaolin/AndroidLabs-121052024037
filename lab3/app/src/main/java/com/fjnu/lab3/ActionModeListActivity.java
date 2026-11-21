package com.fjnu.lab3;

import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * 实验点 4：上下文操作模式（ActionMode）形式的上下文菜单。
 * 长按列表项进入 ActionMode，标题显示 "1 selected"，点删除图标移除该行。
 */
public class ActionModeListActivity extends AppCompatActivity {

    private final List<String> items = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private ListView listView;
    private ActionMode actionMode;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_mode);

        listView = findViewById(android.R.id.list);

        items.add("One");
        items.add("Two");
        items.add("Three");
        items.add("Four");
        items.add("Five");

        adapter = new ArrayAdapter<>(this, R.layout.item_action_mode, R.id.tv_item_name, items);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        // 长按进入上下文操作模式
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            selectedPosition = position;
            if (actionMode == null) {
                actionMode = startActionMode(new DeleteActionModeCallback());
            }
            listView.setItemChecked(position, true);
            return true;
        });

        listView.setOnItemClickListener((parent, view, position, id) ->
                Toast.makeText(this, "点击了：" + items.get(position), Toast.LENGTH_SHORT).show());
    }

    /** ActionMode 的生命周期回调，等价于「上下文菜单」的 onCreate / onClick。 */
    private class DeleteActionModeCallback implements ActionMode.Callback {

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            mode.setTitle("1 selected");
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            if (item.getItemId() == R.id.action_delete) {
                if (selectedPosition >= 0 && selectedPosition < items.size()) {
                    String removed = items.remove(selectedPosition);
                    adapter.notifyDataSetChanged();
                    Toast.makeText(ActionModeListActivity.this,
                            "已删除：" + removed, Toast.LENGTH_SHORT).show();
                }
                mode.finish();
                return true;
            } else if (item.getItemId() == R.id.action_select_all) {
                Toast.makeText(ActionModeListActivity.this,
                        "当前共 " + items.size() + " 条记录", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            selectedPosition = -1;
            listView.clearChoices();
            listView.requestLayout();
        }
    }
}
