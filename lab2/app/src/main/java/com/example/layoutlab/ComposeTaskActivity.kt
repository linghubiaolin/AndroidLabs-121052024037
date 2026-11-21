package com.example.layoutlab

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

/** 实验五：Jetpack Compose 课程学习任务 */
class ComposeTaskActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TaskScreen()
        }
    }
}
