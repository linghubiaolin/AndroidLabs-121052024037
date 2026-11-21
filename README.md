# AndroidLabs · Android开发基础课程实验

福建师范大学 计算机与网络空间安全学院（软件工程）课程实验合集 ｜ 121052024037 令狐标霖

## 实验目录

| 实验 | 内容 | 目录 | 状态 |
| --- | --- | --- | --- |
| 实验一 | 创建第一个 Android 工程并同步至 GitHub | [`lab1/`](./lab1) | 已完成 |
| 实验二 | 界面布局：线性布局 / 表格布局 / 约束布局 / Compose | [`lab2/`](./lab2) | 已完成 |
| 实验三 | 界面组件：ListView / 自定义 AlertDialog / XML 菜单 / ActionMode / RecyclerView | [`lab3/`](./lab3) | 已完成 |
| 期中实验 | NotePad 记事本应用：时间戳 / 搜索 + 六项附加功能 | [`midterm/`](./midterm) | 已完成 |

## 仓库结构

```
AndroidLabs-121052024037
├── README.md                        # 本文件：实验总览
├── .gitignore                       # 全仓库忽略规则
├── .github/workflows/android.yml    # CI：编译每个工程并上传 APK
├── lab1/                            # 实验一：Java + Material3 + Intent
├── lab2/                            # 实验二：Kotlin + Compose + 四种布局
├── lab3/                            # 实验三：ListView / 对话框 / 菜单 / ActionMode / RecyclerView
└── midterm/                         # 期中实验：NotePad（ContentProvider + SQLite）
```

每个目录都是可以独立编译、独立运行的完整 Android 工程，
内部结构与普通工程一致（`app/` + `gradle/` + `settings.gradle`）。

## 开发环境

- Android Studio 2026.1.4（Quail 4），语言 Java（lab2 的 Compose 页为 Kotlin）
- Gradle 9.9.1 + AGP 9.4.1，compileSdk 37 / minSdk 24–26 / targetSdk 37（各工程略有差异，见各自 README）

## 如何打开某个实验

Android Studio → `File → Open` → 选择具体目录（例如 `lab1`），**不要**选仓库根目录。

## 命令行编译

```bash
cd lab1
./gradlew assembleDebug        # Windows: gradlew.bat assembleDebug
```

产物：`lab1/app/build/outputs/apk/debug/app-debug.apk`（其他目录同理）。

## 持续集成

每次 push 到 `main`，`.github/workflows/android.yml` 会把四个工程各编译一遍，
APK 作为产物挂在 **Actions** 页签的运行记录里，可直接下载。
