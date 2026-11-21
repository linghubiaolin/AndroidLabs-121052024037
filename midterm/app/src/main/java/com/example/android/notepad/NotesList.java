/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.notepad;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 笔记列表页。
 *
 * 在原 NotePad 示例基础上做了如下扩展：
 * 1) 列表项显示「修改时间」时间戳；
 * 2) 顶部搜索框，按标题或正文内容实时过滤；
 * 3) 附加功能：卡片式 UI、收藏星标（收藏置顶）、单条笔记导出为 txt。
 */
public class NotesList extends ListActivity {

    private static final String TAG = "NotesList";

    /** 导出笔记时用于 startActivityForResult 的请求码 */
    private static final int REQUEST_EXPORT = 1001;
    /** 导入 txt 时用于 startActivityForResult 的请求码 */
    private static final int REQUEST_IMPORT = 1002;

    /** 偏好文件名与排序方式键 */
    private static final String PREF_NAME = "notepad_prefs";
    private static final String KEY_SORT = "sort_mode";

    /** 笔记底色：白色、蓝色、绿色、黄色、粉色、紫色 */
    private static final int[] CARD_COLORS = {
            R.color.note_white, R.color.note_blue, R.color.note_green,
            R.color.note_yellow, R.color.note_pink, R.color.note_purple
    };

    /** 底色选择对话框里的名字 */
    private static final int[] CARD_COLOR_NAMES = {
            R.string.color_white, R.string.color_blue, R.string.color_green,
            R.string.color_yellow, R.string.color_pink, R.string.color_purple
    };

    /** 查询列：ID、标题、正文、修改时间、收藏标记、底色编号 */
    private static final String[] PROJECTION = new String[]{
            NotePad.Notes._ID,                           // 0
            NotePad.Notes.COLUMN_NAME_TITLE,             // 1
            NotePad.Notes.COLUMN_NAME_NOTE,              // 2
            NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE, // 3
            NotePad.Notes.COLUMN_NAME_STARRED,           // 4
            NotePad.Notes.COLUMN_NAME_COLOR              // 5
    };

    /** 标题列下标 */
    private static final int COLUMN_INDEX_TITLE = 1;

    /** 收藏的笔记置顶，其次按当前选择的排序方式 */
    private String buildSortOrder() {
        int mode = getPreferences().getInt(KEY_SORT, 0);
        String rest;
        if (mode == 1) {
            rest = NotePad.Notes.COLUMN_NAME_TITLE + " ASC";
        } else if (mode == 2) {
            rest = NotePad.Notes.COLUMN_NAME_CREATE_DATE + " DESC";
        } else {
            rest = NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE + " DESC";
        }
        return NotePad.Notes.COLUMN_NAME_STARRED + " DESC, " + rest;
    }

    private SharedPreferences getPreferences() {
        return getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    /** 列表项时间戳格式 */
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());

    private SimpleCursorAdapter mAdapter;
    private EditText mSearchInput;
    private String mKeyword = "";

    /** 待导出的笔记 id（等用户在系统保存界面选好位置） */
    private long mExportNoteId = -1;

