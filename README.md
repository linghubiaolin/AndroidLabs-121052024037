# AndroidLabs · Android开发基础课程实验

福建师范大学 计算机与网络空间安全学院（软件工程）《Android开发基础》课程实验合集。

| 项目 | 内容 |
| --- | --- |
| 学号 | 121052024037 |
| 姓名 | 令狐标霖 |
| 语言 | Java |
| 开发环境 | Android Studio 2026.1.4（Quail 4） |
| 构建工具 | Gradle 9.9.1 + AGP 9.4.1 |
| 编译版本 | compileSdk 37 / minSdk 26 / targetSdk 37 |

## 实验目录

| 实验 | 内容 | 目录 | 状态 |
| --- | --- | --- | --- |
| 实验一 | 创建第一个 Android 工程并同步至 GitHub | [`lab1/`](./lab1) | ✅ 已完成 |
| 实验二 | 界面布局：线性布局 / 表格布局 / 约束布局 / Compose | [`lab2/`](./lab2) | ✅ 已完成 |
| 实验三 | 界面组件：ListView / 自定义 AlertDialog / XML 菜单 / ActionMode / RecyclerView | [`lab3/`](./lab3) | ✅ 已完成 |
| 期中实验 | NotePad 记事本应用：时间戳 / 搜索 + 六项附加功能 | [`midterm/`](./midterm) | ✅ 已完成 |

## 仓库结构

```
AndroidLabs-121052024037
├── README.md                        # 本文件：实验总览
├── .gitignore                       # 全仓库忽略规则
├── .github/workflows/android.yml    # CI：编译每个实验并上传 APK
├── lab1/                            # 实验一：Java + Material3 + Intent（完整独立工程）
├── lab2/                            # 实验二：Kotlin + Compose + 四种布局
├── lab3/                            # 实验三：ListView / 对话框 / 菜单 / ActionMode / RecyclerView
└── midterm/                         # 期中实验：NotePad 记事本应用（ContentProvider + SQLite）
```

每个 `labN/` 与 `midterm/` 都是一个可以独立编译、独立运行的完整 Android 工程，
内部结构与普通 Android 工程完全一致（`app/` + `gradle/` + `settings.gradle`）。

## 如何打开某个实验

Android Studio → `File → Open` → 选择具体实验目录（例如 **`lab1`**），**不要**选仓库根目录。

## 命令行编译

```bash
cd lab1
./gradlew assembleDebug        # Windows: gradlew.bat assembleDebug
```

产物：`lab1/app/build/outputs/apk/debug/app-debug.apk`

## 持续集成

每次 push 到 `main`，GitHub Actions 会自动编译各实验并上传 APK 产物，
可在仓库 **Actions** 标签页的每次运行记录里下载。
