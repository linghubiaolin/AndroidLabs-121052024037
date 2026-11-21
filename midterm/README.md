# NotePad 记事本应用（移动软件开发期中实验）

在 [fjnu-cse/NotePad](https://github.com/fjnu-cse/NotePad) 源码基础上完成的功能扩展与界面重构，包名仍为 `com.example.android.notepad`，数据仍由 `com.google.provider.NotePad` 这个 ContentProvider 提供。

- 姓名：令狐标霖
- 学号：121052024037
- 专业：福建师范大学 计算机与网络安全学院 软件工程

## 功能清单

| 类别 | 功能 | 说明 |
| --- | --- | --- |
| 基本要求 | 列表时间戳 | 每条笔记显示 `yyyy-MM-dd HH:mm` 格式的最后修改时间 |
| 基本要求 | 笔记查询 | 顶部搜索框输入关键字，同时匹配标题与正文，即时刷新 |
| 附加功能 | 界面美化 | 卡片式列表、自定义标题栏、统一配色（品牌蓝 `#2563EB`）、空状态提示 |
| 附加功能 | 收藏星标 | 长按菜单收藏/取消收藏，收藏项置顶并显示金色星标 |
| 附加功能 | 导出为 txt | 通过系统保存对话框导出为 UTF-8 文本，内容为「标题 + 空行 + 正文」 |
| 附加功能 | 笔记底色 | 长按菜单「笔记颜色」，白/蓝/绿/黄/粉/紫六色，按条保存 |
| 附加功能 | 排序切换 | 右上角「⋮」→「排序方式」，按修改时间 / 标题 / 创建时间，选择会被记住 |
| 附加功能 | 导入 txt | 右上角「⋮」→「导入 txt」，选择本地 txt，文件名作标题、全文作正文 |

## 目录结构

```
app/src/main/java/com/example/android/notepad/
├── NotesList.java          列表页：时间戳、搜索、收藏、导出、内容观察者
├── NoteEditor.java         编辑页：自定义标题栏、正文编辑、自动保存
├── TitleEditor.java        修改标题页
├── NotePad.java            契约类（含新增 starred 列）
├── NotePadProvider.java    数据层：建表、版本升级、增删改查
└── NotesLiveFolder.java    原示例遗留文件，未在 Manifest 中注册

app/src/main/res/
├── layout/  notes_list.xml、note_item.xml、note_editor.xml、title_editor.xml
├── drawable/ bg_card.xml、bg_search.xml、bg_btn_primary.xml
├── menu/    list_context_menu.xml
└── values/  strings.xml、colors.xml、themes.xml
```

数据库 `note_pad.db` 版本为 4，表 `notes(_id, title, note, created, modified, starred, color)`；
`onUpgrade` 依次用 `ALTER TABLE ... ADD COLUMN` 追加 `starred`（v3）与 `color`（v4），不删表，保留旧数据。

## 环境与构建

- Windows 11，Android Studio（内置 JDK 17）
- Gradle 9.9.1 + AGP 9.4.1
- compileSdk 37 / minSdk 24 / targetSdk 37
- 无第三方依赖，仓库顺序为阿里云镜像 → `google()` → `mavenCentral()`

```bash
# 命令行构建
set JAVA_HOME=C:\Program Files\Android\Android Studio\jbr
gradlew.bat assembleDebug

# 安装到已启动的模拟器
adb install -r app\build\outputs\apk\debug\app-debug.apk
adb shell am start -S -n com.example.android.notepad/.NotesList
```

注意事项：工程路径不要包含中文字符，否则 AGP 会直接报 `non-ASCII characters` 错误。

## 关键实现

1. **时间戳**：`SimpleCursorAdapter.setViewBinder()` 中判断列名为 `modified` 时用 `SimpleDateFormat` 格式化，不改动 provider 返回结构。
2. **搜索**：`TextWatcher` 每次输入变化后带 `(title LIKE ? OR note LIKE ?)` 的 selection 重新查询，排序为 `starred DESC, modified DESC`。
3. **收藏**：长按菜单调用 `toggleStar()`，只 update `starred` 一列，不修改 `modified`，因此不干扰时间戳。
4. **导出 / 导入**：导出用 `ACTION_CREATE_DOCUMENT` + `openOutputStream()` 写 UTF-8；导入用 `ACTION_OPEN_DOCUMENT` + `openInputStream()` 读取，`DISPLAY_NAME` 去掉 `.txt` 作标题。
5. **底色**：`color` 列 0–5 对应六种颜色，ViewBinder 里按编号新建 `GradientDrawable` 当卡片背景，不复用共享 drawable。
6. **排序**：`buildSortOrder()` 固定 `starred DESC,` 前缀，再拼所选字段；排序方式存在 SharedPreferences，重启后沿用。

## 运行截图

模拟器（Pixel 7 / API 36）实测截图共 14 张，放在仓库根目录的 [docs/screenshots/midterm/](../docs/screenshots/midterm)，按验证顺序编号：空列表(01)、带时间戳的列表(02)、搜索(03)、长按菜单(04)、收藏置顶(05)、编辑页(06)、导出保存页(07)、导出完成(08)、新建笔记(09)、修改标题(10)、卡片底色(11)、排序对话框(12)、按标题排序(12-sort-title)、导入选择与导入完成(13、14)。

## 原工程说明

项目来自于官方早期的数据库操作的基本教程，更多解析参考 [Android Sample--NotePad解析](https://blog.csdn.net/llfjfz/article/details/67638499)。


