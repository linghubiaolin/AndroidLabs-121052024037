# LayoutLab · Android 界面布局实验

福建师范大学《Android 界面布局》实验二 —— 用五种方式完成同一组界面练习：

| 编号 | 实验 | 技术点 | 布局文件 |
| --- | --- | --- | --- |
| 01 | 4×4 等分网格 | LinearLayout + `layout_weight` | `activity_linear.xml` |
| 02 | 菜单表 | TableLayout + `stretchColumns` / `layout_span` | `activity_table.xml` |
| 03 | 计算器界面 | ConstraintLayout 双向链 + 权重 | `activity_calculator.xml` |
| 04 | 太空订票界面 | ConstraintLayout 相对约束 + `bias` | `activity_space.xml` |
| 05 | 课程学习任务 | Jetpack Compose + 状态管理 | `TaskScreen.kt` |

## 目录结构

```
LayoutLab/
├── app/src/main/
│   ├── java/com/example/layoutlab/
│   │   ├── MainActivity.kt          # 实验入口（Compose 写的目录页）
│   │   ├── LinearLayoutActivity.kt  # 实验一
│   │   ├── TableLayoutActivity.kt   # 实验二
│   │   ├── CalculatorActivity.kt    # 实验三
│   │   ├── SpaceActivity.kt         # 实验四
│   │   ├── ComposeTaskActivity.kt   # 实验五
│   │   └── TaskScreen.kt            # Compose 任务清单界面
│   ├── res/layout/                  # 四个 XML 布局
│   ├── res/drawable/                # 形状与图标
│   ├── res/drawable-nodpi/          # 实验四的 PNG 素材
│   └── res/values/                  # 颜色、字符串、主题
├── build.gradle.kts
├── settings.gradle.kts              # 仓库地址已配置国内镜像
└── gradle/wrapper/                  # Gradle 9.9.1
```

## 运行

1. Android Studio 打开本工程，等待 Gradle 同步完成。
2. 选择模拟器（API 24 及以上）或真机，点击 Run。
3. 首页即为实验目录，点击任意条目进入对应界面。

## 环境

- Android Studio 2026.1.4 / AGP 9.5.1 / Gradle 9.9.1 / Kotlin 2.2.10
- compileSdk 37，minSdk 24
- Compose 由 BOM 统一管理版本

> 说明：AGP 9 已内置 Kotlin 支持，工程中不再应用 `kotlin-android` 插件，只额外应用 Compose 编译器插件。
