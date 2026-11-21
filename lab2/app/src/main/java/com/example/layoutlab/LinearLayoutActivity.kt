package com.example.layoutlab

import android.os.Bundle
import androidx.activity.ComponentActivity

/** 实验一：线性布局 —— activity_linear.xml */
class LinearLayoutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linear)
    }
}
