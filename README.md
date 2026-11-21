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

## 仓库结构

```
AndroidLabs-121052024037
├── README.md                        # 本文件：实验总览
├── .gitignore                       # 全仓库忽略规则
├── .github/workflows/android.yml    # CI：编译每个实验并上传 APK
└── lab1/                            # 实验一：完整独立 Android 工程
    ├── app/                         # 应用模块（Java 源码 + 资源 + 布局）
    ├── gradle/                      # 版本目录 + wrapper（Gradle 9.9.1）
    ├── settings.gradle              # 仓库声明（阿里云镜像优先）
    ├── build.gradle                 # 顶层构建脚本
    └── gradle.properties            # 全局构建参数
```

## 如何打开某个实验

Android Studio → `File → Open` → 选择 **`lab1`** 这个子目录（不是仓库根目录）。
每个子目录都是一个可以独立编译、独立运行的完整 Android 工程。

## 命令行编译

```bash
cd lab1
./gradlew assembleDebug        # Windows: gradlew.bat assembleDebug
```

产物：`lab1/app/build/outputs/apk/debug/app-debug.apk`

## 持续集成

每次 push 到 `main`，GitHub Actions 会自动编译各实验并上传 APK 产物，
可在仓库 **Actions** 标签页的每次运行记录里下载。
