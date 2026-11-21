# AndroidLabs · Android开发基础课程实验

福建师范大学 计算机与网络空间安全学院（软件工程）课程实验合集 ｜ 121052024037 令狐标霖

## 实验一览

| 实验 | 内容 | 目录 |
| --- | --- | --- |
| 实验一 | 创建第一个 Android 工程并同步至 GitHub | [`lab1/`](./lab1) |
| 实验二 | 界面布局：线性布局 / 表格布局 / 约束布局 / Compose | [`lab2/`](./lab2) |
| 实验三 | 界面组件：ListView / 自定义 AlertDialog / XML 菜单 / ActionMode / RecyclerView | [`lab3/`](./lab3) |
| 期中实验 | NotePad 记事本应用：时间戳 / 搜索 + 六项附加功能 | [`midterm/`](./midterm) |

## 实验一 · 创建第一个 Android 工程

Java + Material3 工程，练习 Activity 生命周期与页面间通信：首页显示欢迎语、学号姓名与进入时间；「点我一下」用计数与 Toast 验证点击回调；「跳转到第二个页面」通过显式 Intent 携带点击次数，第二个页面用 `setResult` 回传消息，首页用 `ActivityResultLauncher` 接收后刷新。

<p align="center">
<img src="docs/screenshots/lab1/01-home.png" width="220">
<img src="docs/screenshots/lab1/02-click5.png" width="220">
<img src="docs/screenshots/lab1/03-second.png" width="220">
<img src="docs/screenshots/lab1/04-reply.png" width="220">
</p>

从左到右：首页初始态 → 点击五次后的计数与 Toast → 第二个页面显示传入的次数 → 回传消息在首页展示。

## 实验二 · 界面布局

同一个界面练习题用五种方式实现，比较不同布局的取舍：`LinearLayout` 的 `layout_weight` 做 4×4 等分网格；`TableLayout` 配 `stretchColumns` 与 `layout_span` 做菜单表；`ConstraintLayout` 用双向链实现计算器键盘；再用相对约束与 `bias` 做太空订票界面；最后用 Jetpack Compose 与状态管理做课程任务清单。

<p align="center">
<img src="docs/screenshots/lab2/lab2-01-nav.png" width="220">
<img src="docs/screenshots/lab2/lab2-02-linear.png" width="220">
<img src="docs/screenshots/lab2/lab2-03-table.png" width="220">
<img src="docs/screenshots/lab2/lab2-04-calc.png" width="220">
</p>

<p align="center">
<img src="docs/screenshots/lab2/lab2-05-space.png" width="220">
<img src="docs/screenshots/lab2/lab2-06-task-init.png" width="220">
<img src="docs/screenshots/lab2/lab2-07-task-add.png" width="220">
<img src="docs/screenshots/lab2/lab2-08-task-two.png" width="220">
</p>

上排：Compose 目录页、4×4 网格、表格布局菜单、约束布局计算器；下排：太空订票界面、Compose 任务清单的初始态 / 添加一条 / 追加两条后的刷新结果。

## 实验三 · 界面组件

五个常用界面组件各做一页，重点是把「组件」和「交互」分开看：`ListView` 用 `SimpleAdapter` 做图文列表，点击弹 Toast 并让选中行变深红；自定义对话框用 `AlertDialog.Builder.setView()` 加载自写布局，带输入校验；XML 菜单做到字号与颜色两个子菜单；`ActionMode` 长按进入选择态，标题显示选中数量并可删除；`RecyclerView` 配 `ViewHolder`，追加记录后局部刷新不错位。

<p align="center">
<img src="docs/screenshots/lab3/lab3-01-main.png" width="220">
<img src="docs/screenshots/lab3/lab3-02-listview.png" width="220">
<img src="docs/screenshots/lab3/lab3-03-dialog.png" width="220">
<img src="docs/screenshots/lab3/lab3-04-menu-page.png" width="220">
</p>

<p align="center">
<img src="docs/screenshots/lab3/lab3-05-menu-overflow.png" width="220">
<img src="docs/screenshots/lab3/lab3-07-actionmode.png" width="220">
<img src="docs/screenshots/lab3/lab3-08-recycler.png" width="220">
</p>

上排：入口页、图文列表、自定义登录对话框、XML 菜单页；下排：菜单子项展开、`ActionMode` 选中态、`RecyclerView` 卡片列表。

## 期中实验 · NotePad 记事本应用

在课程给定源码的基础上扩展实现。基本要求两项：列表条目显示最后修改时间（`yyyy-MM-dd HH:mm`）、按标题或正文实时查询；附加功能六项：卡片式界面美化、收藏星标置顶、导出为 txt、单条笔记换底色、三种排序方式切换、从本地 txt 导入笔记。数据库版本由 2 升到 4，两次升级都用 `ALTER TABLE` 追加列，旧笔记不丢。

<p align="center">
<img src="docs/screenshots/midterm/02-list-time.png" width="220">
<img src="docs/screenshots/midterm/03-search.png" width="220">
<img src="docs/screenshots/midterm/11-note-color.png" width="220">
<img src="docs/screenshots/midterm/05-starred.png" width="220">
</p>

<p align="center">
<img src="docs/screenshots/midterm/07-export-dialog.png" width="220">
<img src="docs/screenshots/midterm/12-sort-dialog.png" width="220">
<img src="docs/screenshots/midterm/13-import-picker.png" width="220">
<img src="docs/screenshots/midterm/14-import-done.png" width="220">
</p>

上排：带时间戳的列表、搜索 `android` 同时命中标题与正文、四色卡片、收藏置顶；下排：导出保存页、排序方式选择、导入时的文件选择、导入成功。

## 仓库结构

```
AndroidLabs-121052024037
├── README.md                        # 本文件：实验总览与截图
├── .gitignore                       # 全仓库忽略规则
├── .github/workflows/android.yml    # CI：编译每个工程并上传 APK
├── docs/screenshots/                # 各实验的运行截图
├── lab1/                            # 实验一：Java + Material3 + Intent
├── lab2/                            # 实验二：Kotlin + Compose + 四种布局
├── lab3/                            # 实验三：ListView / 对话框 / 菜单 / ActionMode / RecyclerView
└── midterm/                         # 期中实验：NotePad（ContentProvider + SQLite）
```

每个目录都是可以独立编译、独立运行的完整 Android 工程，内部结构与普通工程一致（`app/` + `gradle/` + `settings.gradle`）。