    /** 数据变化（新建 / 保存 / 删除）时自动刷新列表 */
    private final ContentObserver mObserver =
            new ContentObserver(new Handler(Looper.getMainLooper())) {
                @Override
                public void onChange(boolean selfChange) {
                    requery();
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);

        // 没有携带 data 时，使用默认的 notes 列表 URI
        if (getIntent().getData() == null) {
            getIntent().setData(NotePad.Notes.CONTENT_URI);
        }

        // 自定义布局：标题栏（新建按钮）+ 搜索框 + 列表
        setContentView(R.layout.notes_list);

        mSearchInput = findViewById(R.id.search_input);
        mSearchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mKeyword = s.toString();
                requery();
            }
        });

        findViewById(R.id.btn_new).setOnClickListener(v ->
                startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData())));

        // 右上角「⋮」：导入 txt / 切换排序方式
        findViewById(R.id.btn_more).setOnClickListener(this::showOverflowMenu);

        mAdapter = new SimpleCursorAdapter(
                this,
                R.layout.note_item,
                null,
                new String[]{
                        NotePad.Notes.COLUMN_NAME_TITLE,
                        NotePad.Notes.COLUMN_NAME_NOTE,
                        NotePad.Notes.COLUMN_NAME_MODIFICATION_DATE,
                        NotePad.Notes.COLUMN_NAME_STARRED,
                        NotePad.Notes.COLUMN_NAME_COLOR
                },
                new int[]{R.id.title, R.id.preview, R.id.time, R.id.star, R.id.card},
                0);

        // 时间戳格式化、空标题 / 空正文兜底、星标配色
        mAdapter.setViewBinder(new SimpleCursorAdapter.ViewBinder() {
            @Override
            public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
                int id = view.getId();
                if (id == R.id.title) {
                    String title = cursor.getString(columnIndex);
                    if (title == null || title.trim().isEmpty()) {
                        title = getString(R.string.untitled_note);
                    }
                    ((TextView) view).setText(title);
                    return true;
                } else if (id == R.id.preview) {
                    String note = cursor.getString(columnIndex);
                    if (note == null || note.trim().isEmpty()) {
                        note = getString(R.string.empty_note);
                    } else {
                        // 摘要只显示一行，换行替换成空格
                        note = note.replace('\n', ' ').trim();
                    }
                    ((TextView) view).setText(note);
                    return true;
                } else if (id == R.id.time) {
                    long modified = cursor.getLong(columnIndex);
                    ((TextView) view).setText(TIME_FORMAT.format(new Date(modified)));
                    return true;
                } else if (id == R.id.star) {
                    boolean starred = cursor.getInt(columnIndex) == 1;
                    TextView star = (TextView) view;
                    star.setTextColor(getColor(starred ? R.color.star_on : R.color.star_off));
                    return true;
                } else if (id == R.id.card) {
                    // 整张卡片的底色由 color 列决定
                    view.setBackground(buildCardBackground(cursor.getInt(columnIndex)));
                    return true;
                }
                return false;
            }
        });

        setListAdapter(mAdapter);
        getListView().setEmptyView(findViewById(R.id.empty_view));
        getListView().setOnCreateContextMenuListener(this);

        // 监听 provider 数据变化，编辑页保存后列表自动刷新
        getContentResolver().registerContentObserver(
                getIntent().getData(), true, mObserver);

        requery();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            getContentResolver().unregisterContentObserver(mObserver);
        } catch (IllegalStateException e) {
            Log.w(TAG, "unregister observer failed", e);
        }
    }

    /**
     * 按当前关键字重新查询：关键字为空时显示全部，
     * 否则用 「title LIKE ? OR note LIKE ?」同时匹配标题和正文。
     */
    private void requery() {
        if (mAdapter == null) {
            return;
        }
        Uri uri = getIntent().getData();
        String selection = null;
        String[] selectionArgs = null;

        String keyword = mKeyword == null ? "" : mKeyword.trim();
        if (!keyword.isEmpty()) {
            selection = "(" + NotePad.Notes.COLUMN_NAME_TITLE + " LIKE ? OR "
                    + NotePad.Notes.COLUMN_NAME_NOTE + " LIKE ?)";
            String like = "%" + keyword + "%";
            selectionArgs = new String[]{like, like};
        }

        Cursor cursor = getContentResolver().query(
                uri, PROJECTION, selection, selectionArgs, buildSortOrder());
        // changeCursor 会自动关闭上一个游标
        mAdapter.changeCursor(cursor);
    }

    /** 把底色编号转换成圆角卡片背景 */
    private GradientDrawable buildCardBackground(int colorIndex) {
        int idx = Math.max(0, Math.min(colorIndex, CARD_COLORS.length - 1));
        float density = getResources().getDisplayMetrics().density;
        GradientDrawable bg = new GradientDrawable();
        bg.setColor(getColor(CARD_COLORS[idx]));
        bg.setCornerRadius(12 * density);
        bg.setStroke(Math.round(density), getColor(R.color.divider));
        return bg;
    }

    /** 右上角「⋮」下拉菜单 */
    private void showOverflowMenu(View anchor) {
        PopupMenu popup = new PopupMenu(this, anchor);
        popup.getMenuInflater().inflate(R.menu.list_overflow, popup.getMenu());
        popup.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.menu_import) {
                pickImportFile();
                return true;
            } else if (item.getItemId() == R.id.menu_sort) {
                showSortDialog();
                return true;
            }
            return false;
        });
        popup.show();
    }

    /** 选择排序方式（修改时间 / 标题 / 创建时间） */
    private void showSortDialog() {
        String[] items = {
                getString(R.string.sort_time),
                getString(R.string.sort_title),
                getString(R.string.sort_created)
        };
        int checked = getPreferences().getInt(KEY_SORT, 0);
        new AlertDialog.Builder(this)
                .setTitle(R.string.sort_dialog_title)
                .setSingleChoiceItems(items, checked, (dialog, which) -> {
                    getPreferences().edit().putInt(KEY_SORT, which).apply();
                    dialog.dismiss();
                    requery();
                })
                .setNegativeButton(R.string.menu_cancel, null)
                .show();
    }

    /** 选择单条笔记的底色 */
    private void showColorDialog(Uri noteUri) {
        String[] items = new String[CARD_COLOR_NAMES.length];
        for (int i = 0; i < items.length; i++) {
            items[i] = getString(CARD_COLOR_NAMES[i]);
        }
        new AlertDialog.Builder(this)
                .setTitle(R.string.menu_color)
                .setItems(items, (dialog, which) -> {
                    ContentValues v = new ContentValues();
                    v.put(NotePad.Notes.COLUMN_NAME_COLOR, which);
                    getContentResolver().update(noteUri, v, null, null);
                })
                .show();
    }

    /** 打开系统文件选择器挑选一个 txt 文件 */
    private void pickImportFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, REQUEST_IMPORT);
    }

    /** 读取选中的 txt 并插入成一条新笔记（文件名作标题） */
    private void importTxt(Uri source) {
        String name = null;
        Cursor c = getContentResolver().query(source,
                new String[]{OpenableColumns.DISPLAY_NAME}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                name = c.getString(0);
            }
            c.close();
        }
        if (name == null) {
            name = source.getLastPathSegment();
        }
        if (name == null) {
            name = getString(R.string.untitled_note);
        }
        String title = name.toLowerCase(Locale.ROOT).endsWith(".txt")
                ? name.substring(0, name.length() - 4) : name;

        StringBuilder sb = new StringBuilder();
        try (InputStream is = getContentResolver().openInputStream(source)) {
            if (is == null) {
                throw new IllegalStateException("openInputStream returned null");
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(is, StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
        } catch (Exception e) {
            Log.e(TAG, "import failed", e);
            Toast.makeText(this, R.string.toast_import_failed, Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues v = new ContentValues();
        v.put(NotePad.Notes.COLUMN_NAME_TITLE, title);
        v.put(NotePad.Notes.COLUMN_NAME_NOTE, sb.toString().trim());
        getContentResolver().insert(getIntent().getData(), v);
        Toast.makeText(this, getString(R.string.toast_imported, title), Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_options_menu, menu);

        Intent intent = new Intent(null, getIntent().getData());
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NotesList.class), null, intent, 0, null);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        MenuItem mPasteItem = menu.findItem(R.id.menu_paste);
        if (mPasteItem != null) {
            mPasteItem.setEnabled(clipboard != null && clipboard.hasPrimaryClip());
        }

        final boolean haveItems = getListAdapter() != null && getListAdapter().getCount() > 0;
        if (haveItems) {
            Uri uri = ContentUris.withAppendedId(getIntent().getData(), getSelectedItemId());

            Intent[] specifics = new Intent[1];
            specifics[0] = new Intent(Intent.ACTION_EDIT, uri);
            MenuItem[] items = new MenuItem[1];

            Intent intent = new Intent(null, uri);
            intent.addCategory(Intent.CATEGORY_ALTERNATIVE);

            menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, Menu.NONE, Menu.NONE,
                    null, specifics, intent, Menu.NONE, items);

            if (items[0] != null) {
                items[0].setShortcut('1', 'e');
            }
        } else {
            menu.removeGroup(Menu.CATEGORY_ALTERNATIVE);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_add) {
            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            return true;
        } else if (item.getItemId() == R.id.menu_paste) {
            startActivity(new Intent(Intent.ACTION_PASTE, getIntent().getData()));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) menuInfo;
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return;
        }

        Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
        if (cursor == null) {
            return;
        }

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.list_context_menu, menu);
        menu.setHeaderTitle(cursor.getString(COLUMN_INDEX_TITLE));

        Intent intent = new Intent(null, Uri.withAppendedPath(getIntent().getData(),
                Integer.toString((int) info.id)));
        intent.addCategory(Intent.CATEGORY_ALTERNATIVE);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        menu.addIntentOptions(Menu.CATEGORY_ALTERNATIVE, 0, 0,
                new ComponentName(this, NotesList.class), null, intent, 0, null);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info;
        try {
            info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        } catch (ClassCastException e) {
            Log.e(TAG, "bad menuInfo", e);
            return false;
        }
        Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
        int id = item.getItemId();

        if (id == R.id.context_open) {
            startActivity(new Intent(Intent.ACTION_EDIT, noteUri));
            return true;
        } else if (id == R.id.context_star) {
            toggleStar(noteUri);
            return true;
        } else if (id == R.id.context_color) {
            showColorDialog(noteUri);
            return true;
        } else if (id == R.id.context_export) {
            startExport(noteUri);
            return true;
        } else if (id == R.id.context_copy) {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null) {
                clipboard.setPrimaryClip(ClipData.newUri(getContentResolver(), "Note", noteUri));
            }
            return true;
        } else if (id == R.id.context_delete) {
            getContentResolver().delete(noteUri, null, null);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    /** 切换收藏状态（附加功能：收藏星标） */
    private void toggleStar(Uri noteUri) {
        boolean starred = false;
        Cursor c = getContentResolver().query(noteUri,
                new String[]{NotePad.Notes.COLUMN_NAME_STARRED}, null, null, null);
        if (c != null) {
            if (c.moveToFirst()) {
                starred = c.getInt(0) == 1;
            }
            c.close();
        }

        ContentValues values = new ContentValues();
        values.put(NotePad.Notes.COLUMN_NAME_STARRED, starred ? 0 : 1);
        getContentResolver().update(noteUri, values, null, null);
        Toast.makeText(this, starred ? "已取消收藏" : "已收藏，置顶显示",
                Toast.LENGTH_SHORT).show();
    }

    /** 导出单条笔记：拉起系统「保存到文件」界面（附加功能：导出 txt） */
    private void startExport(Uri noteUri) {
        Cursor c = getContentResolver().query(noteUri,
                new String[]{NotePad.Notes._ID, NotePad.Notes.COLUMN_NAME_TITLE},
                null, null, null);
        String title = "note";
        if (c != null) {
            if (c.moveToFirst()) {
                mExportNoteId = c.getLong(0);
                String t = c.getString(1);
                if (t != null && !t.trim().isEmpty()) {
                    title = t.trim();
                }
            }
            c.close();
        }

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, title + ".txt");
        startActivityForResult(intent, REQUEST_EXPORT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK || data == null) {
            return;
        }
        if (requestCode == REQUEST_IMPORT) {
            Uri source = data.getData();
            if (source != null) {
                importTxt(source);
            }
            return;
        }
        if (requestCode != REQUEST_EXPORT) {
            return;
        }
        Uri target = data.getData();
        if (target == null || mExportNoteId < 0) {
            return;
        }

        Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), mExportNoteId);
        Cursor c = getContentResolver().query(noteUri,
                new String[]{NotePad.Notes.COLUMN_NAME_TITLE, NotePad.Notes.COLUMN_NAME_NOTE},
                null, null, null);
        String title = "";
        String note = "";
        if (c != null) {
            if (c.moveToFirst()) {
                title = c.getString(0);
                note = c.getString(1);
            }
            c.close();
        }

        try (OutputStream os = getContentResolver().openOutputStream(target)) {
            if (os == null) {
                throw new IllegalStateException("openOutputStream returned null");
            }
            String content = (title == null ? "" : title) + "\n\n"
                    + (note == null ? "" : note);
            os.write(content.getBytes(StandardCharsets.UTF_8));
            os.flush();
            Toast.makeText(this,
                    getString(R.string.toast_exported, target.getLastPathSegment()),
                    Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e(TAG, "export failed", e);
            Toast.makeText(this, R.string.toast_export_failed, Toast.LENGTH_SHORT).show();
        }
        mExportNoteId = -1;
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);
        String action = getIntent().getAction();

        if (Intent.ACTION_PICK.equals(action) || Intent.ACTION_GET_CONTENT.equals(action)) {
            setResult(RESULT_OK, new Intent().setData(uri));
        } else {
            startActivity(new Intent(Intent.ACTION_EDIT, uri));
        }
    }
}
